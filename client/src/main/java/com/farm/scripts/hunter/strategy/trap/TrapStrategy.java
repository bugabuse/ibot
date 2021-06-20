package com.farm.scripts.hunter.strategy.trap;

import com.farm.ibot.api.accessors.GroundItem;
import com.farm.ibot.api.accessors.GroundItems;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.hunter.HunterConfig;
import com.farm.scripts.hunter.trap.Trap;
import com.farm.scripts.hunter.trap.TrapObject;
import com.farm.scripts.hunter.trap.TrapTile;

import java.util.ArrayList;
import java.util.Iterator;

public class TrapStrategy extends Strategy {
    private TrapTile toFix;

    public boolean active() {
        return true;
    }

    public void onAction() {
        if (WebWalking.walkTo(HunterConfig.currentSpot.getRoot(), 8, new Tile[0])) {
            if (this.toFix == null || this.toFix.getState() == Trap.State.IDLE) {
                this.toFix = this.getTrapToFix();
            }

            if (Player.getLocal().getAnimation() != -1 || !this.pickItem()) {
                if (this.toFix == null) {
                    this.idle();
                } else {
                    switch (this.toFix.getState()) {
                        case BROKEN:
                            this.doAction(this.toFix, "Dismantle");
                            break;
                        case CATCHED:
                            this.doAction(this.toFix, "Check");
                            break;
                        case EMPTY:
                            this.createTrap(this.toFix);
                            break;
                        case BROKEN_GROUND_ITEM:
                            this.pickItem();
                    }

                }
            }
        }
    }

    private void idle() {
        int x = 0;
        int y = 0;
        int i = 0;
        TrapTile[] var4 = HunterConfig.currentSpot.getTiles();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Tile tile = var4[var6];
            if (!this.canLayTrap(i)) {
                break;
            }

            x += tile.getX();
            y += tile.getY();
            ++i;
        }

        if (i >= 2) {
            Tile center = new Tile(x / i, y / i);
            Walking.walkTo(center, 0);
        }

    }

    private TrapTile getTrapToFix() {
        ArrayList<TrapTile> available = new ArrayList();

        int lastPriority;
        TrapTile nearest;
        for (lastPriority = 0; this.canLayTrap(lastPriority) && lastPriority < HunterConfig.currentSpot.getTiles().length; ++lastPriority) {
            nearest = HunterConfig.currentSpot.getTiles()[lastPriority];
            if (nearest.getState() != Trap.State.IDLE) {
                available.add(nearest);
            }
        }

        lastPriority = -1;
        nearest = null;
        Iterator var4 = available.iterator();

        while (var4.hasNext()) {
            TrapTile tile = (TrapTile) var4.next();
            if (tile.getPriority() > lastPriority) {
                lastPriority = tile.getPriority();
                nearest = tile;
            }
        }

        return nearest;
    }

    public boolean canLayTrap(int i) {
        return HunterConfig.TRAP_COUNT_LEVEL_REQUIREMENTS.length > i && Skill.HUNTER.getRealLevel() >= HunterConfig.TRAP_COUNT_LEVEL_REQUIREMENTS[i];
    }

    private boolean pickItem() {
        if (Inventory.getFreeSlots() > 1) {
            GroundItem item = GroundItems.get(HunterConfig.currentSpot.getRoot().getTrap().getItemId());
            if (item != null && item.getPosition().distance(HunterConfig.currentSpot.getRoot()) < 7) {
                return item.interact("Take") && Time.waitInventoryChange();
            }
        }

        return false;
    }

    private void doAction(TrapTile trap, String action) {
        GameObject object = TrapObject.create(GameObjects.getTopAt(trap));
        int walkDist = 1;
        if (trap.distance() > 1) {
            walkDist = 0;
        }

        if (!Walking.walkTo(trap, walkDist)) {
            Time.sleep(() -> {
                return Player.getLocal().getPosition().equals(trap);
            });
        } else {
            if (object != null && object.interact(action)) {
                Trap.State state = trap.getState();
                if (Time.waitInventoryChange()) {
                    Time.sleep(() -> {
                        return trap.getState() != state;
                    });
                }
            }

        }
    }

    private void createTrap(TrapTile trap) {
        if (!Walking.walkTo(trap, 0)) {
            Time.sleep(() -> {
                return Player.getLocal().getPosition().equals(trap);
            });
        } else {
            Item item = Inventory.get(trap.getItemId());
            if (item != null && item.interact("Lay")) {
                Time.sleep(() -> {
                    return trap.getState() == Trap.State.IDLE;
                });
            }

        }
    }
}
