package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.CanvasAccessor;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.ICanvasAccessor;

public class DefaultCanvasAccessor implements ICanvasAccessor {
    public CanvasAccessor getInstance(Object instance) {
        return (CanvasAccessor) Wrapper.get("Client.canvasInstance", CanvasAccessor.class, instance);
    }

    public void setCanvas(Object instance, Object value) {
        Wrapper.set("Client.Canvas", instance, value);
    }
}
