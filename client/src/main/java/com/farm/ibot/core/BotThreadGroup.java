package com.farm.ibot.core;

public class BotThreadGroup extends ThreadGroup {
    private final Bot bot;

    public BotThreadGroup(BotThreadGroup parent, String name) {
        this(parent.getBot(), name);
    }

    public BotThreadGroup(Bot bot, String name) {
        super(name);
        this.bot = bot;
        this.setMaxPriority(10);
    }

    public BotThreadGroup(ThreadGroup parent, Bot bot, String name) {
        super(parent, name);
        this.bot = bot;
        this.setMaxPriority(10);
    }

    public Bot getBot() {
        return this.bot;
    }
}
