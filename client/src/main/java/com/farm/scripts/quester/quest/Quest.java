package com.farm.scripts.quester.quest;

import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.StrategyContainer;

import java.util.Arrays;

public abstract class Quest extends MultipleStrategyScript {
    public Quest(Strategy... strategies) {
        super(new StrategyContainer("Default"));
        this.getDefaultStrategies().addAll(Arrays.asList(strategies));
        this.setCurrentlyExecutitng(this.getDefaultStrategies());
    }

    public abstract boolean isCompleted();

    public abstract String getStateString();
}
