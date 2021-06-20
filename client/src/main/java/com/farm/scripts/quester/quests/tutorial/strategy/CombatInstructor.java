package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Combat;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class CombatInstructor extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case OPEN_COMBAT_INSTRUCTOR_GATE:
                this.enter();
                break;
            case TALK_TO_COMBAT_INSTRUCTOR:
            case TALK_TO_COMBAT_INSTRUCTOR_2:
            case CLOSE_INTERFACE:
                this.doTalking();
                break;
            case VIEW_WORN_EQUIPMENT:
                this.viewEquipment();
                break;
            case WIELD_DAGGER:
                this.wieldDagger();
                break;
            case UNEQUIP_DAGGER_WIELD_SWORD:
                this.unequipAndWield();
                break;
            case OPEN_GATE_RATS:
                this.enterRatRoom();
                break;
            case ATTACK_RAT_MELEE:
            case ATTACK_RAT_MELEE_IN_PROGRESS:
                this.attackRatMelee();
                break;
            case ATTACK_RAT_RANGED:
            case ATTACK_RAT_RANGED_IN_PROGRESS:
                this.attackRatRanged();
                break;
            case LEAVE_UNDERGROUND:
                this.leave();
        }

    }

    private void leave() {
        if (GameObjects.get("Ladder", new Tile(3111, 9525)).interactAndWaitDisappear("Climb-up")) {
            TutorialState.waitStateChange();
        }

    }

    private void attackRatRanged() {
        if (!Equipment.isEquipped("Shortbow")) {
            Inventory.get("Shortbow").interact("Wield");
            Time.waitInventoryChange();
        } else if (Inventory.container().contains("Bronze arrow")) {
            Inventory.get("Bronze arrow").interact("Wield");
            Time.waitInventoryChange();
        } else {
            if (!Combat.isInCombat()) {
                Npcs.get("Giant rat").interact("Attack");
                Time.sleep(Combat::isInCombat);
            }

        }
    }

    private void attackRatMelee() {
        if (!Combat.isInCombat()) {
            Npc rat = Npcs.get("Giant rat");
            if (rat.isReachable()) {
                rat.interact("Attack");
                Time.sleep(Combat::isInCombat);
            } else {
                this.enterRatRoom();
            }
        }

    }

    private void enterRatRoom() {
        GameObject obj = GameObjects.get("Gate", new Tile(3112, 9518));
        if (obj != null && obj.getPosition().distance(new Tile(3112, 9518)) < 4) {
            obj.interactAndWaitDisappear("Open");
        }

    }

    private void unequipAndWield() {
        if (Inventory.container().contains("Bronze sword")) {
            Inventory.get("Bronze sword").interact("Wield");
            Time.waitInventoryChange();
        }

        if (Inventory.container().contains("Wooden shield")) {
            Inventory.get("Wooden shield").interact("Wield");
            Time.waitInventoryChange();
        }

    }

    private void wieldDagger() {
        if (!Widgets.isValid(84)) {
            this.viewEquipment();
            Time.sleep(600, 800);
        } else {
            Item dagger = Inventory.get("Bronze dagger");
            if (dagger != null) {
                dagger.interact("Equip");
                Time.sleep(600, 800);
            }

        }
    }

    private void viewEquipment() {
        if (GameTab.EQUIPMENT.open()) {
            Mouse.clickBox(555, 413, 591, 451);
        }

    }

    private void enter() {
        if (GameObjects.get("Gate", new Tile(3094, 9502, 0)).interactAndWaitDisappear("Open")) {
            TutorialState.waitStateChange();
        }

    }

    private void doTalking() {
        Npc instructor = Npcs.get("Combat Instructor");
        if (instructor != null && instructor.isReachable()) {
            if (Dialogue.talkTo("Combat Instructor", true)) {
                Dialogue.goNext(new String[0]);
            }
        } else {
            WebWalking.walkTo(new Tile(3107, 9508, 0), 5, new Tile[0]);
        }

    }
}
