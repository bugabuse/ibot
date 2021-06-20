/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.transaction;

public class TransactionRunnable {
    private final Runnable runnable;
    public boolean isCompleted = false;

    public TransactionRunnable(Runnable r) {
        this.runnable = r;
    }

    public void execute() {
        this.isCompleted = false;
        this.runnable.run();
        this.isCompleted = true;
    }
}

