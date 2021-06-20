package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IHealthBarData;

public class DefaultHealthBarData implements IHealthBarData {
    public int getCurrentHealth(Object instance) {
        return (Integer) Wrapper.get("HealthBarData.currentHealth", instance);
    }
}
