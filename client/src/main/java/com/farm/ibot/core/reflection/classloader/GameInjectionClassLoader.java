// Decompiled with: Procyon 0.5.36
package com.farm.ibot.core.reflection.classloader;

import com.farm.ibot.api.util.Debug;
import com.farm.ibot.core.reflection.Hook;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.init.Settings;
import javassist.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class GameInjectionClassLoader extends GameClassLoader {
    private static HashMap<String, String> primitives;

    static {
        (GameInjectionClassLoader.primitives = new HashMap<String, String>()).put("java.lang.Boolean", "boolean");
        GameInjectionClassLoader.primitives.put("java.lang.Character", "char");
        GameInjectionClassLoader.primitives.put("java.lang.Byte", "byte");
        GameInjectionClassLoader.primitives.put("java.lang.Short", "short");
        GameInjectionClassLoader.primitives.put("java.lang.Integer", "int");
        GameInjectionClassLoader.primitives.put("java.lang.Long", "long");
        GameInjectionClassLoader.primitives.put("java.lang.Float", "float");
        GameInjectionClassLoader.primitives.put("java.lang.Double", "double");
        GameInjectionClassLoader.primitives.put("java.lang.Void", "void");
    }

    public final HashMap<String, Class> classMap;
    private final HashMap<String, Method> methodMap;
    private final HashMap<String, Field> fieldMap;
    public ClassPool cp;
    public Loader loader;

    public GameInjectionClassLoader(final String directory) {
        this.methodMap = new HashMap<String, Method>();
        this.classMap = new HashMap<String, Class>();
        this.fieldMap = new HashMap<String, Field>();
        this.cp = new ClassPool(false);
        final ClassPath temp = new ClassClassPath(BotScript.class);
        try {
            this.cp.appendClassPath(directory);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if (Settings.useInjection) {
            Debug.log("Generating Injections");
            try {
                final com.farm.ibot.core.util.DirClassPath injectedApiClassPath = new com.farm.ibot.core.util.DirClassPath(Settings.API_DATA.getAbsolutePath());
                final Injector injector = new Injector(this.cp);
                this.cp.appendClassPath(injectedApiClassPath);
                this.cp.appendClassPath(temp);
                injector.generateInjectedApiAccessors();
                this.cp.removeClassPath(temp);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            Debug.log("Finished Injections");
        }
        this.loader = new Loader(this.cp);
    }

    private static String toPrimitiveName(final String className) {
        return GameInjectionClassLoader.primitives.getOrDefault(className, className);
    }

    @Override
    public Class loadClass(final String name) throws ClassNotFoundException {
        return this.loader.loadClass(name);
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.loader;
    }

    @Override
    public void unload() {
        try {
            final Field f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);
            final Vector<Class> classes = (Vector<Class>) f.get(this.loader);
            for (final Class c : new ArrayList<Class>(classes)) {
                for (final Field field : c.getDeclaredFields()) {
                    try {
                        field.setAccessible(true);
                        field.set(null, null);
                    } catch (Exception ex) {
                    }
                }
            }
            f.set(this.loader, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T getValue(final String obfuscatedName, final Object instance) {
        try {
            final Field f = this.getField(obfuscatedName);
            if (f == null) {
                return null;
            }
            if (instance == null && !Modifier.isStatic(f.getModifiers())) {
                return null;
            }
            final Object o = f.get(instance);
            if (o == null) {
                return null;
            }
            return (T) o;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Class getClass(final String name) {
        if (this.classMap.containsKey(name)) {
            return this.classMap.get(name);
        }
        try {
            final Class c = this.loadClass(name);
            this.classMap.put(name, c);
            return c;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Method getMethod(final String methodLocation, final int argLength) {
        return null;
    }

    public Method getMethod(final String methodLocation, final Object[] params) {
        final Method m = this.methodMap.get(methodLocation);
        if (m != null) {
            return m;
        }
        final String className = methodLocation.substring(0, methodLocation.lastIndexOf("."));
        final String methodName = methodLocation.substring(methodLocation.lastIndexOf(".") + 1, methodLocation.length());
        final Class c = this.getClass(className);
        if (c != null) {
            for (final Method method : c.getDeclaredMethods()) {
                Label_0186:
                {
                    if (method.getName().equals(methodName) && method.getParameterCount() == params.length) {
                        for (int i = 0; i < params.length; ++i) {
                            if (!toPrimitiveName(method.getParameterTypes()[i].getName()).equals(toPrimitiveName(params[i].getClass().getName()))) {
                                break Label_0186;
                            }
                        }
                        method.setAccessible(true);
                        this.methodMap.put(methodLocation, method);
                        return method;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Field[] getFields(final String className) {
        final Class c = this.getClass(className);
        return c.getDeclaredFields();
    }

    @Override
    public Field getField(final String fieldLocation) {
        final Field f = this.fieldMap.get(fieldLocation);
        if (f != null) {
            return f;
        }
        final String className = fieldLocation.substring(0, fieldLocation.lastIndexOf("."));
        final String fieldName = fieldLocation.substring(fieldLocation.lastIndexOf(".") + 1, fieldLocation.length());
        final Class c = this.getClass(className);
        if (c != null) {
            try {
                final Field field = c.getDeclaredField(fieldName);
                field.setAccessible(true);
                this.fieldMap.put(fieldLocation, field);
                return field;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public <T> T getValue(final Hook hook, final Object instance) {
        try {
            Field f = null;
            if (hook != null) {
                f = this.getField(hook.obfuscatedName);
            }
            if (f == null) {
                return null;
            }
            if (instance == null && !Modifier.isStatic(f.getModifiers())) {
                return null;
            }
            final Object o = f.get(instance);
            if (hook.getterMultipler == null) {
                return (T) o;
            }
            if (f.getType() == Long.TYPE) {
                final Object n = (long) o * hook.getterMultipler.longValue();
                return (T) n;
            }
            final Object n = (int) o * hook.getterMultipler.intValue();
            return (T) n;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean setValue(final Hook hook, final Object value, final Object instance) {
        Field field = null;
        if (hook != null) {
            field = this.getField(hook.obfuscatedName);
        }
        if (field != null) {
            try {
                if (hook.getterMultipler != null) {
                    if (field.getType() == Long.TYPE) {
                        field.set(instance, (long) value * hook.setterMultipler.longValue());
                    } else {
                        field.set(instance, (int) value * hook.setterMultipler.intValue());
                    }
                } else {
                    field.set(instance, value);
                }
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean setValue(final String obfuscatedName, final Object value, final Object instance) {
        final Field field = this.getField(obfuscatedName);
        if (field != null) {
            try {
                field.set(instance, value);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Object invokeMethod(final Hook hook, final Object instance, final Object[] args) {
        Method method = null;
        if (hook != null) {
            method = this.getMethod(hook.obfuscatedName, args);
        } else {

        }
        if (method != null) {
            try {
                method.setAccessible(true);
                final Object obj = method.invoke(instance, args);
                return obj;
            } catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex2;
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }
}
