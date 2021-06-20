package com.farm.scripts.bonespicker.util;

import com.farm.ibot.api.world.area.Area;
import com.farm.ibot.api.world.area.RadiusArea;
import com.farm.ibot.api.wrapper.Tile;

public class CowSpot {
    public Tile tile;
    public Area area;

    public CowSpot(Tile tile, int radius) {
        this.tile = tile;
        this.area = new RadiusArea(tile, radius);
    }

    public CowSpot(Tile tile, Area area) {
        this.tile = tile;
        this.area = area;
    }
}
