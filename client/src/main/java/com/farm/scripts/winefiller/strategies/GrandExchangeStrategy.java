package com.farm.scripts.winefiller.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.winefiller.Strategies;
import com.farm.scripts.winefiller.WineFiller;

public class GrandExchangeStrategy extends Strategy {
    public static int sellWineFor = 40;
    public static int buyWineFor = 15;
    public static int maxToSpend = 40000;

    public boolean active() {
        return WineFiller.currentState == WineFiller.GRAND_EXCHANGE;
    }

    public void onAction() {
        if (GrandExchange.open()) {
            if (GrandExchange.canCollect() && Inventory.isFull()) {
                if (Bank.openNearest()) {
                    Bank.depositAll();
                }

            } else if (GrandExchange.collect()) {
                if (Inventory.container().getCount(new int[]{Strategies.JUG_TO_WITHDRAW, Strategies.JUG_TO_WITHDRAW + 1}) > 20) {
                    WineFiller.currentState = WineFiller.NORMAL;
                } else {
                    Item jugsOfWater = new Item(Strategies.JUG_OF_WATER, Inventory.container().getCount(new String[]{"Jug of water"}));
                    GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(Strategies.JUG_TO_WITHDRAW, MathUtils.clamp(Inventory.container().getCount(new int[]{995}), maxToSpend) / buyWineFor), buyWineFor, false);
                    GrandExchangeOffer sellOffer = new GrandExchangeOffer(jugsOfWater, sellWineFor, true);
                    GrandExchangeOffer oldBuyOffer = new GrandExchangeOffer(new Item(Strategies.JUG_OF_WINE, MathUtils.clamp(Inventory.container().getCount(new int[]{995}), maxToSpend) / buyWineFor), buyWineFor, false);
                    if (oldBuyOffer.exists()) {
                        oldBuyOffer.abort();
                    } else if (!sellOffer.exists() || sellOffer.create()) {
                        if (!buyOffer.exists() || buyOffer.create()) {
                            if (Inventory.container().getCount(new int[]{Strategies.JUG_OF_WATER, Strategies.JUG_OF_WATER + 1}) > 20) {
                                sellOffer.create();
                            } else if (Inventory.container().getCount(new int[]{995}) > 1000) {
                                buyOffer.create();
                            } else if (!sellOffer.exists() && !buyOffer.exists()) {
                                WineFiller.currentState = WineFiller.NORMAL;
                            }

                        }
                    }
                }
            }
        }
    }
}
