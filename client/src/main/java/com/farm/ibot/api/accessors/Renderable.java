package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IRenderable;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Renderable extends Wrapper {
    public Renderable(Object instance) {
        super(instance);
    }

    public static IRenderable getRenderableInterface() {
        return Bot.get().accessorInterface.renderableInterface;
    }

    @HookName("Renderable.ModelHeight")
    public int getModelHeight() {
        return getRenderableInterface().getModelHeight(this.instance);
    }
}
