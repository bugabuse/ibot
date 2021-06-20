package com.farm.ibot.api.world.pathfinding.impl;

import com.farm.ibot.api.world.pathfinding.PathFinder;
import com.farm.ibot.api.wrapper.Tile;

import java.util.Deque;
import java.util.LinkedList;

public class StraightPathFinder implements PathFinder {
    public Tile[] findPath(Tile start, Tile end) {
        Deque<Tile> path = new LinkedList();
        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();
        int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
        path.add(start);

        for (int i = 0; i < max; ++i) {
            if (deltaX < 0) {
                ++deltaX;
            } else if (deltaX > 0) {
                --deltaX;
            }

            if (deltaY < 0) {
                ++deltaY;
            } else if (deltaY > 0) {
                --deltaY;
            }

            Tile position = new Tile(end.getX() - deltaX, end.getY() - deltaY);
            path.add(position);
        }

        return (Tile[]) path.toArray(new Tile[path.size()]);
    }
}
