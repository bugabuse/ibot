package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;

import java.util.ArrayList;

public class LootStrategy extends Strategy {
    public boolean active() {
        return GameObjects.get((o) -> {
            return o.getName().contains("Zul-Andra teleport");
        }) != null;
    }

    protected void onAction() {
        ArrayList<GroundItem> all = GroundItems.getAll((f) -> {
            return true;
        });
        if (all.size() > 0) {
            ((GroundItem) all.get(0)).interact("Take");
            Time.waitInventoryChange();
        } else {
            Item ring = Inventory.container().get((i) -> {
                return i.getName().contains("Ring of dueling");
            });
            if (ring != null && ring.interact("Rub")) {
                Time.sleep(1200);
                if (Time.sleep(Dialogue::isInDialouge)) {
                    Dialogue.goNext(new String[]{"Clan Wars Arena"});
                    Time.waitObjectDissapear(GameObjects.get((o) -> {
                        return o.getName().contains("Zul-Andra teleport");
                    }));
                }
            }
        }

    }
}
