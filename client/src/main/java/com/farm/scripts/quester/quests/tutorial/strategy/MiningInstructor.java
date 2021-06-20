package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.InputBox.MakeItemDialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class MiningInstructor extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case TALK_TO_MINING_INSTRUCTOR:
            case TALK_TO_MINING_INSTRUCTOR_2:
            case TALK_TO_MINING_INSTRUCTOR_3:
                this.doTalking();
                break;
            case PROSPECT_TIN_ORE:
                this.prospectOre(true);
                break;
            case PROSPECT_COPPER_ORE:
                this.prospectOre(false);
                break;
            case MINE_TIN_ORE:
                this.mineOre(true);
                break;
            case MINE_COPPER_ORE:
                this.mineOre(false);
                break;
            case MAKE_BRONZE_BAR:
                this.makeBronzeBar();
                break;
            case USE_BAR_ON_ANVIL:
                this.openAnvilInterface();
                break;
            case MAKE_BRONZE_DAGGER:
                this.makeBronzeDagger();
        }

    }

    private void doTalking() {
        if (WebWalking.walkTo(new Tile(3082, 9499, 0), 12, new Tile[0]) && Dialogue.talkTo("Mining Instructor", true)) {
            Dialogue.goNext(new String[0]);
        }

    }

    private void mineOre(boolean isTinOre) {
        Tile position = isTinOre ? new Tile(3078, 9504, 0) : new Tile(3082, 9501, 0);
        if (GameObjects.get("Rocks", position).interact("Mine")) {
            Time.waitInventoryChange();
        }

    }

    private void prospectOre(boolean isTinOre) {
        Tile position = isTinOre ? new Tile(3078, 9504, 0) : new Tile(3082, 9501, 0);
        if (GameObjects.get("Rocks", position).interact("Prospect")) {
            TutorialState.waitStateChange();
        }

    }

    private void makeBronzeBar() {
        Item tinOre = Inventory.get("Tin Ore");
        Item copperOre = Inventory.get("Copper Ore");
        if (tinOre != null && copperOre != null) {
            if (InputBox.isMakeItemDialogueOpen()) {
                MakeItemDialogue.MAKE_1.selectAndExecute();
            }

            if (tinOre.interactWith(GameObjects.get("Furnace"))) {
                Time.waitInventoryChange();
            }
        } else {
            this.mineOre(tinOre == null);
        }

    }

    private void openAnvilInterface() {
        Item bronzeBar = Inventory.get("Bronze bar");
        if (bronzeBar != null) {
            if (bronzeBar.interactWith(GameObjects.get("Anvil"))) {
                TutorialState.waitStateChange();
            }
        } else {
            this.makeBronzeBar();
        }

    }

    private void makeBronzeDagger() {
        if (Dialogue.isInDialouge()) {
        }

        Widget w = Widgets.forId(20447241);
        if (w != null && w.isRendered()) {
            Mouse.clickBox(35, 51, 50, 78);
            TutorialState.waitStateChange();
        } else {
            this.openAnvilInterface();
        }

    }
}
