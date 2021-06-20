package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IRenderable;

public class DefaultRenderable implements IRenderable {
    public int getModelHeight(Object instance) {
        return (Integer) Wrapper.get("Renderable.ModelHeight", instance);
    }
}
