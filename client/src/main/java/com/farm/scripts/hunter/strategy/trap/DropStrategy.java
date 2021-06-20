package com.farm.scripts.hunter.strategy.trap;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;

import java.util.Iterator;

public class DropStrategy extends Strategy {
    public static DropStrategy instance;
    private final String[] junk = new String[]{"kebbit fur", "bones", "bird meat"};

    public DropStrategy() {
        instance = this;
    }

    public boolean active() {
        return Inventory.getFreeSlots() < 2;
    }

    public void onAction() {
        Iterator var1 = Inventory.container().getAll((i) -> {
            return StringUtils.containsAny(i.getDefinition().name, this.junk);
        }).iterator();

        while (var1.hasNext()) {
            Item item = (Item) var1.next();
            if (item.interact("Drop")) {
                Time.sleep(150, 250);
            }
        }

    }
}
