package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.ICollisionMap;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class CollisionMap extends Wrapper {
    public CollisionMap(Object instance) {
        super(instance);
    }

    public static ICollisionMap getCollisionMapInterface() {
        return Bot.get().accessorInterface.collisionMapReflectionInterface;
    }

    @HookName("CollisionMap.CollisionFlags")
    public int[][] getCollisionFlags() {
        return getCollisionMapInterface().getCollisionFlags(this.instance);
    }
}
