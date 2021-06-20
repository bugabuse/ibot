package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class Skippy extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case TALK_SKIPPY:
                if (Dialogue.talkTo("Skippy")) {
                    Dialogue.goNext(new String[]{"Send me to the Iron Man tutors", "Great, send me"});
                }
            default:
        }
    }
}
