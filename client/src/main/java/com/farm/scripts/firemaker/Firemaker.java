package com.farm.scripts.firemaker;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.PaintUtils;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.init.Settings;
import com.farm.scripts.firemaker.strategies.MakeFireStrategy;

import java.awt.*;

public class Firemaker extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, InventoryListener {
    public PaintTimer timer = new PaintTimer();
    SkillTracker tracker;
    private int logs;

    public Firemaker() {
        super(Strategies.DEFAULT);
        this.addEventHandler(new InventoryEventHandler(this));
    }

    public boolean breaksEnabled() {
        return false;
    }

    public void onStartWhenLoggedIn() {
        System.out.println("Firemaker onstart");
        this.timer.reset();
        this.tracker = new SkillTracker(Skill.FIREMAKING);
    }

    public void onStart() {

        Strategies.init(this);
        this.getScriptHandler().antiKick.active = false;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Firemaker Version 0.15");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
        this.drawString(g, "Logs: " + this.logs + "(" + this.timer.getHourRatio(this.logs) + ")");
        this.drawString(g, "Exp: " + this.tracker.getExpGained() + "(" + this.tracker.getHourRatio() / 1000 + "K)");
        Settings.useLongSleep = false;
        PaintUtils.drawTile(g, MakeFireStrategy.fireTile);
    }

    public int loopInterval() {
        return Random.human(10, 20);
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Firemaking: " + Skill.FIREMAKING.getRealLevel() + "</th><th>" + this.getCurrentlyExecuting() + "</th>";
    }

    public void onItemAdded(Item item) {
    }

    public void onItemRemoved(Item item) {
        if (item.getName().toLowerCase().contains("log")) {
            ++this.logs;
        }

    }
}
