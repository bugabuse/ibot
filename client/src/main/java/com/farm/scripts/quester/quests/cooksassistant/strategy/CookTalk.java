package com.farm.scripts.quester.quests.cooksassistant.strategy;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.cooksassistant.CookState;
import com.google.common.primitives.Ints;

public class CookTalk extends Strategy {
    private static final String[] OPTIONS_TO_CLICK = new String[]{"What's wrong?", "I'm always happy to help a cook in distress.", "Actually, I know where to find this"};
    public static Tile COOKS_TILE = new Tile(3208, 3213, 0);
    public static boolean hasIngredients = false;
    private int[] required = new int[]{1933, 1927, 1944};

    public boolean active() {
        return CookState.isInState(CookState.PROVIDE_INGREDIENTS) || Inventory.container().containsAll(this.required) || hasIngredients;
    }

    public void onAction() {

        if (WebWalking.walkTo(COOKS_TILE, 7, new Tile[0])) {
            if (CookState.isInState(CookState.PROVIDE_INGREDIENTS)) {
                Item item = Inventory.container().get((i) -> {
                    return Ints.contains(this.required, i.getId());
                });
                if (item != null) {
                    hasIngredients = true;
                }

                if (!Dialogue.isInDialouge() && item != null) {
                    if (item.interactWith(Npcs.get("Cook"))) {
                        Time.waitInventoryChange();
                    }
                } else if (Dialogue.talkTo("Cook")) {
                    Dialogue.goNext(OPTIONS_TO_CLICK);
                }
            } else if (Dialogue.talkTo("Cook")) {
                Dialogue.goNext(OPTIONS_TO_CLICK);
            }

        }
    }
}
