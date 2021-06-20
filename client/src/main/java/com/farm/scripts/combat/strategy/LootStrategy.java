package com.farm.scripts.combat.strategy;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.combat.Settings;

public class LootStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (!Inventory.isFull()) {
            GroundItem lootItem = GroundItems.get((i) -> {
                return OsbuddyExchange.forId(i.getId()) != null && i.getAmount() * OsbuddyExchange.forId(i.getId()).getSellAverage() > 300 && Settings.getSpot().area.contains(i.getPosition()) && i.getPosition().isReachable();
            });
            if (lootItem != null) {
                ScriptUtils.interruptCurrentLoop();
                lootItem.interact("Take");
                Time.waitInventoryChange();
            }

        }
    }
}
