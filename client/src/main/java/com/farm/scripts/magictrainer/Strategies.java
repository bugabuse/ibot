package com.farm.scripts.magictrainer;

import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.scripts.magictrainer.strategies.TrainMagicStrategy;

public class Strategies {
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");

    public static void init(MultipleStrategyScript script) {
        DEFAULT.add(new TrainMagicStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
