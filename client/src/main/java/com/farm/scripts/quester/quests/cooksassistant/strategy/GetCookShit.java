package com.farm.scripts.quester.quests.cooksassistant.strategy;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.cooksassistant.CookState;

public class GetCookShit extends Strategy implements MessageListener {
    public static final int POT_FULL = 1933;
    public static final int BUCKET_FULL = 1927;
    public static final int EGG = 1944;
    private static final int POT_EMPTY = 1931;
    private static final int BUCKET_EMPTY = 1925;
    private static final int WHEAT = 1947;
    private final Tile TILE_COW = new Tile(3253, 3272, 0);
    private boolean isMealingWheat = false;

    public GetCookShit(BotScript script) {
        if (script != null) {
            script.addEventHandler(new MessageEventHandler(this));
        }

    }

    public boolean active() {
        return !CookTalk.hasIngredients && (CookState.isInState(CookState.PROVIDE_INGREDIENTS) && !Inventory.container().containsAny(new int[]{1933, 1927, 1944}) || !CookState.isInState(CookState.PROVIDE_INGREDIENTS) && !Inventory.container().containsAll(new int[]{1933, 1927, 1944}));
    }

    public void onAction() {
        if (Inventory.getFreeSlots() < 5) {
            if (WebWalking.walkTo(Locations.getClosestBank(), new Tile[0]) && Bank.open()) {
                Bank.depositAll();
            }

        } else if (!Inventory.contains(1933) && !Inventory.contains(1931)) {
            this.grabItem(new Tile(3209, 3213, 0), 1931);
        } else if (!Inventory.contains(1927) && !Inventory.contains(1925)) {
            this.grabItem(new Tile(3216, 9624, 0), 1925);
        } else if (!Inventory.contains(1944)) {
            this.grabItem(new Tile(3226, 3301, 0), 1944);
        } else if (!Inventory.contains(1927)) {
            this.makeMilk();
        } else if (!Inventory.contains(1933)) {
            this.makeFlour();
        }
    }

    public void makeFlour() {
        if (CookState.isInState(CookState.WHEAT_READY)) {
            if (WebWalking.walkTo(new Tile(3166, 3305, 0), 5, new Tile[0])) {
                GameObjects.get(1781).interact("Empty");
                Time.waitInventoryChange();
            }
        } else if (Inventory.contains(1947) && !this.isMealingWheat) {
            if (WebWalking.walkTo(new Tile(3165, 3306, 2), 5, new Tile[0])) {
                Inventory.get(1947).interactWith(GameObjects.get("Hopper"));
                Time.waitInventoryChange();
                Time.sleep(3000, 4000);
                this.isMealingWheat = true;
            }
        } else if (this.isMealingWheat) {
            GameObjects.get("Hopper controls").interact("Operate");
            CookState.waitStateChange();
        } else if (WebWalking.walkTo(new Tile(3159, 3295, 0), 3, new Tile[0])) {
            GameObjects.get("Wheat").interact("Pick");
            Time.waitInventoryChange();
        }

    }

    public void makeMilk() {
        if (WebWalking.walkTo(this.TILE_COW, 4, new Tile[0])) {
            GameObjects.get("Dairy cow").interact("Milk");
            Time.waitInventoryChange();
        }

    }

    public void grabItem(Tile tile, int id) {
        if (WebWalking.walkTo(tile, 2, new Tile[0])) {
            GroundItem item = GroundItems.get(id);
            if (item != null && item.interact("Take")) {
                Time.waitInventoryChange();
            }
        }

    }

    public void onMessage(String message) {
        if (message.toLowerCase().contains("there is already grain")) {
            this.isMealingWheat = true;
        }

        if (message.toLowerCase().contains("you operate")) {
            this.isMealingWheat = false;
        }

    }
}
