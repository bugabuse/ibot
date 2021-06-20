package com.farm.scripts.runecrafter;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.init.AccountData;

import java.awt.*;

public class Runecrafter extends MultipleStrategyScript implements PaintHandler, InventoryListener, ScriptRuntimeInfo {
    public PaintTimer timer;
    public int essencesCrafted;
    public int runesCrafted;
    private SkillTracker tracker;

    public Runecrafter() {
        this(false);
    }

    public Runecrafter(boolean accountTrainer) {
        super(Strategies.DEFAULT);
        this.timer = new PaintTimer();
        this.essencesCrafted = 0;
        this.runesCrafted = 0;
        this.tracker = new SkillTracker(Skill.RUNECRAFTING);
    }

    public void onLoad() {
        this.addEventHandler(new InventoryEventHandler(this));
    }

    public void onStart() {
        if (AccountData.current().username.contains("ramb")) {
            Constants.IS_SLAVE = false;
            Constants.IS_USING_SLAVES = true;
        } else {
            Constants.IS_SLAVE = true;
            Constants.IS_USING_SLAVES = false;
        }

        Strategies.init(this);
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.06");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Essences: " + this.essencesCrafted + "(" + this.timer.getHourRatio(this.essencesCrafted) + ")");
        this.drawString(g, "Runes: " + this.runesCrafted + "(" + this.timer.getHourRatio(this.runesCrafted) + ")");
        this.drawString(g, "State: " + this.getCurrentlyExecuting());
        this.drawString(g, this.tracker.getPaintString());
    }

    public void onItemAdded(Item item) {
        if (item.getName().toLowerCase().contains(" rune")) {
            this.runesCrafted += item.getAmount();
        }

        if (item.getName().toLowerCase().contains("essence")) {
            this.essencesCrafted += item.getAmount();
        }

    }

    public void onItemRemoved(Item item) {
    }

    public String runtimeInfo() {
        return "<th>" + this.timer.getElapsedString() + "</th><th>RC: " + Skill.RUNECRAFTING.getRealLevel() + "</th> " + this.essencesCrafted + "(" + this.timer.getHourRatio(this.essencesCrafted) + ")";
    }
}
