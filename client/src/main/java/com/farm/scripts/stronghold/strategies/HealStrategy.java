package com.farm.scripts.stronghold.strategies;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;

public class HealStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    public boolean isBackground() {
        return true;
    }

    protected void onAction() {
        if (Skill.HITPOINTS.getCurrentLevel() <= 9) {
            ScriptUtils.interruptCurrentLoop();
            if (Inventory.get("Jug of wine") != null && Inventory.get("Jug of wine").interact("Drink")) {
                Time.sleep(300, 600);
            }
        }

    }
}
