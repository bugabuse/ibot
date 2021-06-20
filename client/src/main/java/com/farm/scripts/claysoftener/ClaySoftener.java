package com.farm.scripts.claysoftener;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.scripts.claysoftener.strategies.grandexchange.GrandExchangeStrategy;

import java.awt.*;

public class ClaySoftener extends MultipleStrategyScript implements PaintHandler, InventoryListener, ScriptRuntimeInfo {
    private PaintTimer timer = new PaintTimer();
    private int softClayGained;

    public ClaySoftener() {
        super(Constants.DEFAULT);
    }

    public void onLoad() {
        this.getScriptHandler().loginRandom.autoWorldAssignEnabled = true;
    }

    public void onStartWhenLoggedIn() {
    }

    public void onStart() {

        WebWalking.enableRunningCondition = WebWalking.RUNNING_MANUAL;
        Constants.init(this);
        this.addEventHandler(new InventoryEventHandler(this));
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 1.21");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Soft clay: " + this.softClayGained + "(" + this.timer.getHourRatio(this.softClayGained) + ")");
        this.drawString(g, "State " + Constants.getCurrentState() + "(" + this.getCurrentlyExecuting() + ")");
        this.drawString(g, "Sell for: " + GrandExchangeStrategy.sellClayFor);
        this.drawString(g, "Buy for: " + GrandExchangeStrategy.buyClayFor);
    }

    public void onItemAdded(Item item) {
        if (item.getId() == 1761) {
            this.softClayGained += item.getAmount();
        }

    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>" + this.softClayGained + "(" + this.timer.getHourRatio(this.softClayGained) + ")</th><th>" + Constants.getCurrentState() + "(" + this.getCurrentlyExecuting() + ")</th>";
    }
}
