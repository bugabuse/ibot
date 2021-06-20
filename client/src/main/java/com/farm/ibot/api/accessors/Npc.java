package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.INpc;
import com.farm.ibot.api.data.definition.NpcDefinition;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.interfaces.Interactable;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Npc extends Character implements Interactable {
    public Npc(Object instance) {
        super(instance);
    }

    public static INpc getNpcInterface() {
        return Bot.get().accessorInterface.npcInterface;
    }

    @HookName("Npc.LocalNpcs")
    public static Npc[] getLocalNpcs() {
        return getNpcInterface().getLocalNpcs(null);
    }

    @HookName("Npc.Composite")
    public NpcComposite getComposite() {
        return getNpcInterface().getComposite(this.instance);
    }

    public NpcDefinition getDefinition() {
        return NpcDefinition.forId(this.getId());
    }

    public int getIndex() {
        int id = 0;
        Npc[] npcs = getLocalNpcs();
        if (npcs != null) {
            Npc[] var3 = npcs;
            int var4 = npcs.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Npc npc = var3[var5];
                if (npc != null && npc.equals(this)) {
                    return id;
                }

                ++id;
            }
        }

        return -1;
    }

    public int getId() {
        return this.getComposite() != null ? this.getComposite().getRealId() : -1;
    }

    public String[] getActions() {
        return this.getDefinition().actions;
    }

    public String getName() {
        return this.getDefinition().name;
    }

    public boolean interact(String action) {
        return Interact.interactHandler.npcInteract(action, this);
    }

    public boolean exists() {
        Npc[] local = getLocalNpcs();
        int index = this.getIndex();
        return index > -1 && local.length > index && this.equals(local[index]);
    }
}
