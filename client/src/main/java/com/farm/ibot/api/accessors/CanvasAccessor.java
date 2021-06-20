package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.ICanvasAccessor;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

import java.awt.*;

public class CanvasAccessor extends Wrapper {
    public CanvasAccessor(Object instance) {
        super(instance);
    }

    public static ICanvasAccessor getInterface() {
        return Bot.get().accessorInterface.canvasAccessprInterface;
    }

    @HookName("Client.canvasInstance")
    public static CanvasAccessor getInstance() {
        return getInterface().getInstance(null);
    }

    @HookName("Client.Canvas")
    public static void setCanvas(Canvas canvas) {
        getInterface().setCanvas(null, canvas);
    }
}
