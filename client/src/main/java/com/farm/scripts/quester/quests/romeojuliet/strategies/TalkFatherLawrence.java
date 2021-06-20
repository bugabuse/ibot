package com.farm.scripts.quester.quests.romeojuliet.strategies;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.romeojuliet.RomeoAndJuliet;
import com.farm.scripts.quester.quests.romeojuliet.RomeoState;

public class TalkFatherLawrence extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        switch (RomeoState.getState()) {
            case TALK_TO_FATHER_LAWRENCE:
                this.doTalking();
            default:
        }
    }

    private void doTalking() {
        if (WebWalking.walkTo(RomeoAndJuliet.FATHER_LAWRENCE_TILE, 6, new Tile[0])) {
            if (Dialogue.talkTo("Father Lawrence")) {
                Dialogue.goNext(new String[0]);
            }

        }
    }
}
