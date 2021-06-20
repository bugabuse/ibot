package com.farm.scripts.flaxspinner.strategies.flax;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.InputBox.MakeItemDialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.flaxspinner.FlaxSpinner;

public class SpinStrategy extends Strategy implements InventoryListener {
    public static final Tile SPIN_SPOT = new Tile(3209, 3213, 1);
    public static boolean isIdling = false;
    private static long lastAnimTime = 0L;

    public SpinStrategy() {
        FlaxSpinner.get().addEventHandler(new InventoryEventHandler(this));
    }

    public static boolean isSpinning() {
        if (Player.getLocal().getAnimation() != -1) {
            lastAnimTime = System.currentTimeMillis();
        }

        return System.currentTimeMillis() - lastAnimTime < 5500L;
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (isIdling && !Inventory.contains(1779)) {
            isIdling = false;
        }

        if (Inventory.contains(1779)) {
            GameTab.INVENTORY.open();
            if (!isSpinning() && Client.getRunEnergy() > 10) {
                Walking.setRun(true);
            }

            if (SPIN_SPOT.isReachable() || WebWalking.walkTo(SPIN_SPOT, 1, new Tile[0])) {
                if (!isSpinning()) {
                    if (!InputBox.isMakeItemDialogueOpen()) {
                        GameObject spinningWheel = GameObjects.get("Spinning wheel");
                        if (spinningWheel.interact("Spin")) {
                            Time.sleep(5000, 50, () -> {
                                if (spinningWheel.getPosition().distance() <= 2) {
                                    Keyboard.type(51);
                                }

                                return InputBox.isMakeItemDialogueOpen();
                            });
                        }
                    } else {
                        MakeItemDialogue.MAKE_ALL.selectAndExecute(3);
                    }
                }

            }
        }
    }

    public void onItemAdded(Item item) {
        if (isSpinning() && !Inventory.contains(1779)) {
            isIdling = true;
            ScriptUtils.interruptCurrentLoop();
        }

    }

    public void onItemRemoved(Item item) {
    }
}
