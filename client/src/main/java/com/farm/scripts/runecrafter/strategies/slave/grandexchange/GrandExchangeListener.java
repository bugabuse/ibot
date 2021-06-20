package com.farm.scripts.runecrafter.strategies.slave.grandexchange;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;

public class GrandExchangeListener extends Strategy {
    public static boolean hasTiara() {
        return Inventory.contains(1438);
    }

    public boolean active() {
        return true;
    }

    public void onAction() {
        if (Time.sleep(2500, GrandExchangeListener::hasTiara)) {
            if (Bank.isOpen() && !Time.sleep(2500, () -> {
                return Bank.getContainer().contains(7936, 20) || Inventory.container().contains(7936, 20);
            })) {
            }

        }
    }
}
