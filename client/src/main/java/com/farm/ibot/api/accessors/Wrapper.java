// Decompiled with: FernFlower
package com.farm.ibot.api.accessors;

import com.farm.ibot.core.reflection.Hook;
import com.farm.ibot.core.reflection.Reflection;
import com.farm.ibot.init.Hooks;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Wrapper {
    public final Object instance;

    public Wrapper(Object instance) {
        this.instance = instance;
    }

    public static void setStatic(String hookName, Object value) {
        Reflection.setValue(Hooks.forName(hookName), value);
    }

    public static void set(String hookName, Object instance, Object value) {
        Reflection.setValue(Hooks.forName(hookName), value, instance);
    }

    public static Object invokeStatic(String hookName, Object... args) {
        return Reflection.invokeMethod(Hooks.forName(hookName), (Object) null, args);
    }

    public static Object invoke(Object instance, String hookName, Object... args) {
        return Reflection.invokeMethod(Hooks.forName(hookName), instance, args);
    }

    public static String getCalledHook() {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        StackTraceElement element = ste[3];
        String className = element.getClassName().substring(element.getClassName().lastIndexOf(".") + 1);
        String hookName = className + "." + element.getMethodName();
        hookName = hookName.replaceFirst("get|set|is", "");
        return hookName;
    }

    public static Object getStatic() {
        return getStatic(getCalledHook());
    }

    public static void setStatic(Object value) {
        Reflection.setValue(Hooks.forName(getCalledHook()), value);
    }

    public static Object getStatic(String hookName) {
        return Reflection.getValue((Hook) Hooks.forName(hookName), (Object) null);
    }

    public static Object get(String hookName, Object instance) {
        return Reflection.getValue(Hooks.forName(hookName), instance);
    }

    public static Object get(String hookName, Class type, Object instance) {
        if (type.isArray()) {
            try {
                return createArray(type, instance, hookName);
            } catch (Exception var5) {
                var5.printStackTrace();
                return null;
            }
        } else {
            Object object = Reflection.getValue(Hooks.forName(hookName), instance);

            try {
                return object != null ? type.getConstructor(Object.class).newInstance(object) : null;
            } catch (Exception var6) {
                var6.printStackTrace();
                return Reflection.getValue(Hooks.forName(hookName), instance);
            }
        }
    }

    public static Object getStatic(Class type) {
        return getStatic(getCalledHook(), type);
    }

    public static Object getStatic(String hookName, Class type) {
        if (type.isArray()) {
            try {
                return createArray(type, (Object) null, hookName);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        } else {
            Object object = Reflection.getValue((Hook) Hooks.forName(hookName), (Object) null);

            try {
                return object != null ? type.getConstructor(Object.class).newInstance(object) : null;
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return null;
    }

    public static Object createArray(Class clazz, Object mainInstance, String hookName) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int dimensionCount = clazz.getName().split("\\[").length - 1;
        if (dimensionCount == 1) {
            return createArray1(clazz, mainInstance, hookName);
        } else if (dimensionCount == 2) {
            return createArray2(clazz, mainInstance, hookName);
        } else if (dimensionCount == 3) {
            return createArray3(clazz, mainInstance, hookName);
        } else {

            return null;
        }
    }

    private static Object createArray1(Class clazz, Object mainInstance, String hookName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] temp;
        if (mainInstance != null && mainInstance.getClass().isArray()) {
            temp = (Object[]) ((Object[]) mainInstance);
        } else {
            temp = (Object[]) Reflection.getValue(Hooks.forName(hookName), mainInstance);
        }

        if (temp == null) {
            return null;
        } else if (clazz != null && clazz.getComponentType() != null) {
            Object[] newArray = (Object[]) ((Object[]) Array.newInstance(clazz.getComponentType(), temp.length));
            Constructor constructor = clazz.getComponentType().getDeclaredConstructor(Object.class);

            for (int i = 0; i < temp.length; ++i) {
                if (temp[i] != null) {
                    newArray[i] = constructor.newInstance(temp[i]);
                }
            }

            return newArray;
        } else {
            return null;
        }
    }

    private static Object createArray2(Class clazz, Object mainInstance, String hookName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[][] temp;
        if (mainInstance != null && mainInstance.getClass().isArray()) {
            temp = (Object[][]) ((Object[][]) mainInstance);
        } else {
            temp = (Object[][]) Reflection.getValue(Hooks.forName(hookName), mainInstance);
        }

        if (temp == null) {
            return null;
        } else {
            Object[][] newArray = (Object[][]) ((Object[][]) Array.newInstance(clazz.getComponentType(), temp.length));

            for (int i = 0; i < temp.length; ++i) {
                if (temp[i] != null) {
                    newArray[i] = (Object[]) createArray1(clazz.getComponentType(), temp[i], hookName);
                }
            }

            return newArray;
        }
    }

    private static Object createArray3(Class clazz, Object mainInstance, String hookName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[][][] temp = (Object[][][]) Reflection.getValue(Hooks.forName(hookName), mainInstance);
        if (temp == null) {
            return null;
        } else {
            Object[][][] newArray = (Object[][][]) ((Object[][][]) Array.newInstance(clazz.getComponentType(), temp.length));

            for (int i = 0; i < temp.length; ++i) {
                if (temp[i] != null) {
                    newArray[i] = (Object[][]) createArray2(clazz.getComponentType(), temp[i], hookName);
                }
            }

            return newArray;
        }
    }

    public void set(String hookName, Object value) {
        Reflection.setValue(Hooks.forName(hookName), value, this.instance);
    }

    public Object invoke(String hookName, Object... args) {
        return Reflection.invokeMethod(Hooks.forName(hookName), this.instance, args);
    }

    public Object get() {
        return this.get(getCalledHook());
    }

    public Object get(String hookName) {
        return Reflection.getValue(Hooks.forName(hookName), this.instance);
    }

    public Object get(Class type) {
        return this.get(getCalledHook(), type);
    }

    public Object get(String hookName, Class type) {
        return get(hookName, type, this.instance);
    }

    public boolean equals(Object other) {
        return other instanceof Wrapper && this.instance.equals(((Wrapper) other).instance);
    }
}
