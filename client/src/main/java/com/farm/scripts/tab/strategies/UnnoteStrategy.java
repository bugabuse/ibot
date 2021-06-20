package com.farm.scripts.tab.strategies;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.tab.Strategies;

public class UnnoteStrategy extends Strategy {
    private static long timer = 0L;

    public static boolean isAtHome() {
        boolean atHome = GameObjects.get("Portal") != null;
        if (atHome) {
            timer = System.currentTimeMillis();
        }

        return System.currentTimeMillis() - timer < 6000L && Strategies.TILE_PHIALS.distance() > 80;
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (!Inventory.contains(1761)) {
            if (Inventory.contains(1762)) {
                if (isAtHome()) {
                    GameObjects.get("Portal").interact("Enter");
                    Time.sleep(() -> {
                        return !isAtHome();
                    });
                } else if (!WebWalking.walkTo(Strategies.TILE_PHIALS, 5, new Tile[0])) {
                    Debug.log("Not walked. " + Strategies.TILE_PHIALS.distance());
                    Debug.log("Not walked. " + Strategies.TILE_PHIALS.isReachable());
                } else if (Dialogue.isInDialouge()) {
                    Dialogue.selectOptionThatContains("Exchange All:");
                } else {
                    Inventory.get(1762).interactWith(Npcs.get("Phials"));
                    Time.sleep(Dialogue::isInDialouge);
                }
            }
        }
    }
}
