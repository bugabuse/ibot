package com.farm.scripts.thiever.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.core.script.Strategy;

public class EatFoodStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (!Inventory.isFull()) {
            if (Skill.HITPOINTS.getCurrentLevel() > ThieveManStrategy.getThievingNpc().hpToHeal && (Skill.HITPOINTS.getCurrentLevel() > 4 || Player.getLocal().getModelHeight() != 1000)) {
                return;
            }

            if (!Inventory.container().contains("Jug of wine")) {
                return;
            }
        }

        Inventory.container().get("Jug of wine").interact("Eat");
        this.sleep(600, 800);
    }
}
