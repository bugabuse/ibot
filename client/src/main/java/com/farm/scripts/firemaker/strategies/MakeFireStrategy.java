package com.farm.scripts.firemaker.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.WorldData;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.firemaker.Firemaker;
import com.farm.scripts.firemaker.FiremakerSettings;

import java.awt.*;

public class MakeFireStrategy extends Strategy implements MessageListener {
    public static Tile fireTile;

    public MakeFireStrategy() {
        Firemaker.get().addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Inventory.contains(FiremakerSettings.getLogToBurn()) && Inventory.contains(590)) {
            if (Widgets.closeTopInterface()) {

                if (fireTile == null) {
                    fireTile = this.getNearestFireTile();
                }

                if (fireTile == null) {
                    WebWalking.walkTo(new Tile(3172, 3496, 0), 0, new Tile[0]);
                } else {
                    if (fireTile != null && Time.sleep(() -> {
                        return Player.getLocal().getAnimation() == -1;
                    })) {
                        if (fireTile.getY() != Player.getLocal().getPosition().getY()) {
                            WebWalking.walkTo(fireTile, 0, new Tile[0]);
                            return;
                        }

                        if (!GameTab.INVENTORY.open()) {
                            return;
                        }

                        Point tinderbox = Random.human(Inventory.get("Tinderbox").getBounds());
                        Point log = Random.human(Inventory.get(FiremakerSettings.getLogToBurn()).getBounds());
                        if (Client.getSelectedItemId() != 590) {
                            Mouse.click(tinderbox.x, tinderbox.y);
                        }

                        Mouse.click(log.x, log.y);
                        if (Inventory.contains(FiremakerSettings.getLogToBurn())) {
                            tinderbox = Random.human(Inventory.get("Tinderbox").getBounds());
                            Mouse.click(tinderbox.x, tinderbox.y);
                        }

                        if (Time.waitInventoryChange()) {

                            Time.sleep(() -> {
                                return Player.getLocal().getWalkingQueueSize() > 0 || Dialogue.canClickContinue();
                            });
                            if (Dialogue.canClickContinue()) {
                                Dialogue.clickContinue();
                            }


                        }
                    }

                }
            }
        } else {

            fireTile = null;
        }
    }

    private Tile getNearestFireTile() {
        Tile nearest = null;
        Tile tile = new Tile(3172, 3496, 0);
        if (!tile.isReachable()) {
            WebWalking.walkTo(tile, 1, new Tile[0]);
            return null;
        } else {
            int y = 0;

            label27:
            while (y < 15) {
                for (int x = 0; x < 20; ++x) {
                    if (!this.canFire(tile.add(-x, y))) {
                        ++y;
                        continue label27;
                    }
                }

                nearest = tile.add(0, y);
                break;
            }

            return nearest;
        }
    }

    private boolean canFire(Tile tile) {
        if (tile.distance(new Tile(3162, 3489)) < 5) {
            return false;
        } else {
            return WorldData.getCollisionFlag(tile) == 0 && this.isReachable(tile) && GameObjects.getAt(tile).stream().noneMatch((o) -> {
                return o.getName().contains("Fire");
            });
        }
    }

    private boolean isReachable(Tile tile) {
        return Player.getLocal().getPosition().getZ() == tile.getZ() && tile.distance() <= 30 && tile.isReachable();
    }

    public void onMessage(String message) {
        if (message.contains("You can't light")) {
            fireTile = null;
        }

    }
}
