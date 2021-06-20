package com.farm.scripts.quester.quests.tutorialnew.strategy;

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
import com.farm.scripts.quester.quests.tutorialnew.TutorialStateNew;

public class MiningInstructor extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialStateNew.getState()) {
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
            case MAKE_BRONZE_DAGGER:
                this.makeBronzeDagger();
        }

    }

    private void doTalking() {
        if (WebWalking.walkTo(new Tile(1672, 12511, 0), 12, new Tile[0]) && Dialogue.talkTo("Mining Instructor", true)) {
            Dialogue.goNext(new String[0]);
        }

    }

    private void mineOre(boolean isTinOre) {
        int id = isTinOre ? '鐹' : '鐸';
        if (GameObjects.get(id).interact("Mine")) {
            Time.waitInventoryChange();
        }

    }

    private void prospectOre(boolean isTinOre) {
        int id = isTinOre ? '鐹' : '鐸';
        if (GameObjects.get(id).interact("Prospect")) {
            TutorialStateNew.waitStateChange();
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
                TutorialStateNew.waitStateChange();
            }
        } else {
            this.makeBronzeBar();
        }

    }

    private void makeBronzeDagger() {
        Widget w = Widgets.forId(20447241);
        if (w == null) {
            System.out.println("Anvil interface idk why?");
            this.openAnvilInterface();
        } else {
            System.out.println("Click box!");
            Mouse.clickBox(35, 51, 50, 78);
            TutorialStateNew.waitStateChange();
        }

    }
}
