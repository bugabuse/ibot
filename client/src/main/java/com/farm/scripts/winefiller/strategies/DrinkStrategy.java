package com.farm.scripts.winefiller.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.winefiller.Strategies;
import com.farm.scripts.winefiller.WineFiller;

public class DrinkStrategy extends Strategy {
    public boolean active() {
        return WineFiller.currentState == WineFiller.NORMAL && Inventory.get(Strategies.JUG_OF_WINE) != null;
    }

    public void onAction() {
        if (!Bank.isOpen()) {
            Item item = Inventory.get(Strategies.JUG_OF_WINE);
            if (item != null && item.interact("Drink")) {
                this.sleep(1650, 1750);
            }
        }

    }
}
