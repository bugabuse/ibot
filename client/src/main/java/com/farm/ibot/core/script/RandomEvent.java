package com.farm.ibot.core.script;

public abstract class RandomEvent extends BotScript {
    public boolean active = true;
    public long nextCheck = 0L;

    public abstract boolean isEnabled();

    public abstract boolean isBackground();

    public long checkInterval() {
        return 3000L;
    }
}
