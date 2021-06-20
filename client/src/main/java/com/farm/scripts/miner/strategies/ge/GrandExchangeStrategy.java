package com.farm.scripts.miner.strategies.ge;

import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.miner.Miner;
import com.farm.scripts.miner.Strategies;

public class GrandExchangeStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        if (GrandExchange.open()) {
            if (GrandExchange.canCollect() && Inventory.isFull()) {
                if (Bank.open()) {
                    Bank.depositAllExcept(new int[]{995});
                }

            } else if (GrandExchange.collect()) {
                String[] var1 = GrandExchangeListener.REQUIRED_AXES;
                int var2 = var1.length;

                int var3;
                String axe;
                for (var3 = 0; var3 < var2; ++var3) {
                    axe = var1[var3];
                    if (this.isNeeded(axe) && !Inventory.container().contains(axe) && !Bank.getCache().contains(axe)) {
                        GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(ItemDefinition.forName(axe).id, 1), 10000, false);
                        if (buyOffer.exists() && !buyOffer.create()) {

                            return;
                        }

                        ItemContainer items = new ItemContainer(new Item[][]{Bank.getCache().getItems(), Inventory.getAll()});
                        if (items.getCount(new String[]{axe}) <= 0) {
                            if (Inventory.getCount(995) < 10000 && Bank.open()) {
                                if (Bank.getContainer().getCount(new int[]{995}) < 10000) {
                                    Miner.get().setCurrentlyExecutitng(Strategies.DEFAULT);
                                    return;
                                }

                                Bank.withdraw(995, 30000);
                            }

                            buyOffer.create();

                            return;
                        }
                    } else {

                    }
                }


                var1 = GrandExchangeListener.REQUIRED_AXES;
                var2 = var1.length;

                for (var3 = 0; var3 < var2; ++var3) {
                    axe = var1[var3];
                    if (this.isNeeded(axe) && !Inventory.container().contains(axe) && !Bank.getCache().contains(axe)) {

                        return;
                    }
                }

                Miner.get().setCurrentlyExecutitng(Strategies.DEFAULT);
            }
        }
    }

    private boolean isNeeded(String axe) {
        return true;
    }
}
