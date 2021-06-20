package com.farm.ibot.plugins.onlinedatasender;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.plugin.Plugin;
import com.farm.ibot.init.AccountData;

import java.util.ArrayList;
import java.util.Iterator;

public class BannedAccountsHandler extends Plugin {
    public void onStart() {
    }

    public int onLoop() {
        ArrayList<AccountData> toUpdate = new ArrayList();
        Iterator var2 = Main.bots.iterator();

        while (var2.hasNext()) {
            Bot bot = (Bot) var2.next();
            AccountData data = bot.getSession().getAccount();
            if (data != null && data.isBanned) {

                toUpdate.add(data);
                bot.getSession().setAccount((AccountData) null);
                bot.getScriptHandler().stop();
            }
        }

        if (toUpdate.size() > 0) {
            WebUtils.uploadObject(toUpdate, "http://api.hax0r.farm:8080/accounts/update/");
        }

        return 3000;
    }
}
