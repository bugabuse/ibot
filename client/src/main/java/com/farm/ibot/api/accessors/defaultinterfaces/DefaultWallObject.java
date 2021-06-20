package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Renderable;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IWallObject;

public class DefaultWallObject implements IWallObject {
    public long getUid(Object instance) {
        return (Long) Wrapper.get("WallObject.Uid", instance);
    }

    public int getAnimableX(Object instance) {
        return (Integer) Wrapper.get("WallObject.AnimableX", instance);
    }

    public int getAnimableY(Object instance) {
        return (Integer) Wrapper.get("WallObject.AnimableY", instance);
    }

    public int getRealId(Object instance) {
        return (Integer) Wrapper.get("WallObject.Id", instance);
    }

    public Renderable getRenderable(Object instance) {
        return (Renderable) Wrapper.get("WallObject.Renderable", Renderable.class, instance);
    }

    public int getOrientation(Object instance) {
        return (Integer) Wrapper.get("WallObject.Orientation", instance);
    }

    public void setRenderable(Object instance, Object value) {
        Wrapper.set("WallObject.Renderable", instance, value);
    }
}
