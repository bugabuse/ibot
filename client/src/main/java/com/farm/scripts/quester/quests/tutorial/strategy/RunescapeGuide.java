package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class RunescapeGuide extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case LEAVE_STARTING_ROOM:
                GameObjects.get("Door").interactAndWaitDisappear("Open");
                break;
            case TALK_GUIDE:
            case TALK_GUIDE_2:
            case CLICK_SETTINGS:
                if (Dialogue.talkTo("Gielinor Guide", true)) {
                    Dialogue.goNext(new String[]{"I am an experienced player."});
                }
        }

    }
}
