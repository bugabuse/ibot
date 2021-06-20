package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.INpcComposite;

public class DefaultNpcComposite implements INpcComposite {
    public int getId(Object instance) {
        return (Integer) Wrapper.get("NpcComposite.Id", instance);
    }

    public int[] getRealIds(Object instance) {
        return (int[]) Wrapper.get("NpcComposite.RealIds", instance);
    }
}
