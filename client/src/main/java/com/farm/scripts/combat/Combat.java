package com.farm.scripts.combat;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.api.world.area.PolygonArea;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.MultipleStrategyScript;

import java.awt.*;

public class Combat extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public static int maxCombat = 13;
    public static BotScript instance;
    public PaintTimer timer = new PaintTimer();
    SkillTracker trackerAttack;
    SkillTracker trackerDefence;
    SkillTracker trackerStrength;
    SkillTracker trackerMagic;
    SkillTracker trackerPrayer;

    public Combat() {
        super(Strategies.DEFAULT);
        this.trackerAttack = new SkillTracker(Skill.ATTACK);
        this.trackerDefence = new SkillTracker(Skill.DEFENSE);
        this.trackerStrength = new SkillTracker(Skill.STRENGTH);
        this.trackerMagic = new SkillTracker(Skill.MAGIC);
        this.trackerPrayer = new SkillTracker(Skill.PRAYER);
    }

    public Combat(boolean forWoodcutting) {
        super(Strategies.DEFAULT);
        this.trackerAttack = new SkillTracker(Skill.ATTACK);
        this.trackerDefence = new SkillTracker(Skill.DEFENSE);
        this.trackerStrength = new SkillTracker(Skill.STRENGTH);
        this.trackerMagic = new SkillTracker(Skill.MAGIC);
        this.trackerPrayer = new SkillTracker(Skill.PRAYER);
        if (forWoodcutting) {
            this.startArguments = this.startArguments + "woodcutting";
        }

    }

    public void onStartWhenLoggedIn() {
        WebWalking.enableRunningCondition = WebWalking.RUNNING_ALWAYS_ON;
    }

    public void onStart() {

        this.getScriptHandler().webNotFoundRandom.enabled = false;
        this.getScriptHandler().webNotFoundRandom.active = false;
        Strategies.init(this);
        instance = this;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Combat Version 0.34");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
        this.drawString(g, this.trackerAttack.getPaintString());
        this.drawString(g, this.trackerDefence.getPaintString());
        this.drawString(g, this.trackerStrength.getPaintString());
        this.drawString(g, this.trackerMagic.getPaintString());
        this.drawString(g, this.trackerPrayer.getPaintString());
        PolygonArea area = (PolygonArea) Settings.SPOT_HOBGOBLIN.area;
        Polygon newPoly = new Polygon();

        for (int i = 0; i < area.polygon.npoints; ++i) {
            Tile t = new Tile(area.polygon.xpoints[i], area.polygon.ypoints[i]);
            newPoly.addPoint((int) t.getMinimapPoint().getX(), (int) t.getMinimapPoint().getY());
        }

        g.drawPolygon(newPoly);
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Combat level: " + Player.getLocal().getCombatLevel() + "</th>";
    }

    public Combat withArguments(String args) {
        this.startArguments = args;
        return this;
    }
}
