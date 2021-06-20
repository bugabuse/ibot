package com.farm.scripts.winefiller;

import com.farm.ibot.api.wrapper.Tile;

public class WineSpot {
    public String fountainName;
    public Tile tile;

    public WineSpot fountain(String name) {
        this.fountainName = name;
        return this;
    }

    public WineSpot tile(Tile tile) {
        this.tile = tile;
        return this;
    }
}
