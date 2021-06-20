package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.ComputerInfo;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IComputerInfo;

public class DefaultComputerInfo implements IComputerInfo {
    public ComputerInfo getInstance(Object instance) {
        return (ComputerInfo) Wrapper.get("ComputerInfo.instance", ComputerInfo.class, instance);
    }

    public int getSystemId(Object instance) {
        return (Integer) Wrapper.get("ComputerInfo.systemId", instance);
    }
}
