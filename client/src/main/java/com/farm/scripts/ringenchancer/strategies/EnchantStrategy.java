package com.farm.scripts.ringenchancer.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;

public class EnchantStrategy extends Strategy {
    public boolean active() {
        return Inventory.contains(1637) && Inventory.contains(564);
    }

    protected void onAction() {
        if ((!Bank.isOpen() || Widgets.closeTopInterface()) && Magic.LVL_1_ENCHANT.select()) {
            Time.sleep(200, 400);
            Inventory.get(1637).interact("Cast");
            if (Time.sleep(() -> {
                return Player.getLocal().getAnimation() != -1;
            })) {
                Time.sleep(1100, 1200);
            }
        }

    }
}
