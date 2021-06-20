package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.GameShell;

import java.awt.*;

public interface IGameShell {
    boolean hasCrashed(Object var1, Object var2);

    GameShell getInstance(Object var1);

    Canvas getCanvas(Object var1);
}
