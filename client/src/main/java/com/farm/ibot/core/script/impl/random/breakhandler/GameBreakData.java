package com.farm.ibot.core.script.impl.random.breakhandler;

import com.farm.ibot.api.util.Random;

public class GameBreakData {
    public String breakName = "";
    public double intervalPerHour;
    public boolean logOffGame;
    public long minWaitTime;
    public long maxWaitTime;

    public GameBreakData(String breakName, double intervalPerHour, boolean logOffGame, long minWaitTime, long maxWaitTime) {
        this.breakName = breakName;
        this.intervalPerHour = intervalPerHour;
        this.logOffGame = logOffGame;
        this.minWaitTime = minWaitTime;
        this.maxWaitTime = maxWaitTime;
    }

    public static GameBreakData generateIngameBreak() {
        return new GameBreakData("Ingame Break", Random.next(20.0D, 35.0D), false, (long) Random.next(1000, 3000), (long) Random.next(4000, 15000));
    }

    public static GameBreakData generateSmallBreak() {
        return new GameBreakData("Small Break", Random.next(1.0D, 3.0D), false, (long) Random.next(20000, 50000), (long) Random.next(70000, 130000));
    }

    public static GameBreakData generateBigBreak() {
        return new GameBreakData("Big Break", Random.next(0.5D, 1.5D), true, 120000L, 600000L);
    }
}
