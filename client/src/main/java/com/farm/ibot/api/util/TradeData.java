package com.farm.ibot.api.util;

import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.init.AccountData;

public class TradeData {
    public int id;
    public int accountId;
    public String username;
    public String muleHandlerName;
    public long lastUpdate;
    public int world;
    public Tile tile;
    public boolean requireMuleHandlerAssigned;
    public int distanceToTile;
    public int tradeOnWorld;

    public TradeData(int id, int world, String username) {
        this.id = id;
        this.username = username;
        this.world = world;
        this.accountId = AccountData.current().getId();
    }

    public String toString() {
        return this.username;
    }

    public boolean equals(Object other) {
        return other instanceof TradeData && this.username.equalsIgnoreCase(((TradeData) other).username);
    }

    public Tile getTile() {
        return this.tile;
    }

    public void setTile(Tile t) {
        this.tile = t;
    }

    class WebTile {
        public int x;
        public int y;
        public int z;

        public WebTile() {
        }

        public WebTile(int x, int y) {
            this(x, y, 0);
        }

        public WebTile(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
