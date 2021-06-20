package com.farm.scripts.prayer;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.StrategyScript;
import com.farm.scripts.prayer.strategies.BankStrategy;
import com.farm.scripts.prayer.strategies.BuryStrategy;

import java.awt.*;

public class Prayer extends StrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();
    SkillTracker tracker;

    public Prayer() {
        super(new Strategy[0]);
    }

    public void onStartWhenLoggedIn() {
        this.tracker = new SkillTracker(Skill.PRAYER);
        WebWalking.enableRunningCondition = WebWalking.RUNNING_ALWAYS_ON;
    }

    public void onStart() {

        this.addStrategy(new Strategy[]{new BuryStrategy()});
        this.addStrategy(new Strategy[]{new BankStrategy()});
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Prayer Version 0.01");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Prayer: " + this.tracker.getExpGained() + "(" + this.tracker.getHourRatio() + ")");
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Combat level: " + Player.getLocal().getCombatLevel() + "</th>";
    }
}
