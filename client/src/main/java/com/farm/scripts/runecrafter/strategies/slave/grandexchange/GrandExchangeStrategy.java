package com.farm.scripts.runecrafter.strategies.slave.grandexchange;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.runecrafter.Runecrafter;
import com.farm.scripts.runecrafter.Strategies;

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
                GrandExchangeOffer buyOffer;
                if (!GrandExchangeListener.hasTiara()) {
                    if (Inventory.getCount(995) < 1000) {
                        if (Bank.open()) {
                            if (Bank.getContainer().getCount(new int[]{995}) < 1000) {
                                Strategies.muleManager.activateResupplyState();
                                return;
                            }

                            Bank.withdraw(995, 55000);
                        }

                    } else if (Inventory.getFreeSlots() < 5) {
                        Bank.depositAllExcept(new int[]{995});
                    } else {
                        buyOffer = new GrandExchangeOffer(new Item(5527, 1), 1000, false);
                        if (!buyOffer.exists()) {
                            buyOffer.create();
                        }

                    }
                } else {
                    buyOffer = new GrandExchangeOffer(new Item(7936, Inventory.getCount(995) / 16), 16, false);
                    if (!buyOffer.exists() || buyOffer.create()) {
                        ItemContainer items = new ItemContainer(new Item[][]{Bank.getCache().getItems(), Inventory.getAll()});
                        if (items.getCount(new int[]{7936, 7937}) <= 20) {
                            if (Inventory.getCount(995) < 20000 && Bank.open()) {
                                if (Inventory.getCount(995) + Bank.getContainer().getCount(new int[]{995}) < 20000) {
                                    Strategies.muleManager.activateResupplyState();
                                    return;
                                }

                                Bank.withdraw(995, 55000);
                            }

                            buyOffer.create();
                        } else if (Inventory.container().contains(7937, 100) || Bank.getCache().contains(7936)) {
                            Runecrafter.get().setCurrentlyExecutitng(Strategies.DEFAULT);
                        }
                    }
                }
            }
        }
    }
}
