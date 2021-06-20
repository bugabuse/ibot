package com.farm.scripts.birthday;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.core.script.MultipleStrategyScript;

import java.awt.*;

public class BirthdayEvent extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();

    public BirthdayEvent() {
        super(Strategies.DEFAULT);
    }

    public void onStart() {

        Strategies.init(this);
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Birthday Version 0.03");
        this.drawString(g, "State " + BirthdayState.getState());
        this.drawString(g, "State " + Config.get(2665));
        this.drawString(g, "Current " + this.getCurrentlyExecuting());
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>" + BirthdayState.getState() + "</th><th>" + this.getCurrentlyExecuting() + "</th>";
    }
}
