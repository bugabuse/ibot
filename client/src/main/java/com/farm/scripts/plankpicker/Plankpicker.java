package com.farm.scripts.plankpicker;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;

import java.awt.*;

public class Plankpicker extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, InventoryListener {
    public PaintTimer timer = new PaintTimer();
    private int items = 0;

    public Plankpicker() {
        super(Strategies.DEFAULT);
    }

    public void onStart() {

        Strategies.init(this);
        this.addEventHandler(new InventoryEventHandler(this));
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Plankpicker Version 0.01");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "State: " + this.getCurrentlyExecuting());
        this.drawString(g, "Planks: " + this.items + "(" + this.timer.getHourRatio(this.items) + ")");
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Planks(reg): " + this.items + "(" + this.timer.getHourRatio(this.items) + ")</th>";
    }

    public void onItemAdded(Item item) {
        if (item.getId() == 960) {
            this.items += item.getAmount();
        }

    }
}
