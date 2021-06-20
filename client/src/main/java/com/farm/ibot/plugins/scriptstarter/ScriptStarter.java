// Decompiled with: CFR 0.150
package com.farm.ibot.plugins.scriptstarter;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.plugin.Plugin;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.ConsoleParams;
import com.farm.ibot.init.Session;

public class ScriptStarter
        extends Plugin {
    @Override
    public void onStart() {
    }

    @Override
    public int onLoop() {
        if (ConsoleParams.contains("lastaccs")) {
            return 60000;
        }
        boolean failed = false;
        for (Bot bot : Main.bots) {
            Session data = bot.getSession();
            if (!bot.isLoaded() || data == null || data.getAccount() != null || data.autostartScript == null || data.autostartScript.length() <= 0)
                continue;

            AccountData account = WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/available/?ignoreIpAddress=" + ConsoleParams.contains("ignoreIpAddress") + "&useDynamicProxy=" + (ConsoleParams.contains("dynamicProxy") || ConsoleParams.contains("accountDynamicProxy")) + "&useAccountDynamicProxy=" + ConsoleParams.contains("accountDynamicProxy") + "&script=" + data.autostartScript + "&hostname=" + bot.getFullHostName());

            if (account == null && !ConsoleParams.contains("noFreeAgent")) {
                account = WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/available/?ignoreIpAddress=" + ConsoleParams.contains("ignoreIpAddress") + "&useDynamicProxy=" + (ConsoleParams.contains("dynamicProxy") || ConsoleParams.contains("accountDynamicProxy")) + "&script=FreeAgent&hostname=" + bot.getFullHostName());

                if (account != null) {
                    data.setAccount(account);
                    AccountData boundAccount = WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + account.username + "&script=" + data.autostartScript);
                    account.autostartScript = data.autostartScript;
                    if (boundAccount != null) {
                        account.uniqueScriptId = boundAccount.uniqueScriptId;
                    }
                }
            }
            if (account != null) {
                data.setAccount(account);
                if (bot.isLoaded() && bot.getScriptHandler().getCurrentlyExecuting() == null) {
                    new Thread(bot.getThreadGroup(), () -> {
                        try {
                            bot.getScriptHandler().start(bot.getScriptHandler().scriptLoader.load(data.autostartScript));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, "ScriptStarter " + bot.getThreadGroup().getName() + " thread").start();
                }
            } else {
                failed = true;
            }
            Time.sleep(1000);
        }
        if (failed) {
            return 30000;
        }
        return 5000;
    }
}
