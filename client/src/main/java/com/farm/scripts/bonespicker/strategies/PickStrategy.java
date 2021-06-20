package com.farm.scripts.bonespicker.strategies;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.bonespicker.Strategies;

public class PickStrategy extends Strategy {
    public boolean active() {
        return !Inventory.isFull();
    }

    protected void onAction() {
        Walking.setRun(true);
        if (!Strategies.COW_SPOT.area.contains(Player.getLocal().getPosition())) {
            WebWalking.walkTo(Strategies.COW_SPOT.tile, new Tile[0]);
        } else {
            GroundItem item = GroundItems.get((i) -> {
                return Strategies.COW_SPOT.area.contains(i.getPosition()) && i.getDefinition().name.contains("Cowhide");
            });
            if (item == null) {
                WebWalking.walkTo(Strategies.COW_SPOT.tile, new Tile[0]);
            } else if (item.getPosition().distance() <= 5 && item.getPosition().isReachable()) {
                if (item.interact("Take")) {
                    Time.sleepAny(5000, new Condition[]{Time.INVENTORY_CHANGED, () -> {
                        return !GroundItems.getAt(item.getPosition()).contains(item);
                    }});
                }

            } else {
                WebWalking.walkTo(item.getPosition(), 0, new Tile[0]);
            }
        }
    }
}
