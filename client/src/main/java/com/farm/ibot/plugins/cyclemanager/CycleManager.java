package com.farm.ibot.plugins.cyclemanager;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.PaintUtils;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.plugin.Plugin;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.Settings;

import java.util.Iterator;

public class CycleManager extends Plugin {
    public void onStart() {
    }

    public int onLoop() {
        if (!Settings.cycledSessions) {
            return 60000;
        } else {
            Iterator var1 = Main.bots.iterator();

            while (var1.hasNext()) {
                Bot bot = (Bot) var1.next();

                try {
                    AccountData data = bot.getSession().getAccount();
                    if (data != null) {
                        data.playTime = Long.parseLong(WebUtils.download("http://api.hax0r.farm:8080/accounts/stats/playtime?username=" + data.username + "&lastHours=" + Settings.cycleIntervalHours + "&lastMinutes=00"));
                        if (data.playTime > (long) (Settings.cycleDurationHours * 3600 * 1000 + Settings.cycleDurationMinutes * 60 * 1000)) {
                            Debug.log("Cycle of " + data.getGameUsername() + " has end. (" + PaintUtils.convertMillisToString(data.playTime) + ") Restarting bot to load new account.");
                            Bot.sendRestartAsBanCommand();
                        }
                    }
                } catch (Exception var4) {
                    var4.printStackTrace();
                }
            }

            return 5000;
        }
    }
}
