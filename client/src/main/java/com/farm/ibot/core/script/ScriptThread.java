package com.farm.ibot.core.script;

public class ScriptThread extends Thread {
    public boolean stopped = false;

    public ScriptThread(ThreadGroup tg, Runnable runnable, String name) {
        super(tg, runnable, name);
    }

    public void stopThread() {
        this.stopped = true;
    }

    public boolean isStopped() {
        return this.stopped;
    }
}
