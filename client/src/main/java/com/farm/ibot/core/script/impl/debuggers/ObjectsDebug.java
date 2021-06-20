package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ObjectsDebug extends BackgroundScript implements PaintHandler {
    ArrayList<GameObject> objects = null;

    public void onStart() {
    }

    public int onLoop() {
        this.objects = GameObjects.getAll((o) -> {
            return o.getPosition().distance() < 6 && o.isInteractable();
        });
        return 500;
    }

    public void onPaint(Graphics g) {
        if (this.objects != null) {
            Iterator var2 = this.objects.iterator();

            while (var2.hasNext()) {
                GameObject object = (GameObject) var2.next();
                g.setColor(Color.white);
                if (Screen.isOnGameScreen(object.getScreenPoint())) {
                    if (object.getRenderable() == null) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.white);
                    }

                    g.fillRect(object.getScreenPoint().x - 1, object.getScreenPoint().y - 1, 2, 2);
                    g.drawString(object.getName() + "(" + object.getId() + ") " + object.getUid() + " " + object.getOrientation(), object.getScreenPoint().x, object.getScreenPoint().y);
                }
            }
        }

    }
}
