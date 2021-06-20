/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.util;

import java.util.concurrent.TimeUnit;

public class Timer {
    private long time;

    public Timer() {
        this.reset();
    }

    public Timer(long time) {
        this.time = time;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }

    public long getElapsed() {
        return System.currentTimeMillis() - this.time;
    }

    public int getHourRatio(int value) {
        return (int)((double)value * 3600000.0 / (double)this.getElapsed());
    }

    public long getElapsedSeconds() {
        return this.getElapsed() / 1000L;
    }

    public String getElapsedString() {
        long millis = this.getElapsed();
        String time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return this.time;
    }
}

