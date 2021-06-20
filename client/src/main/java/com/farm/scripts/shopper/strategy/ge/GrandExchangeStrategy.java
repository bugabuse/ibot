package com.farm.scripts.shopper.strategy.ge;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.shopper.Shopper;
import com.farm.scripts.shopper.Strategies;
import com.farm.scripts.shopper.strategy.main.ShopStrategy;

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
                boolean hasOffers = false;
                WithdrawItem[] var2 = ShopStrategy.itemsToSellAtGe;
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    WithdrawItem withdrawItem = var2[var4];
                    Item airRunes = new Item(withdrawItem.getId(), Inventory.container().getCount(new int[]{withdrawItem.getId()}));
                    GrandExchangeOffer sellOffer = new GrandExchangeOffer(airRunes, withdrawItem.getUnitPrice(), true);
                    if (sellOffer.exists()) {
                        hasOffers = true;
                        if (!sellOffer.create()) {
                            return;
                        }
                    }

                    if (Inventory.container().getCount(new int[]{withdrawItem.getId()}) > 0) {
                        sellOffer.create();
                    }
                }

                if (Inventory.container().getCount(new int[]{995}) > 40000 || !hasOffers) {
                    Shopper.instance.setCurrentlyExecutitng(Strategies.DEFAULT);
                }

            }
        }
    }
}
