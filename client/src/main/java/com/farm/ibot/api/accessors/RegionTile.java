package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.gameobject.BoundaryObject;
import com.farm.ibot.api.accessors.gameobject.FloorObject;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.accessors.gameobject.WallObject;
import com.farm.ibot.api.accessors.interfaces.IRegionTile;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;

public class RegionTile extends Wrapper {
    public RegionTile(Object instance) {
        super(instance);
    }

    public static IRegionTile getRegionTileInterface() {
        return Bot.get().accessorInterface.regionTileInterface;
    }

    public static RegionTile get(Tile tile) {
        tile = tile.toLocalTile();
        return get(tile.getX(), tile.getY());
    }

    public static RegionTile get(int x, int y) {
        return get(x, y, Client.getPlane());
    }

    public static RegionTile get(int x, int y, int z) {
        Object[][][] tiles = Region.getRegion().getTilesObject();
        return tiles[z][x][y] != null ? new RegionTile(tiles[z][x][y]) : null;
    }

    @HookName("RegionTile.GameObjects")
    public GameObject[] getGameObjects() {
        return getRegionTileInterface().getGameObjects(this.instance);
    }

    @HookName("RegionTile.WallObject")
    public WallObject getWallObject() {
        return getRegionTileInterface().getWallObject(this.instance);
    }

    @HookName("RegionTile.BoundaryObject")
    public BoundaryObject getBoundaryObject() {
        return getRegionTileInterface().getBoundaryObject(this.instance);
    }

    @HookName("RegionTile.FloorObject")
    public FloorObject getFloorObject() {
        return getRegionTileInterface().getFloorObject(this.instance);
    }

    @HookName("RegionTile.ItemLayer")
    public ItemLayer getItemLayer() {
        return getRegionTileInterface().getItemLayer(this.instance);
    }

    @HookName("RegionTile.X")
    public int getX() {
        return getRegionTileInterface().getX(this.instance);
    }

    @HookName("RegionTile.Y")
    public int getY() {
        return getRegionTileInterface().getY(this.instance);
    }

    @HookName("RegionTile.Z")
    public int getZ() {
        return getRegionTileInterface().getZ(this.instance);
    }
}
