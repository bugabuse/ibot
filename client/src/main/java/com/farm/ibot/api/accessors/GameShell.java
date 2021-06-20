package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IGameShell;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

import java.awt.*;

public class GameShell extends Wrapper {
    public GameShell(Object instance) {
        super(instance);
    }

    public static IGameShell getGameShellInterface() {
        return Bot.get().accessorInterface.gameShellInterface;
    }

    @HookName("GameShell.instance")
    public static GameShell getInstance() {
        return getGameShellInterface().getInstance(null);
    }

    @HookName("GameShell.crashed")
    public boolean hasCrashed() {
        return getGameShellInterface().hasCrashed(this.instance, null);
    }

    @HookName("Client.Canvas")
    public Canvas getCanvas() {
        return getGameShellInterface().getCanvas(this.instance);
    }
}
