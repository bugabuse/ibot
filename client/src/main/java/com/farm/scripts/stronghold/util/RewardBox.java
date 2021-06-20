package com.farm.scripts.stronghold.util;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.wrapper.Tile;

public enum RewardBox {
    FIRST_FLOOR(1, new Tile(1905, 5222), "Gift of Peace"),
    SECOND_FLOOR(2, new Tile(2020, 5216), "Grain of Plenty"),
    THIRD_FLOOR(4, new Tile(2143, 5280), "Box of Health");

    public int configValue;
    public Tile tile;
    public String boxName;

    private RewardBox(int configValue, Tile tile, String boxName) {
        this.configValue = configValue;
        this.tile = tile;
        this.boxName = boxName;
    }

    public boolean isRedeemed() {
        return (Config.get(802) & this.configValue) == this.configValue;
    }
}
