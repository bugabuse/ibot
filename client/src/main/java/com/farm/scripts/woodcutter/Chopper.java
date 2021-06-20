package com.farm.scripts.woodcutter;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.PlayerSpotInfo;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.api.util.string.AccountPlaytimeDynamicString;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;

import java.awt.*;

public class Chopper extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, InventoryListener, PlayerSpotInfo, MessageListener {
    public PaintTimer timer;
    private AccountPlaytimeDynamicString playTime = new AccountPlaytimeDynamicString();
    private SkillTracker tracker;
    private int logs;
    private int deaths;
    private String lastTree;

    public Chopper() {
        super(Strategies.DEFAULT);
        this.tracker = new SkillTracker(Skill.WOODCUTTING);
        this.timer = new PaintTimer();
        this.logs = 0;
        this.deaths = 0;
        this.lastTree = "";
    }

    public Chopper(boolean powerChopping) {
        super(Strategies.DEFAULT);
        this.tracker = new SkillTracker(Skill.WOODCUTTING);
        this.timer = new PaintTimer();
        this.logs = 0;
        this.deaths = 0;
        this.lastTree = "";
        ChopSettings.powerChopping = true;
    }

    public void onStart() {
        this.addEventHandler(new InventoryEventHandler(this));
    }

    public void onLoad() {
        this.getScriptHandler().loginRandom.autoWorldAssignEnabled = true;
    }

    public void onStop() {
    }

    public void onStartWhenLoggedIn() {
        Strategies.init(this);
        this.lastTree = ChopSettings.getTreeToChop().name;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 1.86");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Logs: " + this.logs + "(" + this.timer.getHourRatio(this.logs) + ")");
        this.drawString(g, "Strategy: " + this.getCurrentlyExecuting());
        this.drawString(g, "Location: " + ChopSettings.getSpot().getNote());
        this.drawString(g, "Exp: " + this.tracker.getExpGained() + "(" + this.tracker.getHourRatio() / 1000 + "k)");
        if (!ChopSettings.powerChopping) {
            this.drawString(g, "Trade state: " + Strategies.muleManager.getCurrentState());
            this.drawString(g, "Mule name: " + Strategies.muleManager.getMuleName());
            if (Strategies.muleManager != null && Strategies.muleManager.toWithdraw != null) {
                this.drawString(g, "");
                this.drawString(g, "Wealth: " + Strategies.muleManager.toWithdraw.calculateWealth());
                this.drawString(g, "Trading items:");
                WithdrawItem[] var2 = Strategies.muleManager.toWithdraw.getPriceableItemArray();
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    Item item = var2[var4];
                    this.drawString(g, "(" + item.getId() + ") " + item.getName() + ": " + item.getAmount());
                }
            }
        }

    }

    private String getWealth() {
        if (Strategies.muleManager != null && Strategies.muleManager.toWithdraw != null) {
            int wealth = Strategies.muleManager.toWithdraw.calculateWealth() / 1000;
            return wealth + "k";
        } else {
            return "?";
        }
    }

    private String getWealthCoins() {
        int wealth = (Inventory.getCount(995) + Bank.getCache().getCount(new int[]{995})) / 1000;
        return wealth + "k";
    }

    public String runtimeInfo() {
        int hourRatio = this.timer.getHourRatio(this.logs);
        int logs = this.logs;
        if (hourRatio > 1000) {
            hourRatio = 0;
            logs = -1;
        }

        return this.timer.getElapsedString() + "</th><th>Logs: " + logs + "(" + hourRatio + ")</th><th><b>WC:</b> " + Skill.WOODCUTTING.getRealLevel() + "</th><th><b>Wealth:</b> " + this.getWealth() + " (" + this.getWealthCoins() + ")</th><th>" + ChopSettings.getSpot().getNote() + "</th><th>" + this.getCurrentlyExecuting() + "</th><th>PlaytimeHours: " + this.getTotalPlayTime() + "</th>" + (this.deaths > 0 ? "<th><b>Deaths:</b> " + this.deaths + "</th>" : "");
    }

    private long getTotalPlayTime() {
        return (long) this.playTime.intValue() / 1000L / 60L / 60L;
    }

    public void onItemAdded(Item item) {
        if (!this.lastTree.equals(ChopSettings.getTreeToChop().name)) {
            this.logs = 0;
            this.lastTree = ChopSettings.getTreeToChop().name;
        }

        if (item.getName().toLowerCase().contains("log")) {
            if (this.logs == 0) {
                this.timer.reset();
                this.tracker = new SkillTracker(Skill.WOODCUTTING);
            }

            ++this.logs;
        }

    }

    public boolean breaksEnabled() {
        return false;
    }

    public int maxSpotCount() {
        return ChopSettings.getAvailableSpots().length;
    }

    public void onMessage(String message) {
        if (message.contains("you are dead")) {
            ++this.deaths;
        }

    }

    public int loopInterval() {
        return 600;
    }
}
