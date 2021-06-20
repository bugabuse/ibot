package com.farm.scripts.cooker.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.InputBox.MakeItemDialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.pathfinding.impl.LocalPathFinder;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.cooker.CookerSettings;

public class CookStrategy extends Strategy {
    public static Tile fireTile = null;
    private PaintTimer animationTimer = new PaintTimer();

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Inventory.contains(CookerSettings.getFishToCook()) && Inventory.contains(590)) {
            if (!Locations.GRAND_EXCHANGE.isReachable()) {
                WebWalking.walkTo(Locations.GRAND_EXCHANGE, 1, new Tile[0]);
            } else {
                if (Player.getLocal().getAnimation() == 897) {
                    this.animationTimer.reset();
                }

                if (this.animationTimer.getElapsed() > 2000L) {
                    if (fireTile == null) {
                        fireTile = this.getNearestFireTile();
                    } else {
                        GameObject fire = (GameObject) GameObjects.getAt(fireTile).stream().filter((o) -> {
                            return o.getName().contains("Fire");
                        }).findAny().orElse(null);
                        if (fire != null) {
                            Inventory.get(CookerSettings.getFishToCook()).interactWith(fire);
                            if (Time.sleep(InputBox::isMakeItemDialogueOpen)) {
                                MakeItemDialogue.MAKE_ALL.selectAndExecute();
                                Time.sleep(1500, 3000);
                            }
                        } else {
                            if (!Inventory.contains(CookerSettings.getLogToBurn())) {
                                Bank.openAndWithdraw(new Item[]{new Item(CookerSettings.getLogToBurn(), 1)});
                                return;
                            }

                            if (Inventory.getCount(CookerSettings.getLogToBurn()) > 1) {
                                if (Bank.open()) {
                                    Bank.depositAll();
                                }

                                return;
                            }

                            if (!WebWalking.walkTo(fireTile, 0, new Tile[0])) {
                                return;
                            }

                            if (Inventory.get("Tinderbox").interactWith(Inventory.get(CookerSettings.getLogToBurn())) && Time.waitInventoryChange()) {
                                Time.sleep(() -> {
                                    return GameObjects.getAt(fireTile).stream().anyMatch((o) -> {
                                        return o.getName().contains("Fire");
                                    });
                                });
                            }
                        }

                    }
                }
            }
        }
    }

    private Tile getNearestFireTile() {
        Tile nearest = null;

        for (int x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                Tile t = (new Tile(3161, 3489, 0)).add(x, y);
                if ((GameObjects.get(10059, t) == null || GameObjects.get(10059, t).getPosition().distance(t) > 2) && this.isReachable(t) && GameObjects.getAt(t).stream().noneMatch((o) -> {
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
