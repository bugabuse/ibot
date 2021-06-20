package com.farm.scripts.autogold.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.autogold.AutoGold;

public class StockChecker extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Client.isInGame()) {
            int stock = Inventory.getCount(995) / 1000000;
            if (AutoGold.currentStock != stock) {
                AutoGold.currentStock = stock;
                UpdateMuleStrategy.forceUpdate = true;
            }
        }

    }
}
