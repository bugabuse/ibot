package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.util.PaintUtils;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class BotPatternDebug extends BackgroundScript implements PaintHandler {
    public static ArrayList<Tile> tileClickPoints = new ArrayList();

    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        g.setColor(new Color(0, 255, 0, 160));
        Iterator var2 = (new ArrayList(tileClickPoints)).iterator();

        while (var2.hasNext()) {
            Tile t = (Tile) var2.next();
            Point p = t.getMinimapPoint();
            g.fillOval(p.x - 1, p.y - 1, 3, 3);
        }

        ArrayList<Tile> alreadyDrawn = new ArrayList();
        Iterator var6 = (new ArrayList(tileClickPoints)).iterator();

        while (var6.hasNext()) {
            Tile t = (Tile) var6.next();
            if (!alreadyDrawn.contains(t)) {
                PaintUtils.drawTile(g, t, "" + this.getTileCount(t));
                alreadyDrawn.add(t);
            }
        }

        g.setColor(Color.white);
    }

    private int getTileCount(Tile t) {
        return (int) tileClickPoints.stream().filter((tile) -> {
            return tile.equals(t);
        }).count();
    }
}
