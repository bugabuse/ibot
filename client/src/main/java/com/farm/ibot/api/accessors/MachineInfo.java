package com.farm.ibot.api.accessors;

import com.farm.ibot.api.wrapper.HookName;

public class MachineInfo extends Wrapper {
    public MachineInfo(Object instance) {
        super(instance);
    }

    @HookName("MachineInfo.instance")
    public static MachineInfo getInstance() {
        return (MachineInfo) getStatic("MachineInfo.instance", MachineInfo.class);
    }

    @HookName("MachineInfo.instance")
    public static void setInstance(Object instance) {
        setStatic("MachineInfo.instance", instance);
    }
}
