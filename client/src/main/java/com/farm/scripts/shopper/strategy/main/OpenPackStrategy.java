package com.farm.scripts.shopper.strategy.main;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.shop.Shop;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.Settings;

import java.util.ArrayList;
import java.util.Iterator;

public class OpenPackStrategy extends Strategy {
    public boolean active() {
        return (!Shop.isOpen() || Inventory.isFull()) && Inventory.container().get((i) -> {
            return i.getName().toLowerCase().contains(" pack");
        }) != null;
    }

    public void onAction() {
        Widgets.closeTopInterface();
        if (!Shop.isOpen()) {
            ArrayList<Item> packs = Inventory.container().getAll((i) -> {
                return i.getName().toLowerCase().contains(" pack");
            });
            Iterator var2 = packs.iterator();

            while (var2.hasNext()) {
                Item item = (Item) var2.next();
                item.interact("Open");
                Time.sleep(150 + Settings.renderDelayTime, 350 + Settings.renderDelayTime);
                if (!Inventory.contains(item.getId())) {
                    break;
                }
            }

        }
    }
}
