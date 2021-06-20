package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.zulrahkiller.api.Positions;

public class BoardTravelStrategy extends Strategy {
    public boolean active() {
        return Positions.BOAT_TILE.distance() < 30;
    }

    protected void onAction() {
        if (Inventory.container().getCount(new String[]{"Manta ray"}) < 14) {
            this.teleportBack();
        } else if (Inventory.container().getCount(new String[]{"Prayer potion(4)"}) < 2) {
            this.teleportBack();
        } else if (Inventory.container().getCount(new String[]{"Ring of recoil"}) < 1) {
            this.teleportBack();
        } else {
            Item potion;
            if (Skill.RANGE.getCurrentPercent() <= 110) {
                potion = Inventory.container().get((i) -> {
                    return i.getName().startsWith("Ranging potion");
                });
                if (potion != null) {
                    potion.interact(ItemMethod.DRINK);
                    Time.sleep(1650, 1850);
                    return;
                }
            }

            if (Skill.MAGIC.getCurrentPercent() <= 100) {
                potion = Inventory.container().get((i) -> {
                    return i.getName().startsWith("Magic potion");
                });
                if (potion != null) {
                    potion.interact(ItemMethod.DRINK);
                    Time.sleep(1650, 1850);
                    return;
                }
            }

            GameObject boat = GameObjects.get("Sacrificial boat");
            if (boat != null) {
                boat.interact("Board");
                Time.waitObjectDissapear(boat);
            } else {
                Walking.walkTo(Positions.BOAT_TILE, 3);
            }

        }
    }

    private void teleportBack() {
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
