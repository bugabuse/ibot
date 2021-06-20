package com.farm.scripts.quester.quests.tutorialnew.strategy;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorialnew.TutorialStateNew;

public class QuestGuide extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialStateNew.getState()) {
            case OPEN_QUEST_DOOR:
                this.enter();
                break;
            case TALK_TO_QUEST_GUIDE:
            case TALK_TO_QUEST_GUIDE_2:
                this.doTalking();
                break;
            case LEAVE_QUEST_ROOM:
                this.leave();
        }

    }

    private void leave() {
        GameObjects.get("Ladder", new Tile(3088, 3126, 0)).interactAndWaitDisappear("Climb-down");
    }

    private void enter() {
        if (WebWalking.walkTo(new Tile(1678, 6130, 0), 5, new Tile[0])) {
            GameObjects.get("Door", new Tile(3088, 3126, 0)).interactAndWaitDisappear("Open");
        }

    }

    private void doTalking() {
        if (Dialogue.talkTo("Quest Guide", true)) {
            Dialogue.goNext(new String[0]);
        }

    }
}
