package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.zulrahkiller.api.Zulrah;

public class HealStrategy extends Strategy {
    public static boolean isVenomActive() {
        Widget w = Widgets.get(160, 6);
        return w != null && w.getTextureId() == 1102;
    }

    public boolean active() {
        return Zulrah.get() != null || Zulrah.getZulrahIndex() > 0;
    }

    protected void onAction() {
        Item shark = Inventory.get("Manta ray");
        Item prayerPotion = Inventory.container().get((i) -> {
            return i.getName().startsWith("Prayer potion");
        });
        Item antiVenom = Inventory.container().get((i) -> {
            return i.getName().startsWith("Anti-venom+");
        });
        if (Skill.HITPOINTS.getCurrentLevel() <= 49) {
            ScriptUtils.waitForStrategyExecute(this);
            if (shark != null && shark.interact(ItemMethod.EAT)) {
                this.sleep(600, 800);
            }
        } else {
            if (isVenomActive()) {
                ScriptUtils.waitForStrategyExecute(this);
                if (antiVenom != null && antiVenom.interact(ItemMethod.DRINK)) {
                    this.sleep(600, 700);
                }
            }

            if (Skill.PRAYER.getCurrentPercent() < 50) {
                ScriptUtils.waitForStrategyExecute(this);
                if (prayerPotion != null && prayerPotion.interact(ItemMethod.DRINK)) {
                    this.sleep(600, 700);
                }
            }

        }
    }

    public boolean isBackground() {
        return true;
    }
}
