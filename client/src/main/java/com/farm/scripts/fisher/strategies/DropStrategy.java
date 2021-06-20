package com.farm.scripts.fisher.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fisher.FishSettings;

public class DropStrategy extends Strategy {
    public boolean active() {
        return Inventory.isFull() && !FishSettings.getConfig().isBankingEnabled();
    }

    public void onAction() {
        Item[] var1 = Inventory.getAll();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Item item = var1[var3];
            if (item.getName().startsWith("Raw")) {
                long start = System.currentTimeMillis();
                item.interact("Drop");
                int diff = (int) (System.currentTimeMillis() - start);
                Time.sleep(50, MathUtils.clamp(100 - diff, 50, 150));
            }
        }

    }
}
