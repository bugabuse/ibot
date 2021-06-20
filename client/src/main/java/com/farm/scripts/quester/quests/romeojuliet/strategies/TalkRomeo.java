package com.farm.scripts.quester.quests.romeojuliet.strategies;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.romeojuliet.RomeoAndJuliet;
import com.farm.scripts.quester.quests.romeojuliet.RomeoState;

public class TalkRomeo extends Strategy {
    private static final String[] DIALOGUE_OPTIONS = new String[]{"Perhaps I could help to find her for you?", "Yes, ok, I'll let her know"};

    public boolean active() {
        return true;
    }

    public void onAction() {
        switch (RomeoState.getState()) {
            case TALK_TO_ROMEO:
            case TALK_TO_ROMEO_2:
                this.doTalking();
                break;
            case DELIVER_MESSAGE:
                this.deliverMessage();
        }

    }

    private void deliverMessage() {
        if (Inventory.getCount(755) > 0) {
            this.doTalking();
        } else {
            TalkJuliet.doTalking();
        }

    }

    private void doTalking() {
        Npc romeo = Npcs.get("Romeo");
        if (romeo != null && romeo.getPosition().distance() <= 10 || WebWalking.walkTo(RomeoAndJuliet.ROMEO_TILE, 13, new Tile[0])) {
            if (Dialogue.talkTo("Romeo")) {
                Dialogue.goNext(DIALOGUE_OPTIONS);
            }

        }
    }
}
