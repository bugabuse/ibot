package com.farm.scripts.plankpicker.strategies;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.methods.walking.WalkingUtils;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;

public class PickPlankStrategy extends Strategy {
    public static final Tile PLANKS_TILE = new Tile(2552, 3578, 0);

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (this.goToPlanks()) {
            GroundItem plank = GroundItems.get(960);
            if (plank != null) {
                plank.interact("Take");
                Time.waitInventoryChange();
            }

        }
    }

    private boolean goToPlanks() {
        if (WebWalking.canFindPath(PLANKS_TILE)) {
            return WebWalking.walkTo(PLANKS_TILE, 7, new Tile[0]);
        } else {
            if (WalkingUtils.getTeleportItem("Games necklace") != null) {
                WalkingUtils.teleportByNecklace("Barbarian Outpost", "Games necklace");
            }

            return false;
        }
    }
}
