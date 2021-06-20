package com.farm.scripts.prayer.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;

public class BuryStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        if (Inventory.container().contains("Bones")) {
            if (!Bank.isOpen() || Widgets.closeTopInterface()) {
                Inventory.get("Bones").interact("Bury");
                if (Time.waitInventoryChange()) {
                    Time.sleep(650, 850);
                }

            }
        }
    }
}
