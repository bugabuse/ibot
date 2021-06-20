package com.farm.scripts.ringenchancer;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.manifest.ScriptManifest;

import java.awt.*;

@ScriptManifest(
        isP2p = true
)
public class RingEnchacer extends MultipleStrategyScript implements PaintHandler, InventoryListener {
    public PaintTimer timer = new PaintTimer();
    public int enchants = 0;

    public RingEnchacer() {
        super(Strategies.DEFAULT);
    }

    public void onLoad() {
        this.addEventHandler(new InventoryEventHandler(this));
    }

    public void onStart() {
        Strategies.init(this);
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.01");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Rings enchanted: " + this.enchants + "(" + this.timer.getHourRatio(this.enchants) + ")");
        this.drawString(g, "State: " + this.getCurrentlyExecuting());
    }

    public void onItemAdded(Item item) {
        if (item.getName().contains("Ring of recoil")) {
            ++this.enchants;
        }

    }
}
