package com.farm.scripts.quester.quests.tutorialnew.strategy;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorialnew.TutorialStateNew;

public class RunescapeGuide extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        System.out.println("State " + TutorialStateNew.getState());
        if (Widgets.get(663, 7) == null || !Widgets.get(663, 7).isRendered()) {
            if (TutorialStateNew.getState() == TutorialStateNew.NONE) {
                System.out.println("Tallk");
                if (Dialogue.talkTo("Gielinor Guide", true)) {
                    Dialogue.goNext(new String[]{""});
                }
            }

            switch (TutorialStateNew.getState()) {
                case LEAVE_STARTING_ROOM:
                    GameObjects.get("Door").interactAndWaitDisappear("Open");
                    break;
                case TALK_GUIDE:
                case TALK_GUIDE_2:
                case CLICK_SETTINGS:
                    System.out.println("Tallk");
                    if (Dialogue.talkTo("Gielinor Guide", true)) {
                        Dialogue.goNext(new String[]{""});
                    }
            }

        }
    }
}
