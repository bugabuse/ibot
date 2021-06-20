package com.farm.scripts.bonespicker;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.PlayerSpotInfo;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;

import java.awt.*;

public class BonesPicker extends MultipleStrategyScript implements PaintHandler, InventoryListener, ScriptRuntimeInfo, PlayerSpotInfo {
    public PaintTimer timer = new PaintTimer();
    public int itemsGained = 0;

    public BonesPicker() {
        super(Strategies.DEFAULT);
    }

    public void onStart() {
        Strategies.init(this);
        this.addEventHandler(new InventoryEventHandler(this));
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.16");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Items: " + this.itemsGained + "(" + this.timer.getHourRatio(this.itemsGained) + ")");
        this.drawString(g, "Strategy: " + this.getCurrentlyExecuting());
        this.drawString(g, "Trade action: " + Strategies.muleManager.tradeAction);
        this.drawString(g, "Trade state: " + Strategies.muleManager.getCurrentState());
    }

    public void onItemAdded(Item item) {
        if (!Bank.isOpen() && !item.getDefinition().isNoted() && (item.getName().contains("Cowhide") || item.getName().contains("beef") || item.getName().contains("Bones"))) {
            if (this.itemsGained == 0) {
                this.timer.reset();
            }

            this.itemsGained += item.getAmount();
        }

    }

    public String runtimeInfo() {
        return "<th>" + this.timer.getElapsedString() + "</th><th>Bones: " + this.itemsGained + "(" + this.timer.getHourRatio(this.itemsGained) + ")</th><th>" + Strategies.COW_SPOT.tile.getNote() + "</th><th>" + this.getCurrentlyExecuting() + "</th>";
    }

    public int maxSpotCount() {
        return Strategies.COW_SPOTS.length;
    }
}
