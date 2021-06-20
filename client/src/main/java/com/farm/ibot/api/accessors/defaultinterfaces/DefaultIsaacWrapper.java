package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IIsaacWrapper;

public class DefaultIsaacWrapper implements IIsaacWrapper {
    public int[] getKey(Object instance) {
        return (int[]) Wrapper.get("IsaacWrapper.key", instance);
    }
}
