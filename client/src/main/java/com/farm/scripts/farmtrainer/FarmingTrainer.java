package com.farm.scripts.farmtrainer;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.util.web.account.AccountConfig;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.manifest.ScriptManifest;
import com.farm.ibot.init.AccountData;

import java.awt.*;

@ScriptManifest(
        isP2p = true
)
public class FarmingTrainer extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, MessageListener {
    public static AccountConfig config;
    public static BotScript instance;
    public PaintTimer timer = new PaintTimer();

    public FarmingTrainer() {
        super(Strategies.DEFAULT);
        instance = this;
    }

    public void onStart() {

        this.addEventHandler(new MessageEventHandler(this));
        config = AccountConfig.fetch(AccountData.current().username);
        Strategies.init(this);
        this.getScriptHandler().webNotFoundRandom.enabled = false;
        this.getScriptHandler().loginRandom.autoWorldAssignEnabled = false;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "FarmTrainer Version 0.40");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Farming: " + Skill.FARMING.getRealLevel() + "</th></th><th>" + this.getCurrentlyExecuting() + "</th>";
    }

    public void onMessage(String message) {
        if (message.toLowerCase().contains("there is already")) {
        }

        if (message.toLowerCase().contains("have to be a member to log into that world")) {
            AccountData data = AccountData.current();
            Bot.get().getSession().setAccount((AccountData) null);
            WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + data.username + "&script=BONDS_FARMING");
            Login.logout();
            this.getScriptHandler().stop();
        }

    }
}
