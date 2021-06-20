package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.ItemLayer;
import com.farm.ibot.api.accessors.gameobject.BoundaryObject;
import com.farm.ibot.api.accessors.gameobject.FloorObject;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.accessors.gameobject.WallObject;

public interface IRegionTile {
    GameObject[] getGameObjects(Object var1);

    WallObject getWallObject(Object var1);

    BoundaryObject getBoundaryObject(Object var1);

    FloorObject getFloorObject(Object var1);

    ItemLayer getItemLayer(Object var1);

    int getX(Object var1);

    int getY(Object var1);

    int getZ(Object var1);
}
