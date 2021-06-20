package com.farm.scripts.miner.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.wrapper.item.container.WithdrawContainer;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.miner.MiningUtils;
import com.farm.scripts.miner.Strategies;
import com.google.common.primitives.Ints;

public class BankStrategy extends Strategy {
    public boolean active() {
        return Inventory.isFull() || !MiningUtils.hasPickaxe() || (new WithdrawContainer(Strategies.PRICES, Inventory.getAll())).calculateWealth() > 6500;
    }

    public void onAction() {
        if (Bank.openNearest()) {
            String axe = MiningUtils.getBestPickaxe();
            if (Bank.depositAllExcept((i) -> {
                return i.getName().contains(axe) && Ints.contains(MiningUtils.AXE_IDs, i.getId());
            }) && !Inventory.container().contains(axe) && Bank.getContainer().get(axe) != null) {
                Bank.withdraw(false, Bank.getContainer().get(axe).getId(), 1);
            }
        }

    }
}
