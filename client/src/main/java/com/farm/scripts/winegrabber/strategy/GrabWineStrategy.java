package com.farm.scripts.winegrabber.strategy;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;

public class GrabWineStrategy extends Strategy {
    public static Tile ITEM_TILE = new Tile(2930, 3515, 0);
    public static Tile WALK_TILE = new Tile(2931, 3515, 0);
    public static PaintTimer idleTimer = new PaintTimer(0L);

    public boolean active() {
        GroundItem jug = (GroundItem) GroundItems.getAt(ITEM_TILE).get(0);
        if (WebWalking.walkTo(WALK_TILE, 0, new Tile[0])) {
            if (idleTimer.getElapsed() > 18000L) {
                Magic.TELEKINETIC_GRAB.select();
            }

            if (jug != null && Magic.TELEKINETIC_GRAB.select()) {
                jug.interact("Cast");
                if (Time.sleep(() -> {
                    return GroundItems.getAt(ITEM_TILE).get(0) == null;
                })) {
                    idleTimer.reset();
                }
            }
        }

        return true;
    }

    public void onAction() {
    }
}
