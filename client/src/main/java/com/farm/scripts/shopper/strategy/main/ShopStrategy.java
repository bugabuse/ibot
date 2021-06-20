package com.farm.scripts.shopper.strategy.main;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.shop.Shop;
import com.farm.ibot.api.methods.shop.ShopItem;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebConfig;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.WithdrawContainer;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.shopper.Shopper;
import com.farm.scripts.shopper.Strategies;

public class ShopStrategy extends Strategy {
    public static ShopItem[] itemsToBuy = new ShopItem[]{new ShopItem("Death rune")};
    public static WithdrawItem[] itemsToSellAtGe = new WithdrawItem[]{new WithdrawItem(560, 0, WebConfig.getInt("rune_death_price_sell"))};

    public boolean active() {
        return !Inventory.isFull();
    }

    public void onAction() {
        if (Inventory.getCount(995) < 1000) {
            WithdrawContainer items = new WithdrawContainer(Strategies.PRICES, Inventory.getAll());
            if (items.calculateWealth() > 2000) {
                Shopper.instance.setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
            } else if (WebWalking.walkTo(Locations.getClosestBank(), 5, new Tile[0])) {
                Bank.open();
            }

        } else if (WebWalking.walkTo(Shopper.SHOP_TILE, new Tile[0])) {
            if (Shop.open(Shopper.NPC_NAME)) {
                ShopItem[] var1 = itemsToBuy;
                int var2 = var1.length;

                for (int var3 = 0; var3 < var2; ++var3) {
                    ShopItem shopItem = var1[var3];
                    Item item = Shop.getContainer().get((i) -> {
                        return i.getName().contains(shopItem.name);
                    });
                    if (item != null && item.getAmount() > 0) {
                        Shop.buy(item, 10);
                        if (item.getAmount() > 10) {
                            Time.sleep(100, 250);
                        } else {
                            Time.waitInventoryChange(1000);
                        }

                        return;
                    }
                }

            }
        }
    }
}
