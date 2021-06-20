package com.farm.ibot.core;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.util.PaintTimer;

import java.util.ArrayList;

public class FpsData {
    public static int removedBotCount = 0;
    ArrayList<Long> fpsRecords = new ArrayList();
    private PaintTimer timer = new PaintTimer(0L);
    private long lastGameCycle;

    public void update() {
        if (this.timer.getElapsed() > 1000L) {
            if (this.fpsRecords.size() > 1000) {
                this.fpsRecords.remove(0);
            }

            this.fpsRecords.add((long) Client.getGameCycle() - this.lastGameCycle);
            this.lastGameCycle = (long) Client.getGameCycle();
            this.timer.reset();
        }

    }

    public boolean isOverloaded() {
        return this.fpsRecords.size() > 600 && this.getFps(600) < 6L;
    }

    public long getFps() {
        return this.getFps(10);
    }

    public long getFps(int lastSeconds) {
        long sum = 0L;
        int total = 0;

        for (int i = this.fpsRecords.size() - 1; i > 0 && i >= this.fpsRecords.size() - lastSeconds; --i) {
            sum += (Long) this.fpsRecords.get(i);
            ++total;
        }

        return sum > 0L ? sum / (long) total : 0L;
    }

    public void reset() {
        this.fpsRecords.clear();
    }
}
