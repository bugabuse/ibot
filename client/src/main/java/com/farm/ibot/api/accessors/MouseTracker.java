package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IMouseTracker;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class MouseTracker extends Wrapper {
    public MouseTracker(Object instance) {
        super(instance);
    }

    public static IMouseTracker getInterface() {
        return Bot.get().accessorInterface.mouseTrackerReflectionInterface;
    }

    @HookName("Client.mouseTracker")
    public static MouseTracker getMouseTracker() {
        return getInterface().get();
    }

    @HookName("MouseTracker.xCoordinates")
    public int[] getXCoordinates() {
        return getInterface().getXCoordinates(this.instance);
    }

    @HookName("MouseTracker.yCoordinates")
    public int[] getYCoordinates() {
        return getInterface().getYCoordinates(this.instance);
    }

    @HookName("MouseTracker.times")
    public long[] getTimes() {
        return getInterface().getTimes(this.instance);
    }

    @HookName("MouseTracker.currentIndex")
    public int getCurrentIndex() {
        return getInterface().getCurrentIndex(this.instance);
    }
}
