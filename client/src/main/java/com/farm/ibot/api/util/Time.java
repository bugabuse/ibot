package com.farm.ibot.api.util;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.Bot;

import java.util.Arrays;

public class Time {
    public static final Condition INVENTORY_CHANGED = new Condition() {
        Item[] before = null;

        public void beforeCheck() {
            this.before = Inventory.getAll();
        }

        public boolean active() {
            Item[] current = Inventory.getAll();
            if (this.before.length != current.length) {
                return true;
            } else {
                int i = 0;

                while (true) {
                    if (i < this.before.length && i < current.length) {
                        if (this.before[i].getId() == current[i].getId() && this.before[i].getAmount() == current[i].getAmount()) {
                            ++i;
                            continue;
                        }

                        return true;
                    }

                    return false;
                }
            }
        }
    };

    public static boolean sleep(Condition condition) {
        return sleep(6500, condition);
    }

    public static boolean sleep(int durationMax, Condition condition) {
        return sleep(durationMax, 20, condition);
    }

    public static boolean sleep(int durationMax, int checkInterval, Condition condition) {
        long maxTime = System.currentTimeMillis() + (long) durationMax;
        condition.beforeCheck();

        while (System.currentTimeMillis() < maxTime && !condition.active()) {
            sleep(checkInterval);
        }

        return condition.active();
    }

    public static boolean sleepAny(int durationMax, Condition... conditions) {
        long maxTime = System.currentTimeMillis() + (long) durationMax;
        Arrays.stream(conditions).forEach(Condition::beforeCheck);

        while (System.currentTimeMillis() < maxTime && !Arrays.stream(conditions).anyMatch(Condition::active)) {
            sleep(5);
        }

        return Arrays.stream(conditions).anyMatch(Condition::active);
    }

    public static boolean sleepHuman(Condition condition) {
        return sleepHuman(6500, condition);
    }

    public static boolean sleepHuman(int durationMax, Condition condition) {
        long maxTime = System.currentTimeMillis() + (long) durationMax;

        while (System.currentTimeMillis() < maxTime && !condition.active()) {
            sleep(100);
        }

        sleep(50, 150);
        return condition.active();
    }

    public static void sleep(int durationMin, int durationMax) {
        sleep(Random.next(durationMin, durationMax));
    }

    public static void sleep(long durationMin, long durationMax) {
        sleep(Random.human(durationMin, durationMax));
    }

    public static void sleep(int duration) {
        try {
            Thread.sleep((long) duration);
        } catch (InterruptedException var2) {
        }

    }

    public static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException var3) {
        }

    }

    public static boolean waitInventoryChange() {
        return waitInventoryChange(5000);
    }

    public static boolean waitInventoryChange(int maxTime) {
        return sleep(maxTime, INVENTORY_CHANGED);
    }

    public static boolean waitObjectDissapear(GameObject object) {
        return sleep(() -> {
            return !object.exists();
        });
    }

    public static boolean waitNextGameCycle() {
        long cycle = Bot.get().getCanvasHandler().currentCycle;
        return sleep(() -> {
            return Bot.get().getCanvasHandler().currentCycle > cycle;
        });
    }

    public static boolean waitRegionChange() {
        Tile lastBaseLocation = new Tile(Client.getBaseX(), Client.getBaseY());
        Tile lastPlayerTile = Player.getLocal().getPosition();
        Debug.log("OUR TILE " + Player.getLocal().getPosition());
        if (sleep(30000, () -> {
            return Client.getLoginState() == 30 && (new Tile(Client.getBaseX(), Client.getBaseY())).distance(lastBaseLocation) > 1;
        })) {
            Debug.log("2 OUR TILE " + Player.getLocal().getPosition());
            sleep(30000, () -> {
                return lastPlayerTile.distance() > 1;
            });
            Debug.log("3 OUR TILE " + Player.getLocal().getPosition());
            return true;
        } else {
            return false;
        }
    }

    public static void sleep(double time) {
        sleep((long) time);
    }
}
