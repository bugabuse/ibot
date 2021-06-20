package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class BankInteractions extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case INTERACT_BANK:
                this.interactBank();
                break;
            case INTERACT_POOL_BOOTH:
                this.interactPoolBooth();
                break;
            case LEAVE_BANK_ROOM:
                this.leave();
        }

    }

    private void leave() {
        if (GameObjects.get("Door", new Tile(3124, 3124, 0)).interact("Open")) {
            TutorialState.waitStateChange();
        }

    }

    private void interactBank() {
        if (WebWalking.walkTo(new Tile(3122, 3122, 0), 6, new Tile[0])) {
            if (Dialogue.isInDialouge()) {
                Dialogue.goNext(new String[]{"Yes"});
            } else if (GameObjects.get("Bank booth").interact("Use")) {
                Time.sleep(5000, Dialogue::isInDialouge);
            }
        }

    }

    private void interactPoolBooth() {
        if (WebWalking.walkTo(new Tile(3122, 3122, 0), 7, new Tile[0])) {
            if (Dialogue.isInDialouge()) {
                Dialogue.goNext(new String[]{"Yes"});
            } else if (GameObjects.get("Poll booth").interact("Use")) {
                TutorialState.waitStateChange();
            }
        }

    }
}
