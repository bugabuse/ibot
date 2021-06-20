/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.content.botdata;

public class BotData {
    public String ipAddress;
    public long lastUpdate;

    public BotData(String address, long lastUpdate) {
        this.ipAddress = address;
        this.lastUpdate = lastUpdate;
    }

    public int hashCode() {
        return this.ipAddress.hashCode();
    }

    public String toString() {
        return this.ipAddress;
    }

    public boolean equals(Object other) {
        return this.ipAddress.equals(other);
    }
}

