package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.pathfinding.impl.LocalPathFinder;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class SurvivalExpert extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve() || TutorialState.getState() == TutorialState.OPEN_INVENTORY || Dialogue.isInDialouge();
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case TALK_SURVIVAL_EXPERT:
            case TALK_SURVIVAL_EXPERT_2:
            case TALK_SURVIVAL_EXPERT_LOST_TINDERBOX:
            case OPEN_INVENTORY:
                this.ensureLocation();
                this.doTalk();
                break;
            case CUT_DOWN_TREE:
                this.ensureLocation();
                this.cutTree();
                break;
            case MAKE_FIRE:
                this.ensureLocation();
                this.makeFire();
                break;
            case FISH_SHRIPMS:
                this.ensureLocation();
                this.doFishing();
                break;
            case COOK_SHRIPMS:
            case BURNT_SHRIMP:
                this.ensureLocation();
                this.doCooking();
                break;
            case WALK_TO_GATE:
                this.openGate();
        }

    }

    private boolean ensureLocation() {
        return WebWalking.walkTo(new Tile(3103, 3097), 6, new Tile[0]);
    }

    private void openGate() {
        if (GameObjects.get("Gate").interact("Open")) {
            TutorialState.waitStateChange();
        }

    }

    private void doTalk() {
        if (Dialogue.talkTo("Survival Expert", true)) {
            Dialogue.goNext(new String[]{""});
        }

    }

    private void makeFire() {
        Item tinder = Inventory.get("Tinderbox");
        Item log = Inventory.get(2511);
        if (log == null) {
            this.cutTree();
        } else if (WebWalking.walkTo(this.getNearestFireTile(), 0, new Tile[0]) && tinder.interactWith(log)) {
            Time.waitInventoryChange();
        }

    }

    private void cutTree() {
        if (GameObjects.get((o) -> {
            return o.getName().contains("Tree") && o.isReachable();
        }).interact("Chop down")) {
            Time.waitInventoryChange();
        }

    }

    private void doCooking() {
        GameObject fire = GameObjects.get("Fire");
        if (fire != null) {
            Item shrimps = Inventory.get(2514);
            if (shrimps != null) {
                if (shrimps.interactWith(fire)) {
                    Time.waitInventoryChange();
                }
            } else {
                this.doFishing();
            }
        } else {
            this.makeFire();
        }

    }

    private void doFishing() {
        if (Npcs.get("Fishing spot").interact("Net")) {
            Time.waitInventoryChange();
        }

    }

    private Tile getNearestFireTile() {
        Tile nearest = null;

        for (int x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                Tile t = (new Tile(3102, 3095)).add(x, y);
                if (this.isReachable(t) && GameObjects.getAt(t).stream().noneMatch((o) -> {
                    return o.getName().contains("Fire");
                }) && (nearest == null || nearest.distance() > t.distance())) {
                    nearest = t;
                }
            }
        }

        return nearest;
    }

    private boolean isReachable(Tile tile) {
        return Player.getLocal().getPosition().getZ() == tile.getZ() && tile.distance() <= 30 && (new LocalPathFinder(false, true)).findPath(Player.getLocal().getPosition(), tile) != null;
    }
}
