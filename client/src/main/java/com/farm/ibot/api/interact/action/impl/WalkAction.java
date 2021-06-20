package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Menu;
import com.farm.ibot.api.accessors.Region;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.impl.debuggers.BotPatternDebug;

import java.awt.*;
import java.util.Arrays;

public class WalkAction extends Action {
    private Tile tile;

    public WalkAction(Tile tile, int arg1, int arg2, int arg3, int arg4, String sArg1, String sArg2, int arg5, int arg6) {
        super(arg1, arg2, arg3, arg4, sArg1, sArg2, arg5, arg6);
        this.tile = tile;
    }

    public static WalkAction create(Tile tile) {
        Point p = Random.next(new Rectangle(400, 5, 100, 100));
        p.translate(Random.next(-20, 20), Random.next(-20, 20));
        return new WalkAction(tile, 0, 0, 23, 0, "Walk here", "", 0, 0);
    }

    public void send() {
        if (Menu.forceOpen()) {
            Region.setFocusedX(this.tile.toLocalTile().getX());
            Region.setFocusedY(this.tile.toLocalTile().getY());
            Debug.log("Process menu(" + this.getClass().getSimpleName() + ") action: " + this.arg1 + "," + this.arg2 + "," + this.arg3 + "," + this.arg4 + "," + this.sArg1 + "," + this.sArg2 + "," + this.clickPointX + "," + this.clickPointY);
            this.clickPointX = Mouse.getLocation().x;
            this.clickPointY = Mouse.getLocation().y;
            Client.processMenuAction(this.arg1, this.arg2, this.arg3, this.arg4, this.sArg1, this.sArg2, this.clickPointX, this.clickPointY);
            Time.waitNextGameCycle();
        }

    }

    public void sendByMouse() {
        if (Menu.forceOpen()) {
            if (Arrays.stream(Menu.getNodes()).anyMatch((menuNode) -> {
                return menuNode.getAction().contains("Action");
            })) {
                Menu.close();
                return;
            }

            Debug.log("WALK " + this.tile.toString());
            Debug.log("WALK-LOCAL " + this.tile.toLocalTile().toString());
            this.sArg1 = "Perform";
            this.sArg2 = "<col=ff00ff>Action";
            Menu.addItem(0, 0, 23, 0, this.sArg1, this.sArg2);
            Point point = new Point(-1, -1);
            Time.sleep(2000, () -> {
                Point p = Screen.findPixel(new Color(255, 0, 255), Menu.getBounds());
                if (p != null) {
                    point.setLocation(p.x, p.y);
                    return true;
                } else {
                    return false;
                }
            });
            if (point.getX() != -1.0D && point.getY() != -1.0D) {
                Region.setFocusedX(this.tile.toLocalTile().getX());
                Region.setFocusedY(this.tile.toLocalTile().getY());
                Mouse.click(point.x, point.y);
            }
        }

        BotPatternDebug.tileClickPoints.add(this.tile);
    }
}
