package com.farm.ibot.api.world.area;

import com.farm.ibot.api.wrapper.Tile;

public class RadiusArea extends Area {
    public int radius;
    public Tile centerTile;

    public RadiusArea(Tile centerTile, int radius) {
        this.radius = radius;
        this.centerTile = centerTile;
    }

    public boolean contains(Tile tile) {
        return this.centerTile.distance(tile) <= this.radius;
    }

    public Tile centerTile() {
        return this.centerTile;
    }
}
