package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IComputerInfo;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class ComputerInfo extends Wrapper {
    public ComputerInfo(Object instance) {
        super(instance);
    }

    public static IComputerInfo getComputerInfoInterface() {
        return Bot.get().accessorInterface.computerInfoInterface;
    }

    @HookName("ComputerInfo.instance")
    public static ComputerInfo getInstance() {
        return getComputerInfoInterface().getInstance(null);
    }

    @HookName("ComputerInfo.systemId")
    public int getSystemId() {
        return getComputerInfoInterface().getSystemId(this.instance);
    }
}
