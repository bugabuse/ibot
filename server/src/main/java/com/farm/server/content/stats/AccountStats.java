/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.content.stats;

import com.farm.server.core.util.MathUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.ToLongFunction;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class AccountStats {
    public String username;
    public long banTime = 0L;
    public boolean flaggedAsStolen;
    public TreeMap<Long, Long> onlineRecords = new TreeMap();
    private String scriptName;
    public String ipAddress;

    public AccountStats(String username) {
        this.username = username;
    }

    public void updateRecord() {
        if (this.onlineRecords.size() > 0) {
            long lastRecordTime = this.onlineRecords.lastEntry().getKey() + this.onlineRecords.lastEntry().getValue();
            long diff = System.currentTimeMillis() - lastRecordTime;
            if (diff < 30000L) {
                this.onlineRecords.put(this.onlineRecords.lastEntry().getKey(), this.onlineRecords.lastEntry().getValue() + diff);
            } else {
                this.onlineRecords.put(System.currentTimeMillis(), 0L);
            }
        } else {
            this.onlineRecords.put(System.currentTimeMillis(), 0L);
        }
    }

    public long getLastRecordTime() {
        if (this.onlineRecords.size() > 0) {
            return this.onlineRecords.lastEntry().getKey() + this.onlineRecords.lastEntry().getValue();
        }
        return 0L;
    }

    public long getFirstRecordTime() {
        if (this.onlineRecords.size() > 0) {
            return this.onlineRecords.lastEntry().getKey() + this.onlineRecords.firstEntry().getValue();
        }
        return 0L;
    }

    public long getOnlineTimeMs() {
        return this.onlineRecords.values().stream().mapToLong(o -> o).sum();
    }

    public long getOnlineTimeMs(long range) {
        ArrayList<Long> durationList = new ArrayList<Long>();
        long start = System.currentTimeMillis() - range;
        for (Map.Entry<Long, Long> entry : this.onlineRecords.entrySet()) {
            long duration;
            long time = entry.getKey();
            if (!MathUtils.isInRange(start, time, time + (duration = entry.getValue().longValue()))) continue;
            if (start > time) {
                duration -= start - time;
            }
            if (duration <= 0L) continue;
            durationList.add(duration);
        }
        return durationList.stream().mapToLong(o -> o).sum();
    }

    public boolean isBanned() {
        return this.banTime > 0L;
    }

    public String getScriptName() {
        return this.scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }
}

