package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.HealthBarData;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IHealthBar;

public class DefaultHealthBar implements IHealthBar {
    public HealthBarData getData(Object instance) {
        return (HealthBarData) Wrapper.get("HealthBar.data", HealthBarData.class, instance);
    }
}
