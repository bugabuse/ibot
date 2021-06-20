package com.farm.scripts.woodcutter.strategy.ge;

import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer.Status;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.woodcutter.Chopper;
import com.farm.scripts.woodcutter.Strategies;

public class GrandExchangeStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        Walking.setRun(true);
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
                    System.out.println(axe + " needed: " + this.isNeeded(axe));
                    if (this.isNeeded(axe) && !Inventory.container().contains(axe) && !Bank.getCache().contains(axe)) {
                        int price = 10000;
                        GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(ItemDefinition.forName(axe).id, 1), MathUtils.clamp(price, 0, Inventory.getCount(995)), false);
                        if (Inventory.getFreeSlots() < 2) {
                            if (Bank.openNearest()) {
                                Bank.depositAllExcept(new int[]{995});
                            }

                            return;
                        }

                        if (buyOffer.exists()) {
                            if (buyOffer.getStatus() == Status.COMPLETED) {
                                buyOffer.abort();
                                return;
                            }

                            if (!buyOffer.create()) {
                                return;
                            }
                        }

                        ItemContainer items = new ItemContainer(new Item[][]{Bank.getCache().getItems(), Inventory.getAll()});
                        if (items.getCount(new String[]{axe}) <= 0) {
                            if (Inventory.getCount(995) < price && Bank.openNearest()) {
                                if (Bank.getContainer().contains(995)) {
                                    Bank.withdraw(995, 30000);
                                    return;
                                }

                                Strategies.muleManager.activateResupplyState();
                                return;
                            }

                            buyOffer.create();
                            return;
                        }
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

                Chopper.get().setCurrentlyExecutitng(Strategies.DEFAULT);
            }
        }
    }

    private boolean isNeeded(String axe) {
        return true;
    }
}
