package com.farm.scripts.farmer.api;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.world.pathfinding.impl.WebPathFinder;
import com.farm.ibot.api.wrapper.Tile;

public class FarmingPatch {
    private static FarmingPatch[] patches;

    static {
        patches = new FarmingPatch[]{new FarmingPatch("Falador", new Tile(3058, 3310, 0), Locations.TELEPORT_FALADOR), new FarmingPatch("Catherby", new Tile(2812, 3463, 0), Locations.TELEPORT_CAMELOT), new FarmingPatch("Ardougne", new Tile(2672, 3375, 0), Locations.TELEPORT_CAMELOT)};
    }

    private Tile tile;
    private String name;
    private Tile[] patchFromTeleport;

    public FarmingPatch(String name, Tile tile, Tile teleportTile) {
        this.tile = tile;
        this.name = name;
        this.patchFromTeleport = (Tile[]) (new WebPathFinder(false)).findPath(teleportTile, tile).toArray(new Tile[0]);
    }

    public static FarmingPatch current() {
        FarmingPatch[] var0 = patches;
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            FarmingPatch p = var0[var2];
            if (p.getTile().distance() < 10) {
                return p;
            }
        }

        return null;
    }

    public static FarmingPatch[] getPatches() {
        return patches;
    }

    public Tile getTile() {
        return this.tile;
    }

    public String getName() {
        return this.name;
    }

    public Tile[] getPatchFromTeleport() {
        return this.patchFromTeleport;
    }
}
