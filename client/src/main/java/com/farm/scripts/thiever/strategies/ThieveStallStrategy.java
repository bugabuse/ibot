package com.farm.scripts.thiever.strategies;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;

public class ThieveStallStrategy extends Strategy {
    public static final Tile STALL_TILE = new Tile(3268, 3410);

    public boolean active() {
        return Skill.THIEVING.getRealLevel() >= 5;
    }

    protected void onAction() {
        if (Skill.HITPOINTS.getCurrentLevel() > 5) {
            if (WebWalking.walkTo(STALL_TILE, 1, new Tile[0])) {
                GameObject stall = GameObjects.get("Tea stall");
                if (stall != null) {

                    stall.interact("Steal-from");
                    Time.sleep(1000, 3000);
                }

            }
        }
    }
}
