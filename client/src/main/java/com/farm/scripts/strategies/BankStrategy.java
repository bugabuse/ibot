package com.farm.scripts.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;

public class BankStrategy extends Strategy {
    public boolean active() {
        return Inventory.isFull();
    }

    protected void onAction() {
        Walking.setRun(true);
        if (WebWalking.walkTo(Locations.BANK_LUMBRIDGE, new Tile[0])) {
            if (Bank.open()) {
                Bank.depositAll();
            }

        }
    }
}
