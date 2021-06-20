package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IItemLayer;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class ItemLayer extends Wrapper {
    public ItemLayer(Object instance) {
        super(instance);
    }

    public static IItemLayer getItemLayerInterface() {
        return Bot.get().accessorInterface.itemLayerInterface;
    }

    @HookName("ItemLayer.x")
    public int getX() {
        return getItemLayerInterface().getX(this.instance);
    }

    @HookName("ItemLayer.y")
    public int getY() {
        return getItemLayerInterface().getY(this.instance);
    }

    @HookName("ItemLayer.top")
    public int getTop() {
        return getItemLayerInterface().getTop(this.instance);
    }

    @HookName("ItemLayer.plane")
    public int getPlane() {
        return getItemLayerInterface().getPlane(this.instance);
    }

    @HookName("ItemLayer.middle")
    public int getMiddle() {
        return getItemLayerInterface().getMiddle(this.instance);
    }

    @HookName("ItemLayer.height")
    public int getHeight() {
        return getItemLayerInterface().getHeight(this.instance);
    }

    @HookName("ItemLayer.flag")
    public int getFlag() {
        return getItemLayerInterface().getFlag(this.instance);
    }

    @HookName("ItemLayer.bottom")
    public int getBottom() {
        return getItemLayerInterface().getBottom(this.instance);
    }
}
