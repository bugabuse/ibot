package com.farm.ibot.core.script;

public class StrategyExecuteThread extends Thread {
    private boolean stoppedExecutingCurrentLoop = false;

    public StrategyExecuteThread(ThreadGroup group, Runnable runnable) {
        super(group, runnable);
    }

    public void stopExecutingCurrentLoop() {
        this.stoppedExecutingCurrentLoop = true;
    }

    public boolean isStoppedExecutingCurrentLoop() {
        return this.stoppedExecutingCurrentLoop;
    }
}
