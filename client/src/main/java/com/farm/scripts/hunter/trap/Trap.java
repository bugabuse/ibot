package com.farm.scripts.hunter.trap;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.wrapper.Tile;

import java.util.Iterator;

public class Trap {
    private final String[] objectNames;
    private final int groundItemId;

    public Trap(String objectName, int groundItemId) {
        this.objectNames = objectName.split("\\|");
        this.groundItemId = groundItemId;
    }

    public int getItemId() {
        return this.groundItemId;
    }

    private GameObject getTrap(Tile tile) {
        Iterator var2 = GameObjects.getAt(tile).iterator();

        GameObject object;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            object = (GameObject) var2.next();
        } while (!this.matchesName(object.getName()));

        return object;
    }

    public Trap.State getState(Tile tile) {
        GameObject object = this.getTrap(tile);
        if (object == null) {
            return Trap.State.EMPTY;
        } else {
            if (Player.getLocal().getAnimation() == -1) {
                Iterator var3 = GroundItems.getAt(tile).iterator();

                while (var3.hasNext()) {
                    GroundItem item = (GroundItem) var3.next();
                    if (item.getId() == this.groundItemId) {
                        return Trap.State.BROKEN_GROUND_ITEM;
                    }
                }
            }

            String[] actions = object.getActions();
            if (StringUtils.containsAny("Check", actions)) {
                return Trap.State.CATCHED;
            } else if (StringUtils.containsAny("Dismantle", actions)) {
                return StringUtils.containsAny("Investigate", actions) ? Trap.State.IDLE : Trap.State.BROKEN;
            } else {
                return Trap.State.IDLE;
            }
        }
    }

    private boolean matchesName(String name) {
        String[] var2 = this.objectNames;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String str = var2[var4];
            if (name.equalsIgnoreCase(str)) {
                return true;
            }
        }

        return false;
    }

    public static enum State {
        EMPTY,
        IDLE,
        BROKEN,
        CATCHED,
        BROKEN_GROUND_ITEM;

        public String toString() {
            return this.name().charAt(0) + this.name().toLowerCase().substring(1);
        }
    }
}
