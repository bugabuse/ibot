package com.farm.scripts.saltpetre.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;

public class DigStrategy extends Strategy {
    public static final Tile SALTPETRE_CENTER = new Tile(1694, 3533, 0);
    private PaintTimer animTimer = new PaintTimer(0L);

    public boolean active() {
        this.isFilling();
        return AccountData.current().isMembers && !Inventory.isFull() && Inventory.contains(952);
    }

    protected void onAction() {
        if (WorldHopping.isF2p(Client.getCurrentWorld())) {
            if (WorldHopping.hop(WorldHopping.getRandomP2p())) {
                Time.sleep(3000, () -> {
                    return GameObjects.get("Saltpetre") != null;
                });
            }

        } else {
            GameObject saltpetre = GameObjects.get("Saltpetre");
            if (saltpetre == null) {
                Walking.setRun(true);
                if (WebWalking.walkTo(SALTPETRE_CENTER, new Tile[0]) && WorldHopping.hop(WorldHopping.getRandomP2p())) {
                    Time.sleep(3000, () -> {
                        return GameObjects.get("Saltpetre") != null;
                    });
                }

            } else if (saltpetre.getPosition().distance() > 5) {
                WebWalking.walkTo(saltpetre.getPosition(), 5, new Tile[0]);
            } else {
                if (!this.isFilling() && saltpetre.interact("Dig")) {
                    Time.sleep(4000, this::isFilling);
                }

            }
        }
    }

    public boolean isFilling() {
        if (Player.getLocal().getAnimation() != -1) {
            this.animTimer.reset();
        }

        return this.animTimer.getElapsed() < 5500L;
    }
}
