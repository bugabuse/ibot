package com.farm.scripts.cooker;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.PaintUtils;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.scripts.cooker.strategies.CookStrategy;

import java.awt.*;

public class Cooker extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();
    private SkillTracker tracker;

    public Cooker() {
        super(Strategies.DEFAULT);
    }

    public boolean breaksEnabled() {
        return false;
    }

    public void onStart() {

        Strategies.init(this);
    }

    public void onStartWhenLoggedIn() {
        this.tracker = new SkillTracker(Skill.COOKING);
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Cooker Version 0.09");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
        this.drawString(g, "Experience: " + this.tracker.getExpGained() + " (" + this.tracker.getHourRatio() / 1000 + "k)");
        if (CookStrategy.fireTile != null) {
            PaintUtils.drawTile(g, CookStrategy.fireTile);
        }

    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Cooking: " + Skill.COOKING.getRealLevel() + "</th><th>" + this.getCurrentlyExecuting() + "</th>";
    }
}
