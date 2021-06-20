package com.farm.ibot.core.reflection.classloader;

import com.farm.ibot.core.reflection.Hook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class GameReflectionClassLoader extends GameClassLoader {
    private final HashMap<String, Method> methodMap = new HashMap();
    private final HashMap<String, Class> classMap = new HashMap();
    private final HashMap<String, Field> fieldMap = new HashMap();
    private URLClassLoader loader;

    public GameReflectionClassLoader(URL... urls) {
        this.loader = new URLClassLoader(urls);
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        return this.loader.loadClass(name);
    }

    public ClassLoader getClassLoader() {
        return this.loader;
    }

    public void unload() {
    }

    public Class getClass(String name) {
        if (this.classMap.containsKey(name)) {
            return (Class) this.classMap.get(name);
        } else {
            try {
                Class c = this.loadClass(name);
                this.classMap.put(name, c);
                return c;
            } catch (ClassNotFoundException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public Method getMethod(String methodLocation, int argLength) {
        Method m = (Method) this.methodMap.get(methodLocation);
        if (m != null) {
            return m;
        } else {
            String className = methodLocation.substring(0, methodLocation.lastIndexOf("."));
            String methodName = methodLocation.substring(methodLocation.lastIndexOf(".") + 1, methodLocation.length());
            Class c = this.getClass(className);
            if (c != null) {
                Method[] var7 = c.getDeclaredMethods();
                int var8 = var7.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    Method method = var7[var9];
                    if (method.getName().equals(methodName) && method.getParameterCount() == argLength) {
                        method.setAccessible(true);
                        this.methodMap.put(methodLocation, method);
                        return method;
                    }
                }
            }

            return null;
        }
    }

    public Field[] getFields(String className) {
        Class c = this.getClass(className);
        return c.getDeclaredFields();
    }

    public Field getField(String fieldLocation) {
        Field f = (Field) this.fieldMap.get(fieldLocation);
        if (f != null) {
            return f;
        } else {
            String className = fieldLocation.substring(0, fieldLocation.lastIndexOf("."));
            String fieldName = fieldLocation.substring(fieldLocation.lastIndexOf(".") + 1, fieldLocation.length());
            Class c = this.getClass(className);
            if (c != null) {
                try {
                    Field field = c.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    this.fieldMap.put(fieldLocation, field);
                    return field;
                } catch (NoSuchFieldException var7) {
                    var7.printStackTrace();
                }
            }

            return null;
        }
    }

    public <T> T getValue(String obfuscatedName, Object instance) {
        try {
            Field f = this.getField(obfuscatedName);
            if (f == null) {
                return null;
            } else if (instance == null && !Modifier.isStatic(f.getModifiers())) {
                return null;
            } else {
                return f.get(instance) == null ? null : (T) f.get(instance);
            }
        } catch (IllegalAccessException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public <T> T getValue(Hook hook, Object instance) {
        try {
            Field f = null;
            if (hook != null) {
                f = this.getField(hook.obfuscatedName);
            }

            if (f == null) {
                return null;
            } else if (instance == null && !Modifier.isStatic(f.getModifiers())) {
                return null;
            } else {
                Object o = f.get(instance);
                if (o == null) {
                    return null;
                } else if (hook.getterMultipler != null) {
                    if (f.getType() == Long.TYPE) {
                        Object n = (Long) o * hook.getterMultipler.longValue();
                        return (T) n;
                    } else {
                        Object n = (Integer) o * hook.getterMultipler.intValue();
                        return (T) n;
                    }
                } else {
                    return (T) o;
                }
            }
        } catch (IllegalAccessException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public boolean setValue(Hook hook, Object value, Object instance) {
        Field field = null;
        if (hook != null) {
            field = this.getField(hook.obfuscatedName);
        }

        if (field != null) {
            try {
                if (hook.getterMultipler != null) {
                    if (field.getType() == Long.TYPE) {
                        field.set(instance, (Long) value * hook.setterMultipler.longValue());
                    } else {
                        field.set(instance, (Integer) value * hook.setterMultipler.intValue());
                    }
                } else {
                    field.set(instance, value);
                }

                return true;
            } catch (IllegalAccessException var6) {
                var6.printStackTrace();
            }
        }

        return false;
    }

    public boolean setValue(String obfuscatedName, Object value, Object instance) {
        Field field = this.getField(obfuscatedName);
        if (field != null) {
            try {
                field.set(instance, value);
                return true;
            } catch (IllegalAccessException var6) {
                var6.printStackTrace();
            }
        }

        return false;
    }

    public Object invokeMethod(Hook hook, Object instance, Object[] args) {
        Method method = null;
        if (hook != null) {
            method = this.getMethod(hook.obfuscatedName, args.length);
        } else {

        }

        if (method != null) {
            try {
                Object obj = method.invoke(instance, args);
                return obj;
            } catch (InvocationTargetException | IllegalAccessException var6) {
                var6.printStackTrace();
            }
        } else {

        }

        return null;
    }
}
