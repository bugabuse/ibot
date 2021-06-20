package com.farm.scripts.humanfletcher.api.action.wrapper;

import com.farm.ibot.api.wrapper.Tile;

public class GameObjectAction extends Action {
    public int objectId;
    private int tileX;
    private int tileY;

    public GameObjectAction(String menuOption, long time, int objectId, int tileX, int tileY) {
        super(menuOption, time);
        this.objectId = objectId;
        this.tileX = tileX;
        this.tileY = tileY;
    }

    public Tile getTile() {
        return new Tile(this.tileX, this.tileY);
    }
}
