package com.farm.scripts.quester.quests.tutorialnew.strategy;

import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorialnew.TutorialStateNew;

public class EmoteStrategy extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialStateNew.getState()) {
            case PERFORM_EMOTE:
                this.performEmote();
                break;
            case ENABLE_RUN:
                Walking.setRun(!Walking.isRunEnabled());
        }

    }

    private void performEmote() {
        if (GameTab.EMOTES.open()) {
            Widgets.get((w) -> {
                return w.getTextureId() == 701;
            }).interact("");
            TutorialStateNew.waitStateChange();
        }

    }
}
