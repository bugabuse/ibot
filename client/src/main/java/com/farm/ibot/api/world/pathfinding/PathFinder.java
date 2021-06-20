package com.farm.ibot.api.world.pathfinding;

import com.farm.ibot.api.wrapper.Tile;

public interface PathFinder {
    Tile[] findPath(Tile var1, Tile var2);
}
