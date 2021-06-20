package com.farm.ibot.api.interfaces;

import com.farm.ibot.api.wrapper.Tile;

public interface Positionable {
    Tile getPosition();

    default int getX() {
        return this.getPosition().getX();
    }

    default int getY() {
        return this.getPosition().getY();
    }

    default int getZ() {
        return this.getPosition().getZ();
    }
}
