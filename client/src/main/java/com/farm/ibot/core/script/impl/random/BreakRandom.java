package com.farm.ibot.core.script.impl.random;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.RandomEvent;
import com.farm.ibot.core.script.impl.random.breakhandler.GameBreakData;
import com.farm.ibot.init.AccountData;

import java.awt.*;

public class BreakRandom extends RandomEvent implements PaintHandler {
    private GameBreakData currentBreakData;

    public void onPaint(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(10, drawStringY - 20, 170, 80);
        g.setColor(Color.white);
        if (this.currentBreakData != null) {
            this.drawString(g, "Handling a break - " + this.currentBreakData.breakName);
        }

    }

    public void onStart() {
        if (this.currentBreakData != null) {

        }

    }

    public int onLoop() {
        if (this.currentBreakData != null) {
            if (this.currentBreakData.logOffGame) {
                if (!Login.logout()) {
                    return 1000;
                }

                Bot.get().getScriptHandler().loginRandom.active = false;
                Bot.get().getScriptHandler().antiKick.active = false;
            }

            Time.sleep(this.currentBreakData.minWaitTime, this.currentBreakData.maxWaitTime);
        }

        Bot.get().getScriptHandler().loginRandom.active = true;
        Bot.get().getScriptHandler().antiKick.active = true;
        this.currentBreakData = null;
        return 1000;
    }

    public boolean isEnabled() {
        if (!Bot.get().getScriptHandler().getScript().breaksEnabled()) {
            return false;
        } else if (this.currentBreakData != null) {
            return true;
        } else {
            AccountData data = AccountData.current();
            if (data != null) {
                if (data.gameBreakData == null) {
                    data.gameBreakData = new GameBreakData[]{GameBreakData.generateSmallBreak(), GameBreakData.generateIngameBreak(), GameBreakData.generateBigBreak()};
                }

                GameBreakData[] var2 = data.gameBreakData;
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    GameBreakData breakData = var2[var4];
                    if (Random.next(0.0D, 1.0D) <= breakData.intervalPerHour / 3600.0D) {
                        this.currentBreakData = breakData;
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean isBackground() {
        return false;
    }

    public long checkInterval() {
        return 1000L;
    }
}
