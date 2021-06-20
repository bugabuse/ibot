package com.farm.scripts.web;

public class DepositSession {
    private DepositSessionState state;
    private DepositSessionType type;
    private String hash;
    private int amount;
    private int userId;
    private String playerIngameName = "";
    private String botWorld = "";
    private String botName = "";

    public DepositSessionState getState() {
        return this.state;
    }

    public void setState(DepositSessionState state) {
        this.state = state;
    }

    public String getHash() {
        return this.hash;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getPlayerIngameName() {
        return this.playerIngameName;
    }

    public String getBotWorld() {
        return this.botWorld;
    }

    public String getBotName() {
        return this.botName;
    }

    public DepositSessionType getType() {
        return this.type;
    }
}
