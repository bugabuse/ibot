package com.farm.scripts.farmer;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.manifest.ScriptManifest;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.farmer.api.PatchState;
import com.farm.scripts.farmer.strategies.FarmingStateListener;

import java.awt.*;
import java.util.Iterator;

@ScriptManifest(
        isP2p = true
)
public class DailyFarming extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, MessageListener {
    public PaintTimer timer = new PaintTimer();

    public DailyFarming() {
        super(Strategies.DEFAULT);
        this.addEventHandler(new MessageEventHandler(this));
    }

    public void onStart() {

        Strategies.init(this);
        this.getScriptHandler().webNotFoundRandom.enabled = false;
        this.getScriptHandler().loginRandom.autoWorldAssignEnabled = false;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Farming Version 0.14");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
        this.drawString(g, "State: " + PatchState.get());
        this.drawString(g, "");
        this.drawString(g, "");
        this.drawString(g, "");
        Iterator var2 = FarmingStateListener.config.keySet().iterator();

        while (var2.hasNext()) {
            String key = (String) var2.next();
            if (key.contains("Outfit")) {
                this.drawString(g, key + ": " + FarmingStateListener.config.getInt(key));
            } else if (FarmingStateListener.config.getLong(key) > 0L) {
                long seedGrownTime = FarmingStateListener.config.getLong(key) + 4800000L;
                String timeLeft = PaintTimer.msToTime(seedGrownTime - System.currentTimeMillis());
                this.drawString(g, key + ": " + timeLeft);
            } else {
                this.drawString(g, key + ": Ready!");
            }
        }

        if (Strategies.muleManager != null && Strategies.muleManager.toWithdraw != null) {
            this.drawString(g, "");
            this.drawString(g, "Wealth: " + Strategies.muleManager.toWithdraw.calculateWealth());
            this.drawString(g, "Trading items:");
            WithdrawItem[] var7 = Strategies.muleManager.toWithdraw.getPriceableItemArray();
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                Item item = var7[var9];
                this.drawString(g, "(" + item.getId() + ") " + item.getName() + ": " + item.getAmount());
            }
        }

    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Farming: " + Skill.FARMING.getRealLevel() + "</th></th><th>" + this.getCurrentlyExecuting() + "</th>";
    }

    public void onMessage(String message) {
        if (message.toLowerCase().contains("have to be a member to log into that world")) {
            AccountData data = AccountData.current();
            Bot.get().getSession().setAccount((AccountData) null);
            WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + data.username + "&script=BondBuyer");
            Login.logout();
            this.getScriptHandler().stop();
        }

    }
}
