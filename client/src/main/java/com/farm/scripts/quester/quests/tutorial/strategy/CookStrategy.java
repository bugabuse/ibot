package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class CookStrategy extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case OPEN_COOK_DOORS:
                this.openCooks();
                break;
            case TALK_TO_COOK:
                this.talkToCook();
                break;
            case COMBINE_FLOUR_WITH_WATER:
                this.combineShit();
                break;
            case USE_BREAD_WITH_RANGE:
                this.makeBread();
                break;
            case LEAVE_COOKS_BUILDING:
                this.leave();
        }

    }

    private void leave() {
        if (WebWalking.walkTo(new Tile(3074, 3090, 0), 1, new Tile[0])) {
            GameObject doors = GameObjects.get("Door", new Tile(3074, 3090, 0));
            doors.interactAndWaitDisappear("Open");
        }

    }

    private void makeBread() {
        Item bread = Inventory.get("Bread dough");
        if (bread != null && bread.interactWith(GameObjects.get("Range"))) {
            Time.waitInventoryChange();
        }

    }

    private void combineShit() {
        Item item1 = Inventory.get("Pot of flour");
        Item item2 = Inventory.get("Bucket of water");
        if (item1 != null && item2 != null && item1.interactWith(item2)) {
            Time.waitInventoryChange();
        }

    }

    private void talkToCook() {
        if (Dialogue.talkTo("Master Chef", true)) {
            Dialogue.goNext(new String[0]);
        }

    }

    private void openCooks() {
        if (WebWalking.walkTo(new Tile(3079, 3084, 0), 3, new Tile[0])) {
            GameObjects.get("Door", new Tile(3079, 3084, 0)).interactAndWaitDisappear("Open");
        }

    }
}
