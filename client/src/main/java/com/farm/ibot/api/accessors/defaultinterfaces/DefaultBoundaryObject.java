package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Renderable;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IBoundaryObject;

public class DefaultBoundaryObject implements IBoundaryObject {
    public long getUid(Object instance) {
        return (Long) Wrapper.get("BoundaryObject.Uid", instance);
    }

    public int getAnimableX(Object instance) {
        return (Integer) Wrapper.get("BoundaryObject.AnimableX", instance);
    }

    public int getAnimableY(Object instance) {
        return (Integer) Wrapper.get("BoundaryObject.AnimableY", instance);
    }

    public int getRealId(Object instance) {
        return (Integer) Wrapper.get("BoundaryObject.Id", instance);
    }

    public Renderable getRenderable(Object instance) {
        return (Renderable) Wrapper.get("BoundaryObject.Renderable", Renderable.class, instance);
    }

    public int getOrientation(Object instance) {
        return (Integer) Wrapper.get("BoundaryObject.Orientation", instance);
    }

    public void setRenderable(Object instance, Object value) {
        Wrapper.set("BoundaryObject.Renderable", instance, value);
    }
}
