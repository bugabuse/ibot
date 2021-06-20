package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.world.WorldData;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class TileReach {
    private static HashMap<Bot, TileReach> map = new HashMap();
    private int[][] tiles = new int[105][105];
    private int[][] flags = new int[105][105];
    private long lastCache = -1L;
    private int lastHashCode = -1;

    public static boolean isReachable(Tile start, Tile end) {
        TileReach tr = (TileReach) map.get(Bot.get());
        if (tr == null) {
            tr = new TileReach();
            map.put(Bot.get(), tr);
        }

        return tr.isTileReachable(start, end);
    }

    public static TileReach get() {
        return (TileReach) map.get(Bot.get());
    }

    public int getTileFlag(Tile tile) {
        tile = tile.toLocalTile();
        return tile.getX() <= 103 && tile.getY() <= 103 ? this.flags[tile.getX()][tile.getY()] : -1;
    }

    public boolean isTileReachable(Tile start, Tile end) {
        if (System.currentTimeMillis() - this.lastCache > 1800L) {
            this.cacheRegionFlags();
            this.lastCache = System.currentTimeMillis();
        }

        start = start.toLocalTile();
        end = end.toLocalTile();
        if (start.getX() <= 103 && start.getY() <= 103 && end.getX() <= 103 && end.getY() <= 103) {
            return this.tiles[start.getX()][start.getY()] != 0 && this.tiles[start.getX()][start.getY()] == this.tiles[end.getX()][end.getY()];
        } else {
            return false;
        }
    }

    public void cacheRegionFlags() {
        this.flags = WorldData.getCollisionFlags(Client.getPlane());
        int currentHash = Arrays.deepHashCode(this.flags);
        if (this.lastHashCode != currentHash) {
            this.lastHashCode = currentHash;
            this.tiles = new int[105][105];
            int regionId = 2;

            for (int x = 0; x < 104; ++x) {
                for (int y = 0; y < 104; ++y) {
                    if (this.tiles[x][y] == 0) {
                        this.cacheRegionFlags(regionId, new Tile(x, y));
                        ++regionId;
                    }
                }
            }

        }
    }

    private void cacheRegionFlags(int regionId, Tile tile) {
        boolean[][] visited = new boolean[105][105];
        Queue<Tile> queue = new LinkedList();
        queue.add(tile);

        while (!queue.isEmpty()) {
            Tile t = (Tile) queue.remove();
            if (this.canFill(visited, regionId, t, t.add(0, -1))) {
                queue.add(t.add(0, -1));
            }

            if (this.canFill(visited, regionId, t, t.add(0, 1))) {
                queue.add(t.add(0, 1));
            }

            if (this.canFill(visited, regionId, t, t.add(-1, 0))) {
                queue.add(t.add(-1, 0));
            }

            if (this.canFill(visited, regionId, t, t.add(1, 0))) {
                queue.add(t.add(1, 0));
            }
        }

    }

    boolean canFill(boolean[][] visited, int regionId, Tile start, Tile next) {
        if (next.getX() >= 0 && next.getY() >= 0 && next.getX() <= 104 && next.getY() <= 104) {
            if (visited[next.getX()][next.getY()]) {
                return false;
            } else if (!WorldData.isWalkableLocal(start, next, this.flags)) {
                return false;
            } else {
                visited[next.getX()][next.getY()] = true;
                this.tiles[next.getX()][next.getY()] = regionId;
                return true;
            }
        } else {
            return false;
        }
    }
}
