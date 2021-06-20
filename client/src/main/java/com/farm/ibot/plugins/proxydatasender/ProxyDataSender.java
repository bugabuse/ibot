package com.farm.ibot.plugins.proxydatasender;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.plugin.Plugin;

import java.util.ArrayList;
import java.util.Iterator;

public class ProxyDataSender extends Plugin {
    public void onStart() {
    }

    public int onLoop() {
        ArrayList<ProxyRecord> toUpdate = new ArrayList();
        Iterator var2 = Main.bots.iterator();

        while (var2.hasNext()) {
            Bot bot = (Bot) var2.next();
            if (bot.proxy != null) {
                ProxyRecord record = new ProxyRecord();
                record.proxy = bot.proxy.getName();
                record.sessionId = bot.proxy.sessionId;
                toUpdate.add(record);
            }
        }

        if (toUpdate.size() > 0) {
            WebUtils.uploadObject(toUpdate, "http://api.hax0r.farm:8080/proxy/update");
        }

        return 10000;
    }
}
