package com.farm.scripts.combat.strategy;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.combat.Settings;

public class BonesStrategy extends Strategy {
    boolean pickingBones = false;

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Item bones = Inventory.container().get((i) -> {
            return i.getName().toLowerCase().contains("bones");
        });
        if (bones != null) {
            ScriptUtils.interruptCurrentLoop();
            bones.interact("Bury");
            Time.waitInventoryChange();
        } else if (!Inventory.isFull()) {
            Filter<GroundItem> f = (i) -> {
                return i.getDefinition().name.toLowerCase().contains("bones") && i.getPosition().isReachable() && Settings.getSpot().area.contains(i.getPosition()) && i.getPosition().distance() < 8;
            };
            if (GroundItems.getAll(f).size() > 2) {
                this.pickingBones = true;
            }

            GroundItem item = GroundItems.get(f);
            if (item != null) {
                if (this.pickingBones) {
                    ScriptUtils.interruptCurrentLoop();
                    item.interact("Take");
                    Time.waitInventoryChange();
                }
            } else {
                this.pickingBones = false;
            }
        }

    }
}
