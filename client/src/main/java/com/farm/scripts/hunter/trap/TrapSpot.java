package com.farm.scripts.hunter.trap;

import com.farm.ibot.api.wrapper.Tile;

public class TrapSpot {
    private TrapTile[] tiles;

    public TrapSpot(Tile... tiles) {
        this.tiles = new TrapTile[tiles.length];

        for (int i = 0; i < tiles.length; ++i) {
            if (tiles[i] instanceof TrapTile) {
                this.tiles[i] = (TrapTile) tiles[i];
            } else {
                this.tiles[i] = this.getRoot().derive(tiles[i].getX(), tiles[i].getY());
            }
        }

    }

    public TrapTile getRoot() {
        return this.tiles[0];
    }

    public TrapTile[] getTiles() {
        return this.tiles;
    }
}
