package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.NpcComposite;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.INpc;

public class DefaultNpc implements INpc {
    public Npc[] getLocalNpcs(Object instance) {
        return (Npc[]) Wrapper.getStatic("Npc.LocalNpcs", Npc[].class);
    }

    public NpcComposite getComposite(Object instance) {
        return (NpcComposite) Wrapper.get("Npc.Composite", NpcComposite.class, instance);
    }
}
