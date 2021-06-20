package com.farm.scripts.gambling;

import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.scripts.gambling.strategies.GamblingStrategy;
import com.farm.scripts.gambling.strategies.TradeStrategy;
import com.farm.scripts.gambling.strategies.TradeStrategyWinner;

public class Strategies {
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");

    public static void init(MultipleStrategyScript script) {
        DEFAULT.add(new GamblingStrategy());
        DEFAULT.add(new TradeStrategy());
        DEFAULT.add(new TradeStrategyWinner());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
