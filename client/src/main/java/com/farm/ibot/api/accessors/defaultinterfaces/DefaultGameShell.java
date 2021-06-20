package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.GameShell;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IGameShell;

import java.awt.*;

public class DefaultGameShell implements IGameShell {
    public boolean hasCrashed(Object instance, Object value) {
        return (Boolean) Wrapper.get("GameShell.crashed", instance);
    }

    public GameShell getInstance(Object instance) {
        return (GameShell) Wrapper.getStatic("GameShell.instance", GameShell.class);
    }

    public Canvas getCanvas(Object instance) {
        return (Canvas) Wrapper.get("Client.Canvas", instance);
    }
}
