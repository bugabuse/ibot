package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Renderable;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IGameObject;

public class DefaultGameObject implements IGameObject {
    public long getUid(Object instance) {
        return (Long) Wrapper.get("GameObject.Uid", instance);
    }

    public int getAnimableX(Object instance) {
        return (Integer) Wrapper.get("GameObject.AnimableX", instance);
    }

    public int getAnimableY(Object instance) {
        return (Integer) Wrapper.get("GameObject.AnimableY", instance);
    }

    public int getRealId(Object instance) {
        return (Integer) Wrapper.get("GameObject.Id", instance);
    }

    public Renderable getRenderable(Object instance) {
        return (Renderable) Wrapper.get("GameObject.Renderable", Renderable.class, instance);
    }

    public int getOrientation(Object instance) {
        return (Integer) Wrapper.get("GameObject.Orientation", instance);
    }

    public void setRenderable(Object instance, Object value) {
        Wrapper.set("GameObject.Renderable", instance, value);
    }
}
