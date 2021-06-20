package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IConfig;

public class DefaultConfig implements IConfig {
    public int[] getValues(Object instance) {
        return (int[]) Wrapper.get("Config.Values", instance);
    }
}
