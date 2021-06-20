package com.farm.scripts.combat.strategy;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.combat.Settings;

public class EatStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        Item shark = Inventory.get(Settings.FOOD_ID);
        if (Inventory.isFull() || Skill.HITPOINTS.getCurrentPercent() <= 40) {
            ScriptUtils.waitForStrategyExecute(this);
            if (shark != null && shark.interact(ItemMethod.EAT)) {
                this.sleep(1200, 1500);
            }
        }
    }

    public boolean isBackground() {
        return true;
    }
}
