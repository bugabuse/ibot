package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.MouseTracker;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IMouseTracker;

public class DefaultMouseTracker implements IMouseTracker {
    public int[] getXCoordinates(Object instance) {
        return (int[]) Wrapper.get("MouseTracker.xCoordinates", instance);
    }

    public int[] getYCoordinates(Object instance) {
        return (int[]) Wrapper.get("MouseTracker.yCoordinates", instance);
    }

    public long[] getTimes(Object instance) {
        return (long[]) Wrapper.get("MouseTracker.times", instance);
    }

    public int getCurrentIndex(Object instance) {
        return (Integer) Wrapper.get("MouseTracker.currentIndex", instance);
    }

    public MouseTracker get() {
        return (MouseTracker) Wrapper.getStatic("Client.mouseTracker", MouseTracker.class);
    }
}
