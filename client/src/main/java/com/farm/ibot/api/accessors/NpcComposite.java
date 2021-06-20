package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.INpcComposite;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class NpcComposite extends Wrapper {
    public NpcComposite(Object instance) {
        super(instance);
    }

    public static INpcComposite getNpcCompositeInterface() {
        return Bot.get().accessorInterface.npcCompositeInterface;
    }

    @HookName("NpcComposite.Id")
    public int getId() {
        return getNpcCompositeInterface().getId(this.instance);
    }

    @HookName("NpcComposite.RealIds")
    public int[] getRealIds() {
        return getNpcCompositeInterface().getRealIds(this.instance);
    }

    public int getRealId() {
        int[] ids = this.getRealIds();
        if (ids != null && ids.length > 0) {
            return ids[0] > -1 ? ids[0] : this.getId();
        } else {
            return this.getId();
        }
    }
}
