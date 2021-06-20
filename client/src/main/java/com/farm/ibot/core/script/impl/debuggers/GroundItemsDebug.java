package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.Iterator;

public class GroundItemsDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        Iterator var2 = GroundItems.getAll((f) -> {
            return true;
        }).iterator();

        while (var2.hasNext()) {
            GroundItem item = (GroundItem) var2.next();
            Polygon poly = item.getPosition().getBounds();
            g.drawPolygon(poly);
            this.drawString(g, item.getId() + " - " + item.getPosition() + " [ " + item.getAnimablePosition() + " ]");
            if (Screen.isOnGameScreen(item.getScreenPoint())) {
                g.drawString("" + item.getId(), item.getScreenPoint().x, item.getScreenPoint().y);
            }
        }

    }
}
