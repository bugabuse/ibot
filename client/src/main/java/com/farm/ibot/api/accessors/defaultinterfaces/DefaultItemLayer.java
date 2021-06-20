package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IItemLayer;

public class DefaultItemLayer implements IItemLayer {
    public int getX(Object instance) {
        return (Integer) Wrapper.get("ItemLayer.x", instance);
    }

    public int getY(Object instance) {
        return (Integer) Wrapper.get("ItemLayer.y", instance);
    }

    public int getTop(Object instance) {
        return (Integer) Wrapper.get("ItemLayer.top", instance);
    }

    public int getPlane(Object instance) {
        return (Integer) Wrapper.get("ItemLayer.plane", instance);
    }

    public int getMiddle(Object instance) {
        return (Integer) Wrapper.get("ItemLayer.middle", instance);
    }

    public int getHeight(Object instance) {
        return (Integer) Wrapper.get("ItemLayer.height", instance);
    }

    public int getFlag(Object instance) {
        return (Integer) Wrapper.get("ItemLayer.flag", instance);
    }

    public int getBottom(Object instance) {
        return (Integer) Wrapper.get("ItemLayer.bottom", instance);
    }
}
