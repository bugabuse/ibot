/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.content.trade;

import com.farm.server.core.game.Tile;

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

    public boolean equals(Object other) {
        return other instanceof TradeData && this.username.equalsIgnoreCase(((TradeData)other).username);
    }

    public void updateWith(TradeData account) {
        this.id = account.id;
        this.accountId = account.accountId;
        this.username = account.username;
        this.lastUpdate = account.lastUpdate;
        this.world = account.world;
        this.tile = account.tile;
        this.distanceToTile = account.distanceToTile;
    }
}

