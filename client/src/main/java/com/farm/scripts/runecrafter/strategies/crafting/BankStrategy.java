package com.farm.scripts.runecrafter.strategies.crafting;

import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.runecrafter.Constants;

public class BankStrategy extends Strategy {
    public boolean active() {
        return Inventory.getCount(7936) <= 0 || !Equipment.isEquipped(5527);
    }

    protected void onAction() {
        System.out.println("bank");
        if (Bank.openNearest(Constants.ALTAR_AIR_RUINS)) {
            if (Bank.depositAllExcept(new int[]{7936, 5527})) {
                if (!Equipment.isEquipped(5527) && Bank.getContainer().contains(5527)) {
                    Bank.withdraw(5527, 1);
                    return;
                }

                Bank.withdraw(7936, 28);
            }

        }
    }
}
