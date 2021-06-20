package com.farm.ibot.api.accessors.gameobject;

import com.farm.ibot.api.accessors.Renderable;
import com.farm.ibot.api.accessors.interfaces.IFloorObject;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class FloorObject extends GameObject {
    public FloorObject(Object instance) {
        super(instance);
    }

    public static IFloorObject getFloorObjectInterface() {
        return Bot.get().accessorInterface.floorObjectReflectionInterface;
    }

    @HookName("FloorObject.Uid")
    public long getUid() {
        return getFloorObjectInterface().getUid(this.instance);
    }

    @HookName("FloorObject.AnimableX")
    public int getAnimableX() {
        return getFloorObjectInterface().getAnimableX(this.instance);
    }

    @HookName("FloorObject.AnimableY")
    public int getAnimableY() {
        return getFloorObjectInterface().getAnimableY(this.instance);
    }

    @HookName("FloorObject.Renderable")
    public Renderable getRenderable() {
        return getFloorObjectInterface().getRenderable(this.instance);
    }

    @HookName("FloorObject.Renderable")
    public void setRenderable(Object value) {
        this.set("FloorObject.Renderable", value);
    }

    public int getOrientation() {
        return 0;
    }

    @HookName("FloorObject.Id")
    public int getRealId() {
        return (Integer) this.get("FloorObject.Id");
    }
}
