package com.farm.ibot.api.world.pathfinding.impl;

import com.farm.ibot.api.world.WorldData;
import com.farm.ibot.api.world.pathfinding.PathFinder;
import com.farm.ibot.api.wrapper.Tile;

public class ExtendedStraightPathFinder implements PathFinder {
    private int getMove(int place1, int place2) {
        if (place1 - place2 < 0) {
            return 1;
        } else {
            return place1 - place2 > 0 ? -1 : 0;
        }
    }

    public Tile[] findPath(Tile start, Tile end) {
        int stepX = this.getMove(end.getX(), start.getX());
        int stepY = this.getMove(end.getY(), start.getY());
        if (!WorldData.isWalkable(new Tile(end.getX() + stepX, end.getY()), end)) {
            stepX = 0;
        }

        if (!WorldData.isWalkable(new Tile(end.getX(), end.getY() + stepY), end)) {
            stepY = 0;
        }

        if (!WorldData.isWalkable(new Tile(end.getX() + stepX, end.getY() + stepY), end) && Math.abs(stepX) == Math.abs(stepY) && Math.abs(stepX) == 1) {
            if (WorldData.isWalkable(new Tile(end.getX() - stepX, end.getY()), end)) {
                stepX = -stepX;
                stepY = 0;
            } else if (WorldData.isWalkable(new Tile(end.getX(), end.getY() - stepY), end)) {
                stepY = -stepY;
                stepX = 0;
            }
        }

        return (new StraightPathFinder()).findPath(start, new Tile(end.getX() + stepX, end.getY() + stepY));
    }
}
