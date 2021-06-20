package com.farm.scripts.plankpicker.strategies;

import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.walking.WalkingUtils;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.scriptutils.mule.strategies.WalkToSpotStrategy;

public class WalkToMuleSpot implements WalkToSpotStrategy {
    public boolean walkToSpot(Tile tile, int distance) {
        if (WebWalking.canFindPath(tile)) {
            return WebWalking.walkTo(tile, distance, new Tile[0]);
        } else if (WalkingUtils.getTeleportItem("Ring of wealth (") != null) {
            WalkingUtils.teleportByNecklace("Grand Exchange", "Ring of wealth (");
            return false;
        } else if (Bank.getCache().contains((i) -> {
            return i.getName().contains("Ring of wealth (");
        })) {
            if (BankStrategy.goToBank()) {
                Bank.openAndWithdraw(new Item[]{Bank.getCache().get((i) -> {
                    return i.getName().contains("Ring of wealth (");
                })});
            }

            return false;
        } else {
            Magic.LUMBRIDGE_HOME_TELEPORT.select();
            Time.sleep(15000);
            return false;
        }
    }
}
