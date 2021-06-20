package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.util.PaintUtils;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.zulrahkiller.api.Zulrah;

import java.awt.*;

public class AttackStrategy extends Strategy implements PaintHandler {
    private final BotScript script;

    public AttackStrategy(BotScript script) {
        this.script = script;
        script.getPaintHandlers().add(this);
    }

    public boolean active() {
        return Zulrah.get() != null;
    }

    public void onAction() {
        if (!WalkStrategy.isWalking()) {
            if (!Zulrah.getCurrentRotationStrategy().isIdleMode()) {
                if (Player.getLocal().getInteractingIndex() != Zulrah.get().getIndex()) {
                    Zulrah.get().interact("Attack");
                    this.sleep(600, 800);
                }

            }
        }
    }

    public void onPaint(Graphics g) {
        Npc zulrah = Zulrah.get();
        if (Zulrah.get() != null) {
            g.setColor(Color.white);
            this.script.drawString(g, "");
            this.script.drawString(g, "Current instance: " + ListenerStrategy.currentInstanceTimer.getElapsed());
            this.script.drawString(g, "");
            this.script.drawString(g, "Venom  " + HealStrategy.isVenomActive());
            this.script.drawString(g, "Color:  " + Zulrah.getCurrentRotationStrategy().getColor());
            this.script.drawString(g, "Pattern: " + Zulrah.getPattern());
            this.script.drawString(g, "Count: " + Zulrah.getColorHistory().size());
            this.script.drawString(g, "");
            this.script.drawString(g, "Animation: " + zulrah.getAnimation());
            this.script.drawString(g, "Switching: " + Zulrah.isSwitching());
            this.script.drawString(g, "Orientation: " + zulrah.getOrientation());
            g.setColor(Color.green);
            PaintUtils.drawTile(g, Zulrah.getCurrentRotationStrategy().getTile(), "Current");
            if (Zulrah.getCurrentRotationStrategy().getAlternateTile() != null) {
                g.setColor(Color.orange);
                PaintUtils.drawTile(g, Zulrah.getCurrentRotationStrategy().getAlternateTile(), "Alternate");
            }

        }
    }
}
