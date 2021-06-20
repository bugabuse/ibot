package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.Main;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BackgroundScript;
import com.farm.ibot.core.script.impl.random.LoginRandom;

import java.awt.*;

public class UsageDebug extends BackgroundScript implements PaintHandler {
    public String currentIpAddress = null;
    public PaintTimer timer = new PaintTimer(0L);

    public static void restartCurrentProxy() {
        LoginRandom.switchProxy();
        Bot.get().worldHopInterfaceFails = 0;
    }

    public void onStart() {
    }

    public int onLoop() {
        if (Bot.get().proxy != null && (this.currentIpAddress == null || this.timer.getElapsedSeconds() > 30L) && !Client.isInGame() && Bot.get().worldHopInterfaceFails > 4) {
            restartCurrentProxy();
            this.timer.reset();
            return 30000;
        } else {
            return 30000;
        }
    }

    public void onPaint(Graphics g) {
        Bot bot = Bot.get();
        if (bot != null) {
            this.drawStringRight(g, "FPS: " + bot.getFpsData().getFps());
            this.drawStringRight(g, "CPU: " + Main.cpuName);
            if (Main.crashCount > 0) {
                this.drawStringRight(g, "Crashed " + Main.crashCount);
            }

            if (!Client.isInGame()) {
                this.drawStringRight(g, "Proxy: " + (bot.proxy != null ? bot.proxy.getSimpleName() : "none"));
                this.drawStringRight(g, "Worldhop fails: " + Bot.get().worldHopInterfaceFails);
            }
        }
    }
}
