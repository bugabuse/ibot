package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class FinancialAdvisor extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case TALK_FINANCIAL_ADVISOR:
            case TALK_FINANCIAL_ADVISOR_2:
                this.doTalking();
                break;
            case LEAVE_FINANCIAL_ROOM:
                this.leave();
        }

    }

    private void leave() {
        GameObjects.get("Door", new Tile(3129, 3124, 0)).interactAndWaitDisappear("Open");
    }

    private void doTalking() {
        System.out.println("Do talkin");
        if (Dialogue.talkTo("Account Guide", true)) {
            Dialogue.goNext(new String[0]);
        }

    }
}
