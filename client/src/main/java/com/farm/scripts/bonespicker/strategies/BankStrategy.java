package com.farm.scripts.bonespicker.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.core.script.Strategy;

public class BankStrategy extends Strategy {
    public boolean active() {
        return Inventory.isFull();
    }

    protected void onAction() {
        if (Bank.openNearest(Locations.BANK_LUMBRIDGE)) {
            Bank.depositAll();
        }

    }
}
