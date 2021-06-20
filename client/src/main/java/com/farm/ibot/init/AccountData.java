package com.farm.ibot.init;

import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.util.web.WebClient;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.impl.random.breakhandler.GameBreakData;

public class AccountData {
    public String gameUsername;
    public String username;
    public String password;
    public String sessionId;
    public String lastIpAddress;
    public String registeredIpAddress;
    public String hostName;
    public String proxy;
    public String twoFactorSecretKey;
    public int preferredWorld = 301;
    public String autostartScript = "";
    public String currentSpot = "";
    public int currentSpotId = -1;
    public int maxSpotCount = -1;
    public int world = -1;
    public boolean isLoggedIn = false;
    public boolean isBanned = false;
    public boolean isFlaggedAsStolen = false;
    public boolean isMembers = false;
    public boolean emailConfirmed = false;
    public long lastUpdate;
    public long accountCreationTime;
    public long terminatedUntil;
    public String currentScript;
    public String description;
    public GameBreakData[] gameBreakData;
    public int id;
    public long playTime = -1L;
    public int uniqueScriptId = -1;
    private long sessionStart;

    public AccountData(String username, int world, String description) {
        this.username = username;
        this.world = world;
        this.description = description;
    }

    public static long seed() {
        return current() != null ? (long) (current().username.hashCode() * 100000) : (long) (Bot.get().hashCode() * 100000);
    }

    public static long seedForCurrentDay() {
        return seed() + System.currentTimeMillis() % 28800000L;
    }

    public static long seedForCurrentHour() {
        return seed() + System.currentTimeMillis() % 3600000L;
    }

    public static AccountData current() {
        return Bot.get().getSession() != null ? Bot.get().getSession().getAccount() : null;
    }

    public String getGameUsername() {
        return "" + this.gameUsername;
    }

    public boolean equals(Object other) {
        return other instanceof AccountData && this.username.equalsIgnoreCase(((AccountData) other).username);
    }

    public int hashCode() {
        return this.username.hashCode();
    }

    public String toString() {
        return this.username.split("@")[0];
    }

    public int getUniqueScriptId() {
        return this.uniqueScriptId;
    }

    public int getId() {
        return this.id;
    }

    public long fetchPlayTime(int hours, int minutes) {
        return Long.parseLong(WebUtils.download("http://api.hax0r.farm:8080/accounts/stats/playtime?username=" + this.username + "&lastHours=" + hours + "&lastMinutes=" + minutes));
    }

    public long fetchPlayHours(int hours, int minutes) {
        return Long.parseLong(WebUtils.download("http://api.hax0r.farm:8080/accounts/stats/playtime?username=" + this.username + "&lastHours=" + hours + "&lastMinutes=" + minutes)) / 1000L / 60L / 60L;
    }

    public boolean terminate(int minutes) {
        this.terminatedUntil = System.currentTimeMillis() + 60000L * (long) minutes;
        String result = (new WebClient()).downloadString("http://api.hax0r.farm:8080/account/terminate?accounts=" + this.username + "&minutes=" + minutes);
        return result.contains("Terminated");
    }

    public boolean update() {
        return WebUtils.uploadObject(new AccountData[]{this}, "http://api.hax0r.farm:8080/accounts/update/").length() > 0;
    }
}
