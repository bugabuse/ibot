package com.farm.scripts.prayer.strategies;

import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.core.script.Strategy;

public class BankStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        if (!Inventory.container().contains("Bones")) {
            if (Bank.openNearest()) {
                if (Bank.depositAll((i) -> {
                    return !i.getName().contains("Bones");
                })) {
                    Bank.withdraw(ItemDefinition.forName("Bones").getUnnotedId(), 28);
                }

            }
        }
    }
}
