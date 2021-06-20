package com.farm.scripts.hunter.trap;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.world.Screen;

import java.awt.*;

public class TrapObject extends GameObject {
    private TrapObject(Object instance) {
        super(instance);
    }

    public static TrapObject create(GameObject obj) {
        return obj != null ? new TrapObject(obj.instance) : null;
    }

    public Point getScreenPoint() {
        return Screen.centroid(this.getPosition().getBounds());
    }
}
