package com.farm.scripts.woodcutter.strategy.main;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.woodcutter.ChopSettings;
import com.farm.scripts.woodcutter.Strategies;

public class DropStrategy extends Strategy {
    public boolean active() {
        return ChopSettings.isDroppingLogs() && !Strategies.BANK_STRATEGY.active() && Inventory.isFull();
    }

    public void onAction() {
        Item[] var1 = Inventory.getAll();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Item item = var1[var3];
            if (item.getName().toLowerCase().contains("log")) {
                item.interact("Drop");
                Time.sleep(100, 280);
            }
        }

    }
}
