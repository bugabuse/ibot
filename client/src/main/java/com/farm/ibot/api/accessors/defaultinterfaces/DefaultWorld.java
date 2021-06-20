package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IWorld;

public class DefaultWorld implements IWorld {
    public int getId(Object instance) {
        return (Integer) Wrapper.get("World.id", instance);
    }

    public int getType(Object instance) {
        return (Integer) Wrapper.get("World.type", instance);
    }

    public int getPlayerCount(Object instance) {
        return (Integer) Wrapper.get("World.playerCount", instance);
    }
}
