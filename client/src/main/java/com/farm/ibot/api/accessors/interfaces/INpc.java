package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.NpcComposite;

public interface INpc {
    Npc[] getLocalNpcs(Object var1);

    NpcComposite getComposite(Object var1);
}
