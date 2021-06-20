package com.farm.scripts.humanfletcher.strategies;

import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.Settings;

public class AccountTermination extends Strategy {
    public long minutesInLast2h = 0L;
    public long minutesInLast12h = 0L;
    private int maxMinutesPerSession = Random.next(70, 120);

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (!Settings.devMode) {
            this.sleep(60000);
            AccountData account = AccountData.current();
            this.minutesInLast2h = account.fetchPlayTime(3, 0) / 1000L / 60L;
            this.minutesInLast12h = account.fetchPlayTime(16, 0) / 1000L / 60L;
            if (this.minutesInLast12h > 360L || this.minutesInLast2h > (long) this.maxMinutesPerSession) {
                int duration = Random.next(120, 170);
                if (this.minutesInLast12h > 360L) {
                    duration = Random.next(420, 540);
                }

                System.out.println("Our session expired. Minutes played in last 3 hours: " + this.minutesInLast2h);
                if (account.terminate(duration)) {
                    while (!Login.logout()) {
                        System.out.println("Our session expired. Minutes played in last 3 hours: " + this.minutesInLast2h);
                        Time.sleep(1000);
                    }

                    Bot.get().getSession().setAccount((AccountData) null);
                    Bot.get().getScriptHandler().stop();
                }
            }

        }
    }

    public boolean isBackground() {
        return true;
    }
}
