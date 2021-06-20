package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Region;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class WalkingDebug extends BackgroundScript implements PaintHandler {
    public static ArrayList<Tile> clickedTiles = new ArrayList();

    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        if (Region.getFocusedX() != -1) {
            Debug.log(Region.getFocusedX() + ", " + Region.getFocusedY());
        }

        g.setColor(new Color(0, 255, 0, 150));
        Iterator var2 = clickedTiles.iterator();

        while (var2.hasNext()) {
            Tile tile = (Tile) var2.next();
            Point p = tile.getMinimapPoint();
            g.drawOval(p.x - 1, p.y - 1, 2, 2);
        }

    }
}
