package com.farm.ibot.api.util;

import java.util.concurrent.TimeUnit;

public class PaintTimer {
    private long time;

    public PaintTimer() {
        this.reset();
    }

    public PaintTimer(long time) {
        this.time = time;
    }

    public static String msToTime(long millis) {
        String time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return time;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }

    public void reset(long additionalTimeMillis) {
        this.reset();
        this.time += additionalTimeMillis;
    }

    public long getElapsed() {
        return System.currentTimeMillis() - this.time;
    }

    public int getHourRatio(int value) {
        return (int) ((double) value * 3600000.0D / (double) this.getElapsed());
    }

    public long getElapsedSeconds() {
        return this.getElapsed() / 1000L;
    }

    public String getElapsedString() {
        return msToTime(this.getElapsed());
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
