package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IHealthBarData;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class HealthBarData extends Node {
    public HealthBarData(Object instance) {
        super(instance);
    }

    public static IHealthBarData getHealthBarDataInterface() {
        return Bot.get().accessorInterface.healthBarData;
    }

    @HookName("HealthBarData.currentHealth")
    public int getCurrentHealth() {
        return getHealthBarDataInterface().getCurrentHealth(this.instance);
    }
}
