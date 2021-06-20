package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.util.PaintUtils;
import com.farm.ibot.api.world.WorldData;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;

public class CollisionDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        PaintUtils.drawTile(g, Player.getLocal().getPosition());
        this.drawString(g, "" + WorldData.getCollisionFlag(Player.getLocal().getPosition()));
    }
}
