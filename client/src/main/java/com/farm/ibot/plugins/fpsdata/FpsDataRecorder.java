package com.farm.ibot.plugins.fpsdata;

import com.farm.ibot.Main;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.plugin.Plugin;

import java.util.Iterator;

public class FpsDataRecorder extends Plugin {
    public void onStart() {
    }

    public int onLoop() {
        Iterator var1 = Main.bots.iterator();

        while (var1.hasNext()) {
            Bot bot = (Bot) var1.next();
            if (bot.isLoaded()) {
                bot.executeAndWait(() -> {
                    bot.getFpsData().update();
                });
            }
        }

        this.listenFpsDrops();
        return 200;
    }

    public void listenFpsDrops() {
        int totalFps = 0;
        int index = 0;
        Iterator var3 = Main.bots.iterator();

        Bot bot;
        while (var3.hasNext()) {
            bot = (Bot) var3.next();
            Bot.currentThreadBot = bot;
            if (bot.isLoaded() && bot.getScriptHandler().getCurrentlyExecuting() != null && Client.isInGame()) {
                totalFps = (int) ((long) totalFps + bot.getFpsData().getFps(240));
                ++index;
            }
        }

        if (index > 0 && totalFps / index < 1) {
            var3 = Main.bots.iterator();

            while (var3.hasNext()) {
                bot = (Bot) var3.next();
                Bot.currentThreadBot = bot;
                if (bot.isLoaded() && bot.getScriptHandler().getCurrentlyExecuting() != null && Client.isInGame()) {
                    Debug.log(bot.getFpsData().getFps(240));
                }
            }


            Bot.sendRestartCommand();
        }

    }
}
