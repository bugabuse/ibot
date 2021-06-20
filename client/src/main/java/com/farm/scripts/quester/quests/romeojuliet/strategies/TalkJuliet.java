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

public class TalkJuliet extends Strategy {
    public static void talkIfMust() {
        Npc juliet = Npcs.get("Juliet");
        if (Inventory.contains(756) || juliet != null && juliet.getPosition().distance() < 6) {
            if ((juliet == null || !juliet.isReachable()) && !WebWalking.walkTo(RomeoAndJuliet.JULIET_TILE, 3, new Tile[0])) {
                return;
            }


            if (Dialogue.talkTo("Juliet")) {
                Dialogue.goNext(new String[]{"Something else"});
            }
        }

    }

    public static void doTalking() {
        if (WebWalking.walkTo(RomeoAndJuliet.JULIET_TILE, 3, new Tile[0]) && Npcs.get("Juliet") != null) {
            if (Dialogue.talkTo("Juliet")) {
                Dialogue.goNext(new String[]{"Something else"});
            }

        }
    }

    public boolean active() {
        return true;
    }

    public void onAction() {
        switch (RomeoState.getState()) {
            case MAKE_CADAVA_POTION_AND_DELIVER_TO_JULIET:
                talkIfMust();
                break;
            case TALK_TO_JULIET:
                doTalking();
        }

    }
}
