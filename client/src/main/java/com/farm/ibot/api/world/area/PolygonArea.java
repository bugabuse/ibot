package com.farm.ibot.api.world.area;

import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.Tile;

import java.awt.*;

public class PolygonArea extends Area {
    public Polygon polygon = new Polygon();

    public PolygonArea(Tile... tiles) {
        Tile[] var2 = tiles;
        int var3 = tiles.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Tile t = var2[var4];
            this.polygon.addPoint(t.getX(), t.getY());
        }

    }

    public boolean contains(Tile tile) {
        return this.polygon.contains(tile.getX(), tile.getY());
    }

    public Tile centerTile() {
        Point center = Screen.centroid(this.polygon);
        return new Tile(center.x, center.y);
    }
}
