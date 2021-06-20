package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Renderable;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IFloorObject;

public class DefaultFloorObject implements IFloorObject {
    public long getUid(Object instance) {
        return (Long) Wrapper.get("FloorObject.Uid", instance);
    }

    public int getAnimableX(Object instance) {
        return (Integer) Wrapper.get("FloorObject.AnimableX", instance);
    }

    public int getAnimableY(Object instance) {
        return (Integer) Wrapper.get("FloorObject.AnimableY", instance);
    }

    public int getRealId(Object instance) {
        return (Integer) Wrapper.get("FloorObject.Id", instance);
    }

    public Renderable getRenderable(Object instance) {
        return (Renderable) Wrapper.get("FloorObject.Renderable", Renderable.class, instance);
    }

    public void setRenderable(Object instance, Object value) {
        Wrapper.set("FloorObject.Renderable", instance, value);
    }
}
