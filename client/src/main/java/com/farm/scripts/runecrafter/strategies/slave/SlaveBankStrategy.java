package com.farm.scripts.runecrafter.strategies.slave;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Trade;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.runecrafter.Constants;

public class SlaveBankStrategy extends Strategy {
    public boolean active() {
        return !Trade.isOpen() && Inventory.getCount(7936) < 20;
    }

    protected void onAction() {
        Debug.log("BANK HERE " + Locations.getClosestBank(Player.getLocal().getPosition()));
        if (Bank.openNearest(Constants.ALTAR_AIR_RUINS)) {
            Debug.log("dsfsdafsdaf " + Locations.getClosestBank(Player.getLocal().getPosition()));
            if (Inventory.getCount(995) <= 0 && Bank.getContainer().contains(995)) {
                Bank.withdraw(995, 1);
            }

            if (!Inventory.contains(1438) && Bank.getContainer().contains(1438)) {
                if (Inventory.container().hasSpace(new Item(1438, 1))) {
                    Bank.withdraw(1438, 1);
                } else {
                    Bank.depositAll();
                }

            } else {
                if (Bank.depositAllExcept(new int[]{7936, 995, 1438})) {
                    Bank.withdraw(7936, 27);
                }

            }
        }
    }
}
