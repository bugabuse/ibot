package com.farm.scripts.farmtrainer.strategies;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interact.action.impl.ObjectAction;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.script.Strategy;

public class MakePlants extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (!this.atHouse()) {
            if (Widgets.closeTopInterface()) {
                Inventory.container().get("Teleport to house").interact("Break");
                Time.sleep(this::atHouse);
                Time.sleep(600, 1200);
            }
        } else if (!Varbit.HOUSE_BUILD_MODE.booleanValue()) {
            if (GameTab.OPTIONS.open()) {
                Mouse.click(654, 448);
                if (Time.sleep(() -> {
                    return Widgets.get((w) -> {
                        return w.isRendered() && w.getText().contains("Building Mode");
                    }) != null;
                })) {
                    Mouse.click(692, 280);
                }

                Time.sleep(this::atHouse);
                Time.sleep(600, 1200);
            }

        } else {
            GameObject plantSpace = GameObjects.get("Small plant space 1");
            GameObject plant = GameObjects.get("Plant");
            if (plant != null) {
                ObjectAction.create(998, plant).send();
                if (Time.sleep(() -> {
                    return Dialogue.contains("Yes");
                })) {
                    Dialogue.selectOptionThatContains("Yes");
                    Time.sleep(() -> {
                        return !Dialogue.isInDialouge();
                    });
                }

            } else {
                if (plantSpace != null) {
                    ObjectAction.create(998, plantSpace).send();
                    if (Time.sleep(() -> {
                        return Widgets.get((w) -> {
                            return w.isRendered() && w.getText().contains("Furniture Creation Menu");
                        }) != null;
                    })) {
                        Keyboard.type("1");
                        Time.sleep(() -> {
                            return !plantSpace.exists();
                        });
                        return;
                    }
                }

            }
        }
    }

    private boolean atHouse() {
        return GameObjects.get(4525) != null && GameObjects.get(4530) != null && GameObjects.get(13099) != null;
    }
}
