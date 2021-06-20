package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IGameConfig;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class GameConfig extends Wrapper {
    public GameConfig(Object instance) {
        super(instance);
    }

    public static IGameConfig getGameConfigInterface() {
        return Bot.get().accessorInterface.gameConfigInterface;
    }

    @HookName("GameConfig.RoofsEnabled")
    public boolean isRoofsEnabled() {
        return getGameConfigInterface().isRoofsEnabled(this.instance, null);
    }

    @HookName("GameConfig.RoofsEnabled")
    public void setRoofsEnabled(boolean value) {
        getGameConfigInterface().isRoofsEnabled(this.instance, value);
    }

    @HookName("GameConfig.ResizableMode")
    public int getResizableMode() {
        return getGameConfigInterface().getResizableMode(this.instance);
    }

    public boolean isFixedMode() {
        return Client.getGameConfig().getResizableMode() == 1;
    }
}
