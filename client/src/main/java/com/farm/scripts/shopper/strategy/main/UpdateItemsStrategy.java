package com.farm.scripts.shopper.strategy.main;

import com.farm.ibot.api.methods.shop.Shop;
import com.farm.ibot.api.methods.shop.ShopItem;
import com.farm.ibot.core.script.Strategy;

public class UpdateItemsStrategy extends Strategy {
    public boolean active() {
        return Shop.isOpen();
    }

    public void onAction() {
        ShopItem[] var1 = ShopStrategy.itemsToBuy;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ShopItem item = var1[var3];
            item.listen();
        }

    }

    public boolean isBackground() {
        return true;
    }
}
