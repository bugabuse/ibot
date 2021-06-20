package com.farm.scripts.fletcher;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.core.script.MultipleStrategyScript;

import java.awt.*;

public class Fletching extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();
    private SkillTracker tracker;
    private int items;

    public Fletching() {
        super(Strategies.DEFAULT);
        this.tracker = new SkillTracker(Skill.FLETCHING);
    }

    public void onStart() {

        Strategies.init(this);
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Fletcher Version 0.24");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, this.tracker.getPaintString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Fletching: " + Skill.FLETCHING.getRealLevel() + "</th><th>Bows: " + this.getItemsGained() + "(" + this.getItemsPerHour() + ")</th><th>" + Constants.getRequiredItems().getName() + "</th><th>" + this.getCurrentlyExecuting() + "</th>";
    }

    public int loopInterval() {
        return 100;
    }

    private int getItemsGained() {
        return this.tracker.getExpGained() / 58;
    }

    private int getItemsPerHour() {
        return this.tracker.getHourRatio() / 58;
    }
}
