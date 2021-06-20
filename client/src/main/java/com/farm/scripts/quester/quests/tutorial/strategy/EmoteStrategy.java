package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class EmoteStrategy extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case PERFORM_EMOTE:
                this.performEmote();
                break;
            case ENABLE_RUN:
                Walking.setRun(!Walking.isRunEnabled());
                Time.sleep(1000, 2000);
        }

    }

    private void performEmote() {
        if (GameTab.EMOTES.open()) {
            Widgets.get((w) -> {
                return w.getTextureId() == 701;
            }).interact("");
            TutorialState.waitStateChange();
        }

    }
}
