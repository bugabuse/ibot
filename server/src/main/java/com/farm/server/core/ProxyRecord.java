/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core;

public class ProxyRecord {
    public String proxy;
    public long lastUpdate;
    public int sessionId;

    public boolean isOnline() {
        return System.currentTimeMillis() - this.lastUpdate < 60000L;
    }
}

