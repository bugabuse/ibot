package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.zulrahkiller.api.Zulrah;

public class WalkStrategy extends Strategy {
    public static boolean isAlternateTile = false;

    public static boolean isWalking() {
        if (Zulrah.getCurrentRotationStrategy().getWaitBeforeWalkingTime() > 1) {
            if (ListenerStrategy.currentInstanceTimer.getElapsed() > (long) Zulrah.getCurrentRotationStrategy().getWaitBeforeWalkingTime()) {
                return getWalkingTile().distance() > Zulrah.getCurrentRotationStrategy().getAllowedDistance();
            } else {
                return false;
            }
        } else {
            return getWalkingTile().distance() > Zulrah.getCurrentRotationStrategy().getAllowedDistance();
        }
    }

    public static Tile getWalkingTile() {
        return isAlternateTile && Zulrah.getCurrentRotationStrategy().getAlternateTile() != null ? Zulrah.getCurrentRotationStrategy().getAlternateTile() : Zulrah.getCurrentRotationStrategy().getTile();
    }

    public boolean active() {
        return Zulrah.get() != null;
    }

    public boolean isBackground() {
        return true;
    }

    protected void onAction() {
        if (Zulrah.getCurrentRotationStrategy().getWaitBeforeWalkingTime() > 1) {
            if (ListenerStrategy.currentInstanceTimer.getElapsed() > (long) Zulrah.getCurrentRotationStrategy().getWaitBeforeWalkingTime()) {
                Debug.log("WALK > " + Zulrah.isDying() + " > " + ListenerStrategy.currentInstanceTimer.getElapsed());
                this.walk();
            }
        } else {
            this.walk();
        }

    }

    private void walk() {
        if (getWalkingTile().distance() > Zulrah.getCurrentRotationStrategy().getAllowedDistance()) {
            Walking.walkTo(getWalkingTile(), 0);
        }

    }
}
