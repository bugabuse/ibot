package com.farm.scripts.christmas;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.core.script.MultipleStrategyScript;

import java.awt.*;

public class ChristmasEvent extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();

    public ChristmasEvent() {
        super(Strategies.DEFAULT);
    }

    public void onStart() {

        Strategies.init(this);
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "christmas Version 0.20");
        this.drawString(g, "State " + ChristmasState.getState());
        this.drawString(g, "State " + Config.get(2633));
        this.drawString(g, "Current " + this.getCurrentlyExecuting());
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>" + ChristmasState.getState() + "</th><th>" + this.getCurrentlyExecuting() + "</th>";
    }
}
