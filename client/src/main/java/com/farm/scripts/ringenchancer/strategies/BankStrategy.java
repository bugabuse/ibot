package com.farm.scripts.ringenchancer.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.core.script.Strategy;

public class BankStrategy extends Strategy {
    public boolean active() {
        return !Inventory.contains(1637) || !Inventory.contains(564);
    }

    protected void onAction() {
        if (Bank.openNearest() && Bank.depositAllExcept(new int[]{564, 1637})) {
            if (Bank.getContainer().contains(1637)) {
                Bank.withdraw(1637, 27);
            }

            if (Bank.getContainer().contains(564)) {
                Bank.withdraw(564, 1000);
            }
        }

    }
}
