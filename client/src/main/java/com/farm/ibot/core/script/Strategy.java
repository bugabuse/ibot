package com.farm.ibot.core.script;

import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.ScriptUtils;

public abstract class Strategy {
    private long nextActivation = 0L;
    private boolean executing;

    public boolean active() {
        return true;
    }

    protected abstract void onAction();

    protected void sleep(int min, int max) {
        this.sleep(Random.human(min, max));
    }

    protected void sleep(int time) {
        this.nextActivation = System.currentTimeMillis() + (long) time;
    }

    public boolean isSleeping() {
        return this.nextActivation > System.currentTimeMillis();
    }

    public boolean isBackground() {
        return false;
    }

    public void execute() {
        this.executing = true;

        try {
            this.onAction();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        this.executing = false;
        if (this.equals(ScriptUtils.getLockedStrategy())) {
            ScriptUtils.unlockStrategyWait();
        }

    }

    public boolean isExecuting() {
        return this.executing;
    }
}
