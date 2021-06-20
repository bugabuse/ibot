package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;

public class RenderDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
    }
}
