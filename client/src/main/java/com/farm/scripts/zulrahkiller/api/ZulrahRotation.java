package com.farm.scripts.zulrahkiller.api;

import com.farm.ibot.api.wrapper.Tile;

public class ZulrahRotation {
    private Tile zulrahTile;
    private Tile localWalkTile;
    private Zulrah.Color color;
    private boolean rangedFirst = false;
    private boolean jadMode = false;
    private int allowedDistance = 0;
    private Tile alternateTile;
    private boolean idleMode;
    private int waitBeforeWalkingTime;

    public ZulrahRotation(Zulrah.Color color, Tile walkTile) {
        this.localWalkTile = walkTile;
        this.color = color;
    }

    public ZulrahRotation(Zulrah.Color color, Tile walkTile, Tile zulrahTile) {
        this.localWalkTile = walkTile;
        this.zulrahTile = zulrahTile;
        this.color = color;
    }

    public Tile getTile() {
        return this.localWalkTile.toWorldTile();
    }

    public Tile getZulrahTile() {
        return this.zulrahTile != null ? this.zulrahTile.toWorldTile() : null;
    }

    public Zulrah.Color getColor() {
        return this.color;
    }

    public ZulrahRotation withJadMode(boolean rangedFirst) {
        this.rangedFirst = rangedFirst;
        this.jadMode = true;
        return this;
    }

    public ZulrahRotation withAllowedDistance(int distance) {
        this.allowedDistance = distance;
        return this;
    }

    public ZulrahRotation withAlternate(Tile alternateTile) {
        this.alternateTile = alternateTile;
        return this;
    }

    public boolean isJadMode() {
        return this.jadMode;
    }

    public boolean isRangedFirst() {
        return this.rangedFirst;
    }

    public int getAllowedDistance() {
        return this.allowedDistance;
    }

    public int getWaitBeforeWalkingTime() {
        return this.waitBeforeWalkingTime;
    }

    public Tile getAlternateTile() {
        return this.alternateTile != null ? this.alternateTile.toWorldTile() : null;
    }

    public boolean equals(ZulrahRotation other) {
        if (other == null) {
            return false;
        } else if (other.getZulrahTile() != null && this.getZulrahTile() != null) {
            return other.getColor().equals(this.getColor()) && this.getZulrahTile().distance(other.getZulrahTile()) <= 3;
        } else {
            return other.getColor().equals(this.getColor());
        }
    }

    public boolean isIdleMode() {
        return this.idleMode;
    }

    public ZulrahRotation setIdleMode(boolean idleMode) {
        this.idleMode = idleMode;
        return this;
    }

    public ZulrahRotation waitBeforeWalk(int waitBeforeWalkingTime) {
        this.waitBeforeWalkingTime = waitBeforeWalkingTime;
        return this;
    }
}
