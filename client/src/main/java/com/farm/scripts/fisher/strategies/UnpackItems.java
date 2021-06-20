package com.farm.scripts.fisher.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;

import java.util.Iterator;

public class UnpackItems extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        if (Inventory.container().contains((i) -> {
            return i.getName().contains(" pack");
        })) {
            Widgets.closeTopInterface();
            Iterator var1 = Inventory.container().getAll((i) -> {
                return i.getName().contains(" pack");
            }).iterator();

            while (var1.hasNext()) {
                Item item = (Item) var1.next();
                item.interact("Open");
                Time.waitInventoryChange();
                ScriptUtils.interruptCurrentLoop();
            }
        }

    }
}
