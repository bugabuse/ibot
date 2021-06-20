package com.farm.scripts.thiever;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;

import java.awt.*;
import java.util.Calendar;
import java.util.TimeZone;

public class Thiever extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, InventoryListener {
    public PaintTimer timer = new PaintTimer();
    private SkillTracker tracker;
    private int gainedRanarr;
    private int gainedSnapdragon;
    private int gainedTorsol;

    public Thiever() {
        super(Strategies.DEFAULT);
        this.tracker = new SkillTracker(Skill.THIEVING);
    }

    public int loopInterval() {
        return 400;
    }

    public void onStart() {

        Strategies.init(this);
        this.addEventHandler(new InventoryEventHandler(this));
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Thiever Version 0.15");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
        Calendar rightNow = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
        int hour = rightNow.get(11);
        this.drawString(g, "Hour: " + hour);
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
        this.drawString(g, this.tracker.getPaintString());
    }

    public String runtimeInfo() {
        String str = "";
        if (Skill.THIEVING.getRealLevel() >= 38) {
            str = "<th>R: " + this.gainedRanarr + " S: " + this.gainedSnapdragon + " T: " + this.gainedTorsol + "</th>";
        }

        return this.timer.getElapsedString() + "</th><th>" + this.tracker.getPaintString() + " Lvl: " + Skill.THIEVING.getRealLevel() + "</th>" + str + "<th>" + this.getCurrentlyExecuting() + "</th>";
    }

    public void onItemAdded(Item item) {
        if (!Bank.isOpen()) {
            if (item.getName().toLowerCase().contains("ranarr")) {
                ++this.gainedRanarr;
            } else if (item.getName().toLowerCase().contains("snapdragon")) {
                ++this.gainedSnapdragon;
            } else if (item.getName().toLowerCase().contains("torstol")) {
                ++this.gainedTorsol;
            }

        }
    }
}
