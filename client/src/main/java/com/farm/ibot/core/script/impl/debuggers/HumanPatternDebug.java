package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class HumanPatternDebug extends BackgroundScript implements PaintHandler {
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
            g.drawRect(p.x, p.y, 1, 1);
        }

        g.setColor(Color.white);
    }
}
