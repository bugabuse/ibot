package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;

public class GameTabDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Opened Game tab: " + GameTab.getOpened());
        GameTab[] var2 = GameTab.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            GameTab tab = var2[var4];
            if (tab.getWidget() != null) {
                this.drawString(g, tab.name() + " " + tab.getWidget().getTextureId());
            }
        }

    }
}
