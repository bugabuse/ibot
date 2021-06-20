package com.farm.ibot.api.util.string;

import com.farm.ibot.init.AccountData;

public class AccountPlaytimeDynamicString extends DynamicString {
    private final long updateRatioMs;
    private long lastUpdate;
    private long timeMillis;

    public AccountPlaytimeDynamicString() {
        this(60000L);
    }

    public AccountPlaytimeDynamicString(long updateRatioMs) {
        this.timeMillis = -1L;
        this.updateRatioMs = updateRatioMs;
    }

    public String toString() {
        return "" + this.intValue();
    }

    public int intValue() {
        if (System.currentTimeMillis() - this.lastUpdate > this.updateRatioMs) {
            AccountData data = AccountData.current();
            if (this.timeMillis == -1L) {
                this.timeMillis = data.fetchPlayTime(4800, 0);
            } else {
                (new Thread(() -> {
                    this.timeMillis = data.fetchPlayTime(4800, 0);
                })).start();
            }

            this.lastUpdate = System.currentTimeMillis();
        }

        return (int) this.timeMillis;
    }
}
