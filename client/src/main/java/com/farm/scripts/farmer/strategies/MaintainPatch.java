package com.farm.scripts.farmer.strategies;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmer.Strategies;
import com.farm.scripts.farmer.api.FarmingPatch;
import com.farm.scripts.farmer.api.PatchState;
import com.google.common.primitives.Ints;

public class MaintainPatch extends Strategy implements MessageListener {
    private static final int[] PATCH_IDS = new int[]{8152, 8151, 8150};
    public static boolean usedCompost = false;

    public MaintainPatch(MultipleStrategyScript script) {
        script.addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        FarmingPatch patch = FarmingPatch.current();
        if (patch == null) {

        } else if (PatchState.get() != PatchState.GROWING && !Inventory.isFull() || this.notedHerbs()) {
            switch (PatchState.get()) {
                case GROWING:
                default:
                    break;
                case GROWN:
                    if (Inventory.container().getFreeSlots() == 0) {
                        Inventory.get(21483).interact("Drop");
                        Time.waitInventoryChange();
                        return;
                    }

                    this.workOnPatch("Pick");
                    break;
                case DEAD:
                    this.workOnPatch("Clear");
                    break;
                case DISEASED:
                    this.workOnPatch("Cure");
                    break;
                case NEED_TO_RAKE:
                    this.workOnPatch("Rake");
                    break;
                case SEEDABLE:
                    this.putSeed();
            }

            this.sleep(10);
        }
    }

    private boolean notedHerbs() {
        if (PatchState.get() == PatchState.GROWN && Inventory.getFreeSlots() > 0) {
            return true;
        } else {
            Item herbUnnoted = Inventory.get(Strategies.herbToFarm.grimyWeedId);
            if (herbUnnoted != null) {
                Npc npc = Npcs.get("Tool leprechaun");
                if (npc != null) {
                    herbUnnoted.interactWith(npc);
                    Time.waitInventoryChange();
                }

                return false;
            } else {
                return true;
            }
        }
    }

    private void putSeed() {
        if (usedCompost) {
            this.interactWithItem(Strategies.herbToFarm.seedId);
            usedCompost = false;
        } else {
            this.interactWithItem(21483);
        }

    }

    private void interactWithItem(int itemId) {
        Item item = Inventory.container().get(itemId);
        if (item != null) {
            item.interactWith(GameObjects.get((o) -> {
                return Ints.contains(PATCH_IDS, o.getId());
            }));
            this.waitStateChange();
        }

    }

    private void workOnPatch(String action) {
        GameObject go = GameObjects.get((o) -> {
            return Ints.contains(PATCH_IDS, o.getId());
        });
        if (go != null) {
            go.interact(action);
            this.waitStateChange();
        }

    }

    private void waitStateChange() {
        PatchState current = PatchState.get();
        Time.sleep(() -> {
            return Player.getLocal().getAnimation() != -1;
        });
        Time.sleep(() -> {
            return PatchState.get() != current;
        });
    }

    public void onMessage(String message) {
        if (message.contains("You treat the herb") || message.contains("herb patch has already been")) {
            usedCompost = true;
        }

    }
}
