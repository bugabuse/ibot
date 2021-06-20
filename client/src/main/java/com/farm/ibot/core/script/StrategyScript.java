// Decompiled with: Procyon 0.5.36
package com.farm.ibot.core.script;

import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;

import java.util.Arrays;
import java.util.Iterator;

public class StrategyScript extends BotScript {
    private StrategyContainer strategies;
    private boolean isExecuting;
    private StrategyExecuteThread currentStrategyLoopThread;
    private Strategy waitStrategy;

    public StrategyScript(final Strategy... strategies) {
        this.strategies = new StrategyContainer();
        this.isExecuting = false;
        this.currentStrategyLoopThread = null;
        this.waitStrategy = null;
        this.addStrategy(strategies);
    }

    public void addStrategy(final Strategy... strategies) {
        if (strategies != null) {
            this.strategies.addAll(Arrays.asList(strategies));
        }
    }

    @Override
    public void onStart() {
    }

    @Override
    public int onLoop() {
        for (final Strategy s : this.strategies) {
            if (s.isBackground() && !s.isSleeping() && !s.isExecuting() && s.active()) {
                new Thread(Thread.currentThread().getThreadGroup(), s::execute).start();
            }
        }
        Debug.log("Loop 1 - " + !this.isExecuting + " " + (this.waitStrategy == null) + " " + (this.currentStrategyLoopThread == null) + " " + (this.currentStrategyLoopThread != null && !this.currentStrategyLoopThread.isAlive()) + " " + (this.currentStrategyLoopThread != null && this.currentStrategyLoopThread.isInterrupted()));
        if ((!this.isExecuting && this.waitStrategy == null) || this.currentStrategyLoopThread == null || !this.currentStrategyLoopThread.isAlive() || this.currentStrategyLoopThread.isInterrupted()) {
            this.isExecuting = true;

            final Iterator<Strategy> iterator2 = this.strategies.iterator();

            (this.currentStrategyLoopThread = new StrategyExecuteThread(Thread.currentThread().getThreadGroup(), () -> {
                Debug.log("Looping strategies. " + this.strategies.size());
                try {
                    this.strategies.iterator().hasNext();
                    while (iterator2.hasNext()) {
                        Strategy s2 = iterator2.next();
                        Debug.log("Check str: " + s2.toString());
                        if (!s2.isBackground() && !s2.isSleeping() && s2.active()) {
                            Debug.log("Execute str " + s2.toString());
                            s2.execute();
                        }
                        if (this.currentStrategyLoopThread.isStoppedExecutingCurrentLoop()) {

                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.isExecuting = false;
                return;
            })).start();
        }
        return this.loopInterval();
    }

    public void waitForStrategy(final Strategy waitStrategy) {
        this.waitStrategy = waitStrategy;
        ScriptUtils.interruptCurrentLoop();
    }

    public void unlockStrategyWait() {
        this.waitStrategy = null;
    }

    public StrategyExecuteThread getCurrentStrategyLoopThread() {
        return this.currentStrategyLoopThread;
    }

    public int loopInterval() {
        return 400;
    }

    public void clearStrategies() {
        this.strategies.clear();
    }

    public Strategy getWaitStrategy() {
        return this.waitStrategy;
    }

    public StrategyContainer getStrategies() {
        return this.strategies;
    }

    public void setStrategies(final Strategy... strategies) {
        if (strategies != null) {
            this.strategies.clear();
            this.strategies.addAll(Arrays.asList(strategies));
        }
    }

    public void setStrategies(final StrategyContainer container) {
        this.strategies = container;

        if (this.currentStrategyLoopThread != null && !this.currentStrategyLoopThread.isInterrupted()) {
            this.currentStrategyLoopThread.interrupt();
        }
    }
}
