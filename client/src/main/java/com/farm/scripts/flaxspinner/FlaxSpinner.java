package com.farm.scripts.flaxspinner;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.impl.debuggers.MouseDebug;
import com.farm.ibot.core.script.impl.debuggers.MouseDebug.ClickPoint;
import com.farm.ibot.scriptutils.mule.MuleUtils;
import com.farm.scripts.flaxspinner.strategies.flax.SpinStrategy;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class FlaxSpinner extends MultipleStrategyScript implements PaintHandler, InventoryListener, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();
    public boolean loadedTracker = false;
    public SkillTracker tracker;

    public FlaxSpinner() {
        super(Strategies.DEFAULT_TRAINING);
        this.tracker = new SkillTracker(Skill.CRAFTING);
    }

    public void onStart() {
        this.addEventHandler(new InventoryEventHandler(this));
        Strategies.init(this);
        this.getScriptHandler().addDebug(MouseDebug.class);
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.34");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Items: " + this.getItemsGained() + "(" + this.timer.getHourRatio(this.getItemsGained()) + ")");
        this.drawString(g, "Strategy: " + this.getCurrentlyExecuting());
        this.drawString(g, "Mule queue: " + Strategies.muleManager.notifierStrategy.queueIndex);
        Iterator var2 = MouseDebug.getClicks().entrySet().iterator();

        while (var2.hasNext()) {
            Entry<ClickPoint, Integer> entry = (Entry) var2.next();
            g.setColor(new Color(0, 255, 0, MathUtils.clamp(40 * (Integer) entry.getValue(), 0, 255)));
            g.drawRect(((ClickPoint) entry.getKey()).x, ((ClickPoint) entry.getKey()).y, 1, 1);
        }

    }

    private int getItemsGained() {
        return this.tracker.getExpGained() / 15;
    }

    public void onItemAdded(Item item) {
        if (Skill.FLETCHING.getRealLevel() >= 10 && Skill.FLETCHING.getExperience() > 0 && !this.loadedTracker) {
            this.tracker.reset();
        }

    }

    public String runtimeInfo() {
        String otherBotsStr = "";
        List<Player> list = (List) Players.getAll().stream().filter((p) -> {
            return p.getPosition().distance(SpinStrategy.SPIN_SPOT) <= 6;
        }).collect(Collectors.toList());
        int myBots = (int) list.stream().filter((p) -> {
            return MuleUtils.isOnline(p.getName());
        }).count();
        otherBotsStr = "<b>MyBots:</b> " + myBots + "(" + (list.size() - myBots) + ")";
        int items = this.getItemsGained();
        if (this.timer.getHourRatio(items) > 1600) {
            items = 0;
        }

        return "<th>" + this.timer.getElapsedString() + "</th><th><b>Craft: </b>" + Skill.CRAFTING.getRealLevel() + "</th><th>" + otherBotsStr + "</th><th>Strings:" + items + "(" + this.timer.getHourRatio(items) + ")</th><th><b>" + this.getCurrentlyExecuting() + "</b></th>";
    }
}
