package com.farm.scripts.hunter.trap;

import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.wrapper.Tile;

public class TrapTile extends Tile {
    private Trap trap;

    public TrapTile(Tile tile, Trap trap) {
        super(tile.getX(), tile.getY(), tile.getZ());
        this.trap = trap;
    }

    public TrapTile(int x, int y, Trap trap) {
        this(new Tile(x, y, 0), trap);
    }

    public Trap.State getState() {
        return this.trap.getState(this);
    }

    public int getItemId() {
        return this.trap.getItemId();
    }

    public Trap getTrap() {
        return this.trap;
    }

    public TrapTile derive(int x, int y) {
        return new TrapTile(new Tile(this.getX() + x, this.getY() + y), this.trap);
    }

    public int getPriority() {
        int priority = 0;
        if (this.getState() == Trap.State.EMPTY) {
            priority += 10;
        }

        priority += MathUtils.clamp(9 - this.animableDistance(), 0, 9);
        return priority;
    }
}
