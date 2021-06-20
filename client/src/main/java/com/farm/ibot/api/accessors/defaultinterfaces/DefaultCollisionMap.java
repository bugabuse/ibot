package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.ICollisionMap;

public class DefaultCollisionMap implements ICollisionMap {
    public int[][] getCollisionFlags(Object instance) {
        return (int[][]) Wrapper.get("CollisionMap.CollisionFlags", instance);
    }
}
