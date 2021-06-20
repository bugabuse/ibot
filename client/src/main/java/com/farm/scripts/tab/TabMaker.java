package com.farm.scripts.tab;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.manifest.ScriptManifest;
import com.farm.scripts.tab.strategies.HouseOwnersSearcher;
import com.farm.scripts.tab.strategies.UnnoteStrategy;

import java.awt.*;

@ScriptManifest(
        isP2p = true
)
public class TabMaker extends MultipleStrategyScript implements PaintHandler, InventoryListener {
    public PaintTimer timer = new PaintTimer();
    public int tabsMade = 0;

    public TabMaker() {
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
        this.drawString(g, "Tabs made; " + this.tabsMade + "(" + this.timer.getHourRatio(this.tabsMade) + ")");
        this.drawString(g, "State: " + this.getCurrentlyExecuting());
        this.drawString(g, "At home: " + UnnoteStrategy.isAtHome());
        this.drawString(g, "Current House owner: " + HouseOwnersSearcher.currentHouse);
        this.drawString(g, "Available houses: " + HouseOwnersSearcher.availableHouses);
        this.drawString(g, "Banned houses: " + HouseOwnersSearcher.bannedHouses.keySet());
    }

    public void onItemAdded(Item item) {
        if (item.getName().toLowerCase().contains("teleport")) {
            ++this.tabsMade;
        }

    }
}
