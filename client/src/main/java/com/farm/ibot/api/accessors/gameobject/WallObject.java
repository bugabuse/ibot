package com.farm.ibot.api.accessors.gameobject;

import com.farm.ibot.api.accessors.Renderable;
import com.farm.ibot.api.accessors.interfaces.IWallObject;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class WallObject extends GameObject {
    public WallObject(Object instance) {
        super(instance);
    }

    public static IWallObject getWallObjectInterface() {
        return Bot.get().accessorInterface.wallObjectReflectionInterface;
    }

    @HookName("WallObject.Uid")
    public long getUid() {
        return getWallObjectInterface().getUid(this.instance);
    }

    @HookName("WallObject.AnimableX")
    public int getAnimableX() {
        return getWallObjectInterface().getAnimableX(this.instance);
    }

    @HookName("WallObject.AnimableY")
    public int getAnimableY() {
        return getWallObjectInterface().getAnimableY(this.instance);
    }

    @HookName("WallObject.Renderable")
    public Renderable getRenderable() {
        return getWallObjectInterface().getRenderable(this.instance);
    }

    @HookName("WallObject.Renderable")
    public void setRenderable(Object value) {
        getWallObjectInterface().setRenderable(this.instance, value);
    }

    @HookName("WallObject.Orientation")
    public int getOrientation() {
        return getWallObjectInterface().getOrientation(this.instance);
    }

    @HookName("WallObject.Id")
    public int getRealId() {
        return getWallObjectInterface().getRealId(this.instance);
    }
}
