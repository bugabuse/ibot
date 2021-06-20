package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IPlayerComposite;

public class DefaultPlayerComposite implements IPlayerComposite {
    public int[] getAppearance(Object instance) {
        return (int[]) Wrapper.get("PlayerComposite.Appearance", instance);
    }
}
