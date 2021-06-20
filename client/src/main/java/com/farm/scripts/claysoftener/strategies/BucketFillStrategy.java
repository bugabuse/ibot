package com.farm.scripts.claysoftener.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.claysoftener.Constants;
import com.farm.scripts.winefiller.strategies.JugFillStrategy;

public class BucketFillStrategy extends Strategy {
    private static PaintTimer animTimer = new PaintTimer();

    public static boolean isFilling() {
        if (Player.getLocal().getAnimation() == 832) {
            animTimer.reset();
        }

        return Inventory.get(1925) != null && animTimer.getElapsed() < 5500L;
    }

    public boolean active() {
        return Constants.currentState == 0 && !SoftenClayStrategy.isSoftening() && Inventory.get(1925) != null && Inventory.container().getCount(new int[]{1925, 1929}) >= 14 && (Locations.getClosestBank().distance() > Constants.TILE_FOUNTAIN.distance() || Inventory.get(434) != null);
    }

    public void onAction() {

        Walking.setRun(true);
        if (!isFilling() && WebWalking.walkTo(Constants.TILE_FOUNTAIN, 2, new Tile[0]) && Inventory.get(1925).interactWith(GameObjects.get("Fountain"))) {
            Time.sleep(JugFillStrategy::isFilling);
        }

    }
}
