package com.farm.ibot.api.world.area;

import com.farm.ibot.api.wrapper.Tile;

public abstract class Area {
    public String note = "";

    public abstract boolean contains(Tile var1);

    public abstract Tile centerTile();

    public String getNote() {
        return this.note;
    }

    public Area setNote(String note) {
        this.note = note;
        return this;
    }
}
