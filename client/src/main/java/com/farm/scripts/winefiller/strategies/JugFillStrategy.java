package com.farm.scripts.winefiller.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.winefiller.Strategies;
import com.farm.scripts.winefiller.WineFiller;

public class JugFillStrategy extends Strategy {
    private static long lastAnim = 0L;

    public static boolean isFilling() {
        if (Player.getLocal().getAnimation() == 832) {
            lastAnim = System.currentTimeMillis();
        }

        return System.currentTimeMillis() - lastAnim < 1600L;
    }

    public boolean active() {
        return WineFiller.currentState == WineFiller.NORMAL && Inventory.get(Strategies.JUG_EMPTY) != null;
    }

    public void onAction() {

        if (!isFilling()) {
            GameObject gameObject = GameObjects.get(Strategies.getSpot().fountainName, Strategies.getSpot().tile);
            if (gameObject != null && gameObject.getPosition().distance(Strategies.getSpot().tile) < 6 && gameObject.isReachable()) {
                if (Inventory.get(Strategies.JUG_EMPTY).interactWith(GameObjects.get(Strategies.getSpot().fountainName, Strategies.getSpot().tile)) && Time.sleep(() -> {
                    return Player.getLocal().isMoving();
                })) {
                    Time.sleep(20000, () -> {
                        return isFilling() || !Player.getLocal().isMoving();
                    });
                }
            } else {
                if (Client.getRunEnergy() > 20) {
                    Walking.setRun(true);
                }

                WebWalking.walkTo(Strategies.getSpot().tile, 0, new Tile[0]);
            }

        }
    }
}
