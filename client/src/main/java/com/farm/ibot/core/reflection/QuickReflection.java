package com.farm.ibot.core.reflection;

import com.farm.ibot.core.Bot;
import com.farm.ibot.core.reflection.classloader.GameClassLoader;

import java.lang.reflect.Field;

public class QuickReflection {
    public static Field[] getFields(String className) {
        GameClassLoader loader = getClassLoader();
        return loader != null ? loader.getFields(className) : new Field[0];
    }

    public static <T> T getValue(Hook hook) {
        return getValue(hook, null);
    }

    public static <T> T getValue(Hook hook, Object instance) {
        GameClassLoader loader = getClassLoader();
        return loader != null ? loader.getValue(hook, instance) : null;
    }

    public static boolean setValue(Hook hook, Object value) {
        return setValue(hook, value, null);
    }

    public static boolean setValue(Hook hook, Object value, Object instance) {
        GameClassLoader loader = getClassLoader();
        return loader != null && loader.setValue(hook, value, instance);
    }

    public static Object invokeMethod(Hook hook, Object instance, Object[] args) {
        GameClassLoader loader = getClassLoader();
        return loader != null ? loader.invokeMethod(hook, instance, args) : null;
    }

    public static GameClassLoader getClassLoader() {
        Bot bot = Bot.get();
        return bot != null ? bot.getGameLoader().getClassLoader() : null;
    }
}
