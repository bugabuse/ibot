package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IHealthBar;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class HealthBar extends Node {
    public HealthBar(Object instance) {
        super(instance);
    }

    public static IHealthBar getHealthBarInterface() {
        return Bot.get().accessorInterface.healthBar;
    }

    @HookName("HealthBar.data")
    public HealthBarData getData() {
        return getHealthBarInterface().getData(this.instance);
    }
}
