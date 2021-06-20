package com.farm.scripts.woodcutter.util;

import com.farm.ibot.api.world.area.Area;
import com.farm.ibot.api.wrapper.Tile;

public class TreeArea extends Area {
    private final Area area;
    public int treesCount = 0;

    public TreeArea(Area area) {
        this.area = area;
    }

    public TreeArea treesCount(int count) {
        this.treesCount = count;
        return this;
    }

    public boolean contains(Tile tile) {
        return this.area.contains(tile);
    }

    public Tile centerTile() {
        return this.area.centerTile();
    }

    public TreeArea setNote(String note) {
        this.note = note;
        return this;
    }
}
