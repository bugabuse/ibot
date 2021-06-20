package com.farm.ibot.scriptutils.mule;

import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.init.AccountData;

import java.util.Arrays;

public class MuleUtils {
    private static AccountData[] lastData = null;
    private static PaintTimer updateTimer = new PaintTimer(0L);

    public static AccountData forName(String name) {
        if (lastData == null || updateTimer.getElapsed() > 15000L) {
            updateOnlineData();
        }

        return (AccountData) Arrays.stream(lastData).filter((a) -> {
            return a != null && a.getGameUsername().equalsIgnoreCase(name);
        }).findAny().orElse(null);
    }

    public static boolean isOnline(String name) {
        if (lastData == null || updateTimer.getElapsed() > 15000L) {
            updateOnlineData();
        }

        return lastData != null && Arrays.stream(lastData).anyMatch((a) -> {
            return a != null && a.getGameUsername().equalsIgnoreCase(name);
        });
    }

    public static void updateOnlineData() {
        AccountData[] data = (AccountData[]) WebUtils.downloadObject(AccountData[].class, "http://api.hax0r.farm:8080/accounts/onlinejson");
        if (data != null) {
            lastData = data;
        }

        updateTimer.reset();
    }
}
