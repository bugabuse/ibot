package com.farm.scripts.flaxspinner.strategies.flax;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer.Status;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.flaxspinner.FlaxSpinner;
import com.farm.scripts.flaxspinner.Strategies;
import com.farm.scripts.flaxspinner.strategies.ge.GrandExchangeStrategy;

public class BankStrategy extends Strategy {
    protected void onAction() {
        boolean hasOffer = false;
        GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(1779, 4000), GrandExchangeStrategy.flaxPrice.intValue(), false);
        if (buyOffer.exists() && buyOffer.getCurrentPrice() == GrandExchangeStrategy.flaxPrice.intValue() && buyOffer.getStatus() != Status.COMPLETED) {
            hasOffer = true;
        }

        if (Locations.GRAND_EXCHANGE.distance() < 15) {
            if (!Bank.hasCache()) {
                Bank.openNearest(Locations.GRAND_EXCHANGE);
                return;
            }

            if (!hasOffer && Inventory.container().countNoted().getCount(new int[]{1779}) + Bank.getCache().getCount(new int[]{1779}) < 3900) {
                FlaxSpinner.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE_FLAXING);
                return;
            }
        }

        if (!Inventory.contains(1779)) {
            Walking.setRun(true);
            if (Bank.openNearest() && Bank.depositAllExcept(new int[]{1779})) {
                if (Bank.getContainer().contains(1779)) {
                    Bank.withdraw(1779, 28);
                } else if (!Inventory.contains(1779)) {
                    FlaxSpinner.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE_FLAXING);
                }
            }

        }
    }
}
