package com.farm.scripts.winefiller.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.winefiller.Strategies;
import com.farm.scripts.winefiller.WineFiller;

public class BankStrategy extends Strategy {
    public boolean active() {
        return WineFiller.currentState == WineFiller.NORMAL;
    }

    public void onAction() {
        if (!Inventory.contains(Strategies.JUG_EMPTY)) {
            if (Client.getRunEnergy() > 20) {
                Walking.setRun(true);
            }

            if (Bank.openNearest()) {
                if (Bank.getContainer().contains(Strategies.JUG_TO_WITHDRAW)) {
                    if (!Inventory.isEmpty()) {
                        Bank.depositAll();
                    }

                    if (Inventory.isEmpty()) {
                        Bank.withdraw(Strategies.JUG_TO_WITHDRAW, 28);
                    }
                } else {
                    if (Inventory.container().contains(Strategies.JUG_TO_WITHDRAW + 1)) {
                        Bank.depositAll();
                        return;
                    }

                    WineFiller.currentState = WineFiller.GRAND_EXCHANGE;
                }

            }
        }
    }
}
