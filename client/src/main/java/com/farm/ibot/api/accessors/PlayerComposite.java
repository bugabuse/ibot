package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IPlayerComposite;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class PlayerComposite extends Wrapper {
    public PlayerComposite(Object instance) {
        super(instance);
    }

    public static IPlayerComposite getPlayerInterface() {
        return Bot.get().accessorInterface.playerCompositeInterface;
    }

    @HookName("PlayerComposite.Appearance")
    public int[] getAppearance() {
        return getPlayerInterface().getAppearance(this.instance);
    }
}
