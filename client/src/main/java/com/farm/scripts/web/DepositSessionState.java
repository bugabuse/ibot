package com.farm.scripts.web;

public enum DepositSessionState {
    WAIT_FOR_BOT(false),
    IN_PROGRESS(false),
    IN_PROGRESS_TRADING_WITHDRAW(false),
    IN_PROGRESS_TRADING_DEPOSIT(false),
    EXPIRED(true),
    CANCELED(true),
    COMPLETED(true);

    private boolean completed;

    private DepositSessionState(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return this.completed;
    }
}
