package com.farm.ibot.api.world;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.wrapper.Tile;

public class WorldData {
    public static int[][] getCollisionFlags(int plane) {
        return Client.getCollisionMaps()[plane].getCollisionFlags();
    }

    public static byte[][][] getTileFlags() {
        return Client.getTileFlags();
    }

    public static int[][][] getTileHeights() {
        return Client.getTileHeights();
    }

    public static int getCollisionFlag(Tile tile) {
        tile = tile.toLocalTile();
        return getCollisionFlags(tile.getZ())[tile.getX()][tile.getY()];
    }

    public static boolean isWalkable(Tile next, Tile position) {
        next = next.toLocalTile();
        position = position.toLocalTile();
        return isWalkableLocal(next, position);
    }

    public static boolean isWalkableLocal(Tile next, Tile position) {
        return isWalkableLocal(next, position, getCollisionFlags(position.getZ()));
    }

    public static boolean isWalkableLocal(Tile next, Tile position, int[][] flags) {
        return isWalkableLocal(next.getX(), next.getY(), position.getX(), position.getY(), flags);
    }

    public static boolean isWalkableLocal(int destX, int destY, int currentX, int currentY, int[][] flags) {
        int deltaX = getDiff(currentX, destX);
        int deltaY = getDiff(currentY, destY);
        if (currentX < flags.length && currentY < flags.length) {
            int here = flags[currentX][currentY];
            if (deltaX == 0) {
                if (deltaY == 0) {
                    return true;
                }

                if (deltaY == -1 && (here & 32) == 0 && (flags[currentX][currentY - 1] & 19398912) == 0) {
                    return true;
                }

                if (deltaY == 1 && (here & 2) == 0 && (flags[currentX][currentY + 1] & 19398912) == 0) {
                    return true;
                }
            } else if (deltaX == -1) {
                if (deltaY == 0 && (here & 128) == 0 && (flags[currentX - 1][currentY] & 19398912) == 0) {
                    return true;
                }

                if (deltaY == -1 && (here & 224) == 0 && (flags[currentX - 1][currentY - 1] & 19398912) == 0 && (flags[currentX][currentY - 1] & 19399040) == 0 && (flags[currentX - 1][currentY] & 19398944) == 0) {
                    return true;
                }

                if (deltaY == 1 && (here & 131) == 0 && (flags[currentX - 1][currentY + 1] & 19398912) == 0 && (flags[currentX][currentY + 1] & 19399040) == 0 && (flags[currentX - 1][currentY] & 19398914) == 0) {
                    return true;
                }
            } else {
                if (deltaY == 0 && (here & 8) == 0 && (flags[currentX + 1][currentY] & 19398912) == 0) {
                    return true;
                }

                if (deltaY == -1 && (here & 56) == 0 && (flags[currentX + 1][currentY - 1] & 19398912) == 0 && (flags[currentX][currentY - 1] & 19398920) == 0 && (flags[currentX + 1][currentY] & 19398944) == 0) {
                    return true;
                }

                if (deltaY == 1 && (here & 14) == 0 && (flags[currentX + 1][currentY + 1] & 19398912) == 0 && (flags[currentX][currentY + 1] & 19398920) == 0 && (flags[currentX + 1][currentY] & 19398914) == 0) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private static int getDiff(int num2, int num1) {
        if (num1 > num2) {
            return 1;
        } else {
            return num2 > num1 ? -1 : 0;
        }
    }

    public interface Flag {
        int WALL_NORTHWEST = 1;
        int WALL_NORTH = 2;
        int WALL_NORTHEAST = 4;
        int WALL_EAST = 8;
        int WALL_SOUTHEAST = 16;
        int WALL_SOUTH = 32;
        int WALL_SOUTHWEST = 64;
        int WALL_WEST = 128;
        int OBJECT_TILE = 256;
        int WALL_BLOCK_NORTHWEST = 512;
        int WALL_BLOCK_NORTH = 1024;
        int WALL_BLOCK_NORTHEAST = 2048;
        int WALL_BLOCK_EAST = 4096;
        int WALL_BLOCK_SOUTHEAST = 8192;
        int WALL_BLOCK_SOUTH = 16384;
        int WALL_BLOCK_SOUTHWEST = 32768;
        int WALL_BLOCK_WEST = 65536;
        int OBJECT_BLOCK = 131072;
        int DECORATION_BLOCK = 262144;
        int WALL_ALLOW_RANGE_NORTHWEST = 4194304;
        int WALL_ALLOW_RANGE_NORTH = 8388608;
        int WALL_ALLOW_RANGE_NORTHEAST = 16777216;
        int WALL_ALLOW_RANGE_EAST = 33554432;
        int WALL_ALLOW_RANGE_SOUTHEAST = 67108864;
        int WALL_ALLOW_RANGE_SOUTH = 134217728;
        int WALL_ALLOW_RANGE_SOUTHWEST = 268435456;
        int WALL_ALLOW_RANGE_WEST = 536870912;
        int OBJECT_ALLOW_RANGE = 1073741824;
        int BLOCKED = 19398912;
    }
}
