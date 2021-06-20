package com.farm.scripts.runecrafter.strategies.crafting;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;

public class TiaraEquipStrategy extends Strategy {
    public boolean active() {
        return !Inventory.contains(1438);
    }

    protected void onAction() {
        System.out.println("Eq");
        if (!Inventory.contains(1438)) {
            if (Widgets.closeTopInterface()) {
                Inventory.get(5527).interact("Wear");
                Time.waitInventoryChange();
            }

        }
    }
}
