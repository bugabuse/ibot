package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IConfig;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Config extends Wrapper {
    public Config(Object instance) {
        super(instance);
    }

    public static IConfig getConfigInterface() {
        return Bot.get().accessorInterface.configInterface;
    }

    @HookName("Config.Values")
    public static int[] getValues() {
        return getConfigInterface().getValues(null);
    }

    public static int get(int id) {
        int[] values = getValues();
        return values != null && values.length > id && id > 0 ? values[id] : -1;
    }

    public static void set(int id, int value) {
        int[] values = getValues();
        if (values != null && values.length > id && id > 0) {
            values[id] = value;
        }

    }
}
