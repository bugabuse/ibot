package com.farm.ibot.core.script;

import com.farm.ibot.core.Bot;

import java.util.HashMap;
import java.util.Objects;

public class MultipleStrategyScript extends StrategyScript {
    public static HashMap<Bot, MultipleStrategyScript> instances = new HashMap();
    private StrategyContainer defaultStrategies;
    private StrategyContainer currentlyExecuting;

    public MultipleStrategyScript(StrategyContainer defaultStrategies) {
        super();
        this.defaultStrategies = defaultStrategies;
        instances.put(Bot.get(), this);
    }

    public static MultipleStrategyScript get() {
        return Bot.get().getScriptHandler().getScript() instanceof MultipleStrategyScript ? (MultipleStrategyScript) Bot.get().getScriptHandler().getScript() : (MultipleStrategyScript) instances.get(Bot.get());
    }

    public void setCurrentlyExecutitng(StrategyContainer container) {
        if (!Objects.equals(this.getStrategies(), container)) {
            this.currentlyExecuting = container;
            this.setStrategies(container);
        }

    }

    public StrategyContainer getCurrentlyExecuting() {
        return this.currentlyExecuting;
    }

    public StrategyContainer getDefaultStrategies() {
        return this.defaultStrategies;
    }

    public void setDefaultStrategies(StrategyContainer defaultStrategies) {
        this.defaultStrategies = defaultStrategies;
    }
}
