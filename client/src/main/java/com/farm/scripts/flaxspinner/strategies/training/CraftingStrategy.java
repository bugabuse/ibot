package com.farm.scripts.flaxspinner.strategies.training;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.InputBox.MakeItemDialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.flaxspinner.Constants;

public class CraftingStrategy extends Strategy {
    private static long lastAnim = 0L;

    public static boolean isSpinning() {
        if (Player.getLocal().getAnimation() != -1) {
            lastAnim = System.currentTimeMillis();
        }

        return System.currentTimeMillis() - lastAnim < 5500L;
    }

    protected void onAction() {
        if (!isSpinning() || Dialogue.isInDialouge()) {
            if (Inventory.container().containsAllOne(Constants.getTrainingItems())) {
                if (Widgets.closeTopInterface()) {
                    Inventory.get(Constants.getTrainingItems()[0].getId()).interactWith(Inventory.get(Constants.getTrainingItems()[1].getId()));
                    Time.sleep(InputBox::isMakeItemDialogueOpen);
                    if (MakeItemDialogue.MAKE_ALL.selectAndExecute(1)) {
                        Time.sleep(CraftingLeatherStrategy::isSpinning);
                    }

                }
            }
        }
    }
}
