package com.farm.ibot.core.reflection.classloader;

import com.farm.ibot.core.reflection.Hook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class GameClassLoader {
    public abstract Class getClass(String var1);

    public abstract Method getMethod(String var1, int var2);

    public abstract Field[] getFields(String var1);

    public abstract Field getField(String var1);

    public abstract <T> T getValue(Hook var1, Object var2);

    public abstract boolean setValue(Hook var1, Object var2, Object var3);

    public abstract boolean setValue(String var1, Object var2, Object var3);

    public abstract Object invokeMethod(Hook var1, Object var2, Object[] var3);

    public abstract Class loadClass(String var1) throws ClassNotFoundException;

    public abstract ClassLoader getClassLoader();

    public abstract void unload();

    public abstract <T> T getValue(String var1, Object var2);
}
