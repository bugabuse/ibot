package com.farm.scripts.fletcher.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.InputBox.MakeItemDialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fisher.util.RequiredItem;
import com.farm.scripts.fletcher.Constants;

import java.awt.*;

public class FletchStrategy extends Strategy {
    private PaintTimer lastAnimUpdate = new PaintTimer(0L);

    public boolean active() {
        if (Player.getLocal().getAnimation() != -1) {
            this.lastAnimUpdate.reset();
        }

        return true;
    }

    protected void onAction() {
        if (Skill.FLETCHING.getRealLevel() >= 10) {
            this.makeUnfinishedBows();
        } else {
            this.trainSkill();
        }

    }

    private void makeUnfinishedBows() {
        RequiredItem[] items = Constants.getRequiredItems().getItems();
        if (this.ensure(items[0].getId(), items[1].getId())) {
            Rectangle rect = new Rectangle(560, 210, 166, 253);
            Point p = Random.next(rect);
            p.translate(Random.next(0, 60), Random.next(0, 60));
            Time.sleep(200);
            Inventory.container().getFromPoint(p, items[0].getId()).interactWith(Inventory.container().getFromPoint(p, items[1].getId()));
            if (Time.sleep(InputBox::isMakeItemDialogueOpen)) {
                if (items[0].getName().contains("Knife")) {
                    MakeItemDialogue.MAKE_ALL.selectAndExecute(3);
                } else {
                    MakeItemDialogue.MAKE_ALL.selectAndExecute();
                }

                Time.sleep(() -> {
                    return Player.getLocal().getAnimation() != -1;
                });
            }

        }
    }

    private void trainSkill() {
        if (this.ensure(946, 1511)) {
            Inventory.get(946).interactWith(Inventory.get(1511));
            Time.sleep(1200);
            MakeItemDialogue.MAKE_ALL.selectAndExecute();
            Time.sleep(1200);
        }
    }

    private boolean ensure(int... ids) {
        if (!Inventory.container().containsAll(ids)) {
            return false;
        } else if (this.lastAnimUpdate.getElapsedSeconds() < 2L) {
            return false;
        } else {
            Keyboard.press(27);
            return true;
        }
    }
}
