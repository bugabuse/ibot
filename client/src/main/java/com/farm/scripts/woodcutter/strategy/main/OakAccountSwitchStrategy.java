package com.farm.scripts.woodcutter.strategy.main;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;

public class OakAccountSwitchStrategy extends Strategy {
    public boolean active() {
        return BotScript.get() != null && ("" + BotScript.get().getStartArguments()).contains("Oak") && Skill.WOODCUTTING.getRealLevel() >= 60;
    }

    protected void onAction() {

        Bot bot = Bot.get();
        AccountData account = bot.getSession().getAccount();
        if (account != null) {
            if (!Login.logout()) {
                return;
            }

            WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + account.username + "&script=Quester|Chopper");
            bot.getSession().setAccount((AccountData) null);
            bot.getScriptHandler().stop();
        }

        this.sleep(2000);
    }
}
