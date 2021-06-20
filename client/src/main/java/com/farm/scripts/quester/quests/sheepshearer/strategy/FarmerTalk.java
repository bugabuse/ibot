package com.farm.scripts.quester.quests.sheepshearer.strategy;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.sheepshearer.SheepState;

public class FarmerTalk extends Strategy {
    private static final String[] OPTIONS_TO_CLICK = new String[]{"I'm looking for a quest", "Yes okay. I can", "Of course", "I'm something of an expert", "I'm back!"};
    public static Tile FARMER_TILE = new Tile(3190, 3272, 0);

    public boolean active() {
        return Inventory.contains(1759, 20) || SheepState.isInState(SheepState.FINISHING_QUEST);
    }

    public void onAction() {
        if (WebWalking.walkTo(FARMER_TILE, 7, new Tile[0]) && Dialogue.talkTo("Fred the Farmer", true)) {
            Dialogue.goNext(OPTIONS_TO_CLICK);
        }

    }
}
