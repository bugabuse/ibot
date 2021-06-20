package com.farm.ibot.api.accessors.gameobject;

import com.farm.ibot.api.accessors.Renderable;
import com.farm.ibot.api.accessors.interfaces.IBoundaryObject;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

import java.awt.*;

public class BoundaryObject extends GameObject {
    public BoundaryObject(Object instance) {
        super(instance);
    }

    public static IBoundaryObject getBoundaryObjectInterface() {
        return Bot.get().accessorInterface.boundaryObjectReflectionInterface;
    }

    @HookName("BoundaryObject.Uid")
    public long getUid() {
        return getBoundaryObjectInterface().getUid(this.instance);
    }

    @HookName("BoundaryObject.AnimableX")
    public int getAnimableX() {
        return getBoundaryObjectInterface().getAnimableX(this.instance);
    }

    @HookName("BoundaryObject.AnimableY")
    public int getAnimableY() {
        return getBoundaryObjectInterface().getAnimableY(this.instance);
    }

    @HookName("BoundaryObject.Renderable")
    public Renderable getRenderable() {
        return getBoundaryObjectInterface().getRenderable(this.instance);
    }

    @HookName("BoundaryObject.Renderable")
    public void setRenderable(Object value) {
        this.set("BoundaryObject.Renderable", value);
    }

    @HookName("BoundaryObject.Orientation")
    public int getOrientation() {
        return getBoundaryObjectInterface().getOrientation(this.instance);
    }

    @HookName("BoundaryObject.Id")
    public int getRealId() {
        return (Integer) this.get("BoundaryObject.Id");
    }

    public Point getScreenPoint() {
        int x = this.getAnimableX();
        int y = this.getAnimableY() + 1024;
        int orientation = this.getOrientation();
        if (orientation == 8) {
            y -= 86;
        } else if (orientation == 2) {
            y += 54;
        } else if (orientation == 4) {
            x += 54;
        } else if (orientation == 1) {
            x -= 54;
        }

        return Screen.worldToScreenNew(x, y, this.getModelHeight());
    }
}
