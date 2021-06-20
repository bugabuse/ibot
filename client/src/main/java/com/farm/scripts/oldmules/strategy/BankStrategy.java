package com.farm.scripts.oldmules.strategy;

import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.item.container.WithdrawContainer;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.oldmules.Strategies;
import com.farm.scripts.quester.quests.tutorial.TutorialIsland;

public class BankStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (!(new TutorialIsland()).isCompleted()) {
            if (Login.logout() && AccountData.current() != null) {
                AccountData boundAccount = (AccountData) WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + AccountData.current().username + "&script=FreeAgent_0");
                Bot.get().getSession().setAccount((AccountData) null);
            }

        } else {
            if (Bank.isOpen()) {
                WithdrawContainer container = Strategies.muleManager.toWithdraw;
                if (container == null || container.calculateWealth() < 1000) {
                    if (AccountData.current() != null) {

                        AccountData boundAccount = (AccountData) WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + AccountData.current().username + "&script=FreeAgent_0");
                        if (Login.logout()) {
                            Bot.get().getSession().setAccount((AccountData) null);
                        }
                    } else {
                        Login.logout();
                        Bot.get().getSession().setAccount((AccountData) null);
                    }

                    return;
                }
            } else if (Bank.openNearest()) {
            }

        }
    }
}
