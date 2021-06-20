package com.farm.scripts.mulereceiver.strategy;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.script.Strategy;

public class AlchStrategy extends Strategy {
    public boolean active() {
        return Inventory.container().contains("Maple longbow") && Inventory.container().contains("Nature rune") && Skill.MAGIC.getRealLevel() > 55;
    }

    protected void onAction() {

        Widgets.closeTopInterface();
        if (GameTab.MAGIC.open()) {
            Magic.HIGH_ALCH.click();
            Time.sleep(20, 60);
            Inventory.get("Maple longbow").interact(ItemMethod.MAGIC_ON_ITEM);
            this.sleep(1800);
        }

    }
}
