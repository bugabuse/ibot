package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.zulrahkiller.api.Zulrah;
import com.farm.scripts.zulrahkiller.api.ZulrahRotation;
import com.google.common.base.Objects;

public class ListenerStrategy extends Strategy {
    public static PaintTimer currentInstanceTimer = new PaintTimer();
    private Zulrah.Color lastZulrah = null;
    private Tile lastZulrahPosition = null;
    private int lastAnimation = 0;

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Zulrah.isSwitching()) {
            currentInstanceTimer.reset();
        }

        if (this.lastZulrah != Zulrah.getColor() || !Objects.equal(this.lastZulrahPosition, Zulrah.getPosition())) {
            this.onZulrahChanged();
            this.lastZulrah = Zulrah.getColor();
            this.lastZulrahPosition = Zulrah.getPosition();
        }

        Npc zulrah = Zulrah.get();
        if (zulrah != null && this.lastAnimation != zulrah.getAnimation()) {
            this.onAnimationChanged(zulrah.getAnimation());
            this.lastAnimation = zulrah.getAnimation();
        }

    }

    public boolean isBackground() {
        return true;
    }

    public void onAnimationChanged(int animation) {
        if (animation == 5806 || animation == 5807) {
            WalkStrategy.isAlternateTile = !WalkStrategy.isAlternateTile;
        }

    }

    public void onZulrahChanged() {
        this.lastAnimation = -1;
        currentInstanceTimer.reset();
        Debug.log("Zulrah changed: " + Zulrah.getColor());
        if (Zulrah.hasWaveEnd()) {
            Zulrah.getColorHistory().clear();
        }

        if (Zulrah.getColor() == Zulrah.Color.NONE) {
            Zulrah.getColorHistory().clear();
        } else {
            ZulrahRotation str = new ZulrahRotation(Zulrah.getColor(), (Tile) null, Zulrah.getPosition().toLocalTile());
            Zulrah.getColorHistory().add(str);
        }

    }
}
