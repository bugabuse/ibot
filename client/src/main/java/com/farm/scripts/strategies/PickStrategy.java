package com.farm.scripts.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;

public class PickStrategy extends Strategy {
    public static final Tile POTATO_SPOT = new Tile(3148, 3284, 0);

    public boolean active() {
        return !Inventory.isFull();
    }

    protected void onAction() {
        Walking.setRun(true);
        GameObject ziemniak = GameObjects.get("Potato");
        if (WebWalking.walkTo(POTATO_SPOT, 15, new Tile[0])) {
            if (ziemniak.getPosition().distance() > 5) {
                WebWalking.walkTo(ziemniak.getPosition(), 5, new Tile[0]);
            } else {
                if (ziemniak.interact("Pick") && Time.sleep(4000, () -> {
                    return Player.getLocal().getAnimation() != -1;
                }) && Time.waitInventoryChange()) {
                }

            }
        }
    }
}
