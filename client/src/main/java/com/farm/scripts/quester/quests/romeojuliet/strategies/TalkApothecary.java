package com.farm.scripts.quester.quests.romeojuliet.strategies;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.romeojuliet.RomeoAndJuliet;
import com.farm.scripts.quester.quests.romeojuliet.RomeoState;

public class TalkApothecary extends Strategy {
    public static final int CADAVA_POTION_ID = 756;
    private static final int CADAVA_BERRIES_ID = 753;

    public boolean active() {
        return true;
    }

    public void onAction() {
        switch (RomeoState.getState()) {
            case TALK_TO_APOTHECARY:
                this.doTalking();
                break;
            case MAKE_CADAVA_POTION_AND_DELIVER_TO_JULIET:
                this.makePotion();
        }

    }

    private void makePotion() {
        if (!Inventory.contains(756)) {
            if (Inventory.contains(753)) {
                this.doTalking();
            } else if (WebWalking.walkTo(RomeoAndJuliet.CADAVA_TILE, 8, new Tile[0])) {
                GameObjects.get((g) -> {
                    return "Cadava bush".equalsIgnoreCase(g.getName()) && g.getId() != 23627;
                }).interact("Pick-from");
                Time.waitInventoryChange();
            }
        }

    }

    private void doTalking() {
        if (WebWalking.walkTo(RomeoAndJuliet.APOTHECARY_TILE, 7, new Tile[0])) {
            if (Dialogue.talkTo("Apothecary")) {
                Dialogue.goNext(new String[]{"Talk about something else.", "Talk about Romeo & Juliet."});
            }

        }
    }
}
