/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core;

import java.util.Random;

public class WorldHopping {
    public static final int[] F2P_WORLDS = new int[]{1, 8, 16, 26, 35, 82, 83, 84, 93, 94};
    public static final int[] P2P_WORLDS = new int[]{2, 3, 4, 5, 6, 7, 9, 10, 12, 13, 14, 15, 17, 18, 19, 21, 22, 23, 24, 27, 28, 29, 30, 31, 32, 33, 34, 36, 39, 40, 41, 42, 43, 44, 46, 47, 48, 50, 51, 52, 54, 55, 56, 58, 59, 60, 62, 67, 68, 69, 70, 75, 76, 77, 86};
    public static final int[] WORLDS = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 65, 66, 67, 68, 69, 70, 73, 74, 75, 76, 77, 78, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 117};

    public static int toRegularWorldNumber(int world) {
        if (world >= 300) {
            world -= 300;
        }
        return world;
    }

    public static int toExpandedWorldNumber(int world) {
        if (world < 300) {
            world += 300;
        }
        return world;
    }

    public static int getRandomF2p() {
        return F2P_WORLDS[new Random().nextInt(F2P_WORLDS.length)];
    }

    public static int getRandomP2p() {
        return P2P_WORLDS[new Random().nextInt(P2P_WORLDS.length)];
    }
}

