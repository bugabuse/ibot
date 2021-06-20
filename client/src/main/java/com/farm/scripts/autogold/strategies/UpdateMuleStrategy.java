package com.farm.scripts.autogold.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.web.WebClient;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.autogold.AutoGold;

public class UpdateMuleStrategy extends Strategy {
    public static boolean forceUpdate = false;
    private PaintTimer updater = new PaintTimer(0L);
    private PaintTimer lastOnlineTimer = new PaintTimer(0L);

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Client.isInGame()) {
            this.lastOnlineTimer.reset();
        }

        AccountData current = AccountData.current();
        if (current != null && !current.isBanned) {
            if (this.lastOnlineTimer.getElapsedSeconds() < 1200L) {
                if (this.updater.getElapsed() > 5000L || forceUpdate) {
                    (new WebClient()).downloadString(AutoGold.DOMAIN + "/shop/update?muleName=" + current.getGameUsername() + "&goldInStock=" + AutoGold.currentStock);
                    forceUpdate = false;
                    this.updater.reset();
                }
            } else {
                Bot.get().getScriptHandler().loginRandom.active = true;
                Bot.get().getScriptHandler().antiKick.active = true;
            }
        }

    }

    public boolean isBackground() {
        return false;
    }
}
