package com.farm.scripts.bondbuyer;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.init.AccountData;

import java.awt.*;

public class BondBuyer extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    private static String scriptToBind = "";
    private static BotScript instance;
    public PaintTimer timer = new PaintTimer();

    public BondBuyer() {
        super(Strategies.DEFAULT);
        instance = this;
    }

    public static void onMembershipActivated() {
        if (Client.isInGame()) {

            if (!WorldHopping.isF2p(Client.getCurrentWorld())) {
                rebindOrContinue();
            } else {
                if (!Varbit.MEMBERSHIP_DAYS.booleanValue()) {
                    while (!Login.logout()) {
                        Time.sleep(5000);
                    }
                } else {
                    rebindOrContinue();
                }

            }
        }
    }

    public static void rebindOrContinue() {
        String scriptToBind = null;
        if (get().startArguments.contains("FletcherMobile")) {
            scriptToBind = "FletcherMobile";
        }

        if (get().startArguments.contains("FlaxSpinnerMobile")) {
            scriptToBind = "FlaxSpinnerMobile";
        }

        if (scriptToBind != null) {
            AccountData account = AccountData.current();

            while (!Login.logout()) {
                Time.sleep(1000);
            }

            if (account != null) {
                WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + account.username + "&script=" + scriptToBind);
            }

            Bot.get().getSession().setAccount((AccountData) null);
            Bot.get().getScriptHandler().stop();
        } else {
            get().getScriptHandler().startNextQueuedScript(instance);
        }

    }

    public void onStart() {

        Strategies.init(this);
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "BondBuyer Version 0.02");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Mage: " + Player.getLocal().getCombatLevel() + "</th>";
    }
}
