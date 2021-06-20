package com.farm.scripts.magictrainer;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.core.script.MultipleStrategyScript;

import java.awt.*;

public class MagicTrainer extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();
    public SkillTracker tracker;

    public MagicTrainer() {
        super(Strategies.DEFAULT);
        this.tracker = new SkillTracker(Skill.MAGIC);
    }

    public void onStart() {

        Strategies.init(this);
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Magic Version 0.01");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, this.tracker.getPaintString());
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Magic level: " + Skill.MAGIC.getRealLevel() + "</th>";
    }
}
