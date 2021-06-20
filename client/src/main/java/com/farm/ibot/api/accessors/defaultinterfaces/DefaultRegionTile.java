package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.ItemLayer;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.gameobject.BoundaryObject;
import com.farm.ibot.api.accessors.gameobject.FloorObject;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.accessors.gameobject.WallObject;
import com.farm.ibot.api.accessors.interfaces.IRegionTile;

public class DefaultRegionTile implements IRegionTile {
    public GameObject[] getGameObjects(Object instance) {
        return (GameObject[]) Wrapper.get("RegionTile.GameObjects", GameObject[].class, instance);
    }

    public WallObject getWallObject(Object instance) {
        return (WallObject) Wrapper.get("RegionTile.WallObject", WallObject.class, instance);
    }

    public BoundaryObject getBoundaryObject(Object instance) {
        return (BoundaryObject) Wrapper.get("RegionTile.BoundaryObject", BoundaryObject.class, instance);
    }

    public FloorObject getFloorObject(Object instance) {
        return (FloorObject) Wrapper.get("RegionTile.FloorObject", FloorObject.class, instance);
    }

    public ItemLayer getItemLayer(Object instance) {
        return (ItemLayer) Wrapper.get("RegionTile.ItemLayer", ItemLayer.class, instance);
    }

    public int getX(Object instance) {
        return (Integer) Wrapper.get("RegionTile.X", instance);
    }

    public int getY(Object instance) {
        return (Integer) Wrapper.get("RegionTile.Y", instance);
    }

    public int getZ(Object instance) {
        return (Integer) Wrapper.get("RegionTile.Z", instance);
    }
}
