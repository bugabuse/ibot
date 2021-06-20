package com.farm.ibot.api.world.pathfinding.impl;

import com.farm.ibot.api.world.WorldData;
import com.farm.ibot.api.wrapper.Tile;

import java.util.ArrayList;
import java.util.HashSet;

public class LocalGreedyPathFinder {
    int[][] flags;

    private double getDist(Tile t1, Tile t2) {
        return t1.distanceD(t2);
    }

    public ArrayList<Tile> findPath(Tile start, Tile end) {
        start = start.toLocalTile();
        end = end.toLocalTile();
        this.flags = WorldData.getCollisionFlags(start.getZ());
        ArrayList<Tile> visited = new ArrayList();
        HashSet<Tile> excluded = new HashSet();
        Tile current = start;
        ArrayList finalPath = new ArrayList();

        label50:
        while (true) {
            label41:
            do {
                while (current != null) {
                    Tile last = current;
                    current = this.getBestNeighbor(current, end, excluded);
                    if (current == null) {
                        excluded.add(last);
                        if (visited.size() > 0) {
                            current = (Tile) visited.get(visited.size() - 1);
                        }
                        continue label41;
                    }

                    Tile temp = this.getBestNeighbor(current, end, excluded);
                    if (temp != null && temp.equals(last)) {
                        excluded.add(last);
                        finalPath.add(current);
                        if (current.distance(end) == 0) {
                            return finalPath;
                        }
                    } else {
                        finalPath.add(current);
                        visited.add(current);
                        if (current.distance(end) == 0) {
                            return finalPath;
                        }
                    }
                }

                return finalPath;
            } while (this.getBestNeighbor(current, end, excluded) != null);

            for (int i = visited.size() - 1; i >= 0; --i) {
                if (!excluded.contains(visited.get(i))) {
                    current = (Tile) visited.get(i);
                    continue label50;
                }
            }

            return null;
        }
    }

    private Tile getBestNeighbor(Tile current, Tile next, HashSet<Tile> exluded) {
        if (current == null) {
            return null;
        } else {
            Tile best = null;
            double bestDistance = 2.147483647E9D;

            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    if (x != 0 || y != 0) {
                        Tile t = current.add(x, y);
                        if (!exluded.contains(t) && t.getX() > 0 && t.getY() > 0 && t.getX() <= 103 && t.getY() <= 103 && next.getX() > 0 && next.getY() > 0 && next.getX() <= 103 && next.getY() <= 103 && WorldData.isWalkableLocal(t.getX(), t.getY(), current.getX(), current.getY(), this.flags)) {
                            double dist = this.getDist(next, t);
                            if (dist < bestDistance) {
                                bestDistance = dist;
                                best = t;
                            }
                        }
                    }
                }
            }

            return best;
        }
    }
}
