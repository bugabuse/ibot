/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.content.trade;

public class AccountData {
    public String username;
    public String gameUsername;
    public String password;
    public String sessionId;
    public int preferredWorld = 301;
    public String autostartScript = "";
    public String currentSpot = "";
    public int currentSpotId = -1;
    public int maxSpotCount = -1;
    public int id = -1;
    public int uniqueScriptId = -1;
    public String note;
    public int world;
    public boolean isMembers = false;
    public boolean isLoggedIn = false;
    public boolean isBanned = false;
    public boolean isFlaggedAsStolen = false;
    public long lastUpdate;
    public long accountCreationTime = 0L;
    public long terminatedUntil = 0L;
    public String currentScript;
    public String description;
    public String lastIpAddress;
    public String registeredIpAddress;
    public String hostName;
    public String proxy;
    public String twoFactorSecretKey;
    public boolean emailConfirmed = false;

    public AccountData(String username, int world, String description) {
        this.username = username;
        this.world = world;
        this.description = description;
        this.currentSpot = "";
    }

    public boolean isOnline() {
        return System.currentTimeMillis() - this.lastUpdate < 60000L;
    }

    public boolean equals(Object other) {
        return other instanceof AccountData && this.username.equalsIgnoreCase(((AccountData)other).username);
    }

    public int hashCode() {
        return this.username.hashCode();
    }

    public String toString() {
        return this.username.split("@")[0];
    }

    public void setAs(AccountData as) {
        this.username = as.username;
        this.password = as.password;
        this.preferredWorld = as.preferredWorld;
        this.autostartScript = as.autostartScript;
        this.world = as.world;
        this.isLoggedIn = as.isLoggedIn;
        this.isBanned = as.isBanned;
        this.lastUpdate = as.lastUpdate;
        this.currentScript = as.currentScript;
        this.description = as.description;
        this.gameUsername = as.gameUsername;
        this.currentSpot = "" + as.currentSpot;
        this.registeredIpAddress = "" + as.registeredIpAddress;
        this.lastIpAddress = "" + as.lastIpAddress;
        this.isFlaggedAsStolen = as.isFlaggedAsStolen;
        this.currentSpotId = as.currentSpotId;
        this.maxSpotCount = as.maxSpotCount;
        this.uniqueScriptId = as.uniqueScriptId;
        this.hostName = "" + as.hostName;
        this.proxy = "" + as.proxy;
        this.accountCreationTime = as.accountCreationTime;
        this.emailConfirmed = as.emailConfirmed;
        this.twoFactorSecretKey = as.twoFactorSecretKey;
        this.isMembers = as.isMembers;
        this.terminatedUntil = as.terminatedUntil;
    }

    public String getGameUsername() {
        return this.gameUsername;
    }

    public void clearData() {
        this.isLoggedIn = false;
        this.description = "";
        this.lastUpdate = 0L;
        this.currentScript = "";
    }

    public int getUniqueScriptId() {
        return this.uniqueScriptId;
    }

    public boolean isTerminated() {
        return this.terminatedUntil > System.currentTimeMillis();
    }
}

