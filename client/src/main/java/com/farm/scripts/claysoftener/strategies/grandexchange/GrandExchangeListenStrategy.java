package com.farm.scripts.claysoftener.strategies.grandexchange;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.farm.ibot.api.wrapper.item.container.WithdrawContainer;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.claysoftener.Constants;

public class GrandExchangeListenStrategy extends Strategy {
    public boolean active() {
        return Constants.currentState == 0 || Constants.currentState == 1;
    }

    protected void onAction() {

        if (Constants.currentState != 1 || Bank.open()) {
            if (Bank.isOpen()) {
                WithdrawContainer inventory = new WithdrawContainer(Constants.PRICES, Inventory.getAll());
                WithdrawContainer all = (new WithdrawContainer(Bank.getItemsArray())).merge(inventory);
                if ((all.contains(434) || all.contains(435)) && all.getCount(new int[]{1925, 1929}) >= 14) {
                    Constants.currentState = 0;
                    Constants.needBuckets = false;
                } else {
                    if (!Time.sleep(() -> {
                        return this.getAllItems().contains(434) && this.getAllItems().getCount(new int[]{1925, 1929}) >= 14;
                    })) {
                        if (Inventory.isFull()) {
                            Bank.depositAll();
                            return;
                        }

                        Constants.currentState = 1;
                        boolean hasOffers = (new GrandExchangeOffer(new Item(1761, 1), 10, true)).exists() || (new GrandExchangeOffer(new Item(434, 1), 10, false)).exists();
                        int availableCoinsAmount = GrandExchangeStrategy.maxToSpend - inventory.calculateWealth();
                        if (availableCoinsAmount > 0 && Bank.getContainer().getCount(new int[]{995}) > 0) {
                            Bank.withdraw(995, availableCoinsAmount);
                        } else if (Bank.getContainer().contains(1761)) {
                            Bank.withdraw(true, 1761, 777777);
                        } else if (hasOffers || inventory.calculateWealth() >= 600) {
                            Constants.currentState = 2;
                            if (this.getAllItems().getCount(new int[]{1925, 1929}) < 14) {
                                Constants.needBuckets = true;
                            }
                        }
                    }

                }
            }
        }
    }

    public ItemContainer getAllItems() {
        WithdrawContainer inventory = new WithdrawContainer(Constants.PRICES, Inventory.getAll());
        WithdrawContainer all = (new WithdrawContainer(Bank.getItemsArray())).merge(inventory);
        return new ItemContainer(all.getItemsArray());
    }
}
