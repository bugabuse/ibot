package com.farm.ibot.plugins.idleproxyfetcher;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.plugin.Plugin;
import com.farm.ibot.plugins.proxydatasender.ProxyRecord;
import com.farm.ibot.proxy.Proxy;

import java.util.Iterator;

public class IdleProxyFetcher extends Plugin {
    public void onStart() {
    }

    public int onLoop() {
        boolean failed = false;
        Iterator var2 = Main.bots.iterator();

        while (true) {
            while (true) {
                Bot bot;
                do {
                    do {
                        if (!var2.hasNext()) {
                            if (failed) {
                                return 20000;
                            }

                            return 3000;
                        }

                        bot = (Bot) var2.next();
                    } while (!bot.isLoaded());
                } while (bot.proxy != null);

                ProxyRecord record = (ProxyRecord) WebUtils.downloadObject(ProxyRecord.class, "http://api.hax0r.farm:8080/proxy/assign");
                if (record != null && bot.proxy == null) {
                    Proxy proxy = new Proxy(record.proxy);
                    proxy.sessionId = record.sessionId;
                    bot.proxy = proxy;
                } else {
                    Time.sleep(1000);
                    failed = true;
                }
            }
        }
    }
}
