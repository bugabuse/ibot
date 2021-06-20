package com.farm.scripts.claysoftener.strategies.grandexchange;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.claysoftener.Constants;

public class GrandExchangeStrategy extends Strategy {
    public static int sellClayFor = 204;
    public static int buyClayFor = 165;
    public static int maxToSpend = 70000;

    public boolean active() {
        return Constants.currentState == 2;
    }

    public void onAction() {

        Walking.setRun(true);
        if (GrandExchange.open()) {
            if (GrandExchange.canCollect() && Inventory.isFull()) {
                if (Bank.openNearest()) {
                    Bank.depositAll();
                }

            } else if (GrandExchange.collect()) {
                if (!Constants.needBuckets) {
                    if (Inventory.container().getCount(new int[]{434, 435}) > 20) {
                        Constants.currentState = 0;
                    } else {
                        Item softClay = new Item(1761, Inventory.container().getCount(new String[]{"Soft clay"}));
                        GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(434, MathUtils.clamp(Inventory.container().getCount(new int[]{995}), maxToSpend) / buyClayFor), buyClayFor, false);
                        GrandExchangeOffer sellOffer = new GrandExchangeOffer(softClay, sellClayFor, true);
                        if (!sellOffer.exists() || sellOffer.create()) {
                            if (!buyOffer.exists() || buyOffer.create()) {
                                if (Inventory.container().getCount(new int[]{1761, 1762}) > 800) {
                                    Constants.currentState = 0;
                                } else if (sellOffer.exists() && sellOffer.findGrandExchangeItem().getAmount() > 800) {
                                    sellOffer.abort();
                                } else {
                                    if (Inventory.container().getCount(new int[]{1761, 1762}) > 20) {
                                        if (sellOffer.exists()) {
                                            sellOffer.abort();
                                        }

                                        sellOffer.create();
                                    } else if (Inventory.container().getCount(new int[]{995}) > 1000) {
                                        buyOffer.create();
                                    } else if (!sellOffer.exists() && !buyOffer.exists()) {
                                        Constants.currentState = 0;
                                    }

                                }
                            }
                        }
                    }
                } else {
                    if (Inventory.container().getCount(new int[]{1925, 1926}) < 14 && Inventory.container().getCount(new int[]{995}) >= 2000) {
                        GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(1925, 14), 150, false);
                        buyOffer.create();
                    } else {
                        Constants.currentState = 0;
                        Constants.needBuckets = false;
                    }

                }
            }
        }
    }
}
