package com.farm.scripts.quester.quests.tutorialnew.strategy;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorialnew.TutorialStateNew;

public class CookStrategy extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialStateNew.getState()) {
            case OPEN_COOK_DOORS:
                this.openCooks();
                break;
            case TALK_TO_COOK:
                this.talkToCook();
                break;
            case COMBINE_FLOUR_WITH_WATER:
                this.talkToCook();
                break;
            case USE_BREAD_WITH_RANGE:
                this.talkToCook();
                break;
            case CLICK_MINIMAP:
                this.clickMinimap();
                break;
            case LEAVE_COOKS_BUILDING:
                this.leave();
        }

    }

    private void clickMinimap() {
        (new Action(-1, 10485806, 57, 2, "Floating <col=ff9040>World Map</col>", "", 720 + Random.next(-30, 30), 130 + Random.next(-30, 30))).sendByMouse();
        Time.sleep(1000, 2000);
        Keyboard.type(27);
    }

    private void leave() {
        if (WebWalking.walkTo(new Tile(1665, 6098, 0), 1, new Tile[0])) {
            GameObject doors = GameObjects.get("Door", new Tile(1665, 6098, 0));
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
        if (Dialogue.talkTo("Master Navigator", true)) {
            Dialogue.goNext(new String[0]);
        }

    }

    private void openCooks() {
        if (WebWalking.walkTo(new Tile(1671, 6092, 0), 3, new Tile[0])) {
            GameObjects.get("Door", new Tile(1671, 6092, 0)).interactAndWaitDisappear("Open");
        }

    }
}
