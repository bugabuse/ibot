package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.util.Random;

import java.awt.*;

public class ObjectAction extends Action {
    private static final int xClick = 0;
    private static final int yClick = 0;

    public ObjectAction(int objectUID, int objectRealId, int index) {
        super(getRegionX(objectUID), getRegionY(objectUID), index, objectRealId, "", "", 0, 0);
    }

    public static Action create(int actionIndex, GameObject object) {
        return create(3, actionIndex, object);
    }

    public static Action create(int opcode, int actionIndex, GameObject object) {
        if (object != null) {
            ObjectAction objectAction = new ObjectAction((int) object.getUid(), object.getIdUnsafe(), opcode + actionIndex);
            Point p = Random.human(object.getPosition().getBounds().getBounds());
            p.translate(Random.next(-30, 30), Random.next(-30, 30));
            objectAction.clickPointX = p.x;
            objectAction.clickPointY = p.y;
            objectAction.sArg1 = "";
            objectAction.sArg2 = "<col=ffff>" + object.getName();
            return objectAction;
        } else {
            return new Action("ObjectAction");
        }
    }

    public static Action create(String action, GameObject object) {
        return create(getIndex(object.getActions(), action), object);
    }

    public static int getRegionX(int x) {
        return x & 127;
    }

    public static int getRegionY(int y) {
        return y >> 7 & 127;
    }

    public static int getIndex(String[] actions, String action) {
        for (int i = 0; i < actions.length; ++i) {
            if (actions[i] != null && actions[i].equalsIgnoreCase(action)) {
                return i;
            }
        }

        return 0;
    }
}
