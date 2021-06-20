package com.farm.scripts.ringenchancer;

import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.scripts.ringenchancer.strategies.BankStrategy;
import com.farm.scripts.ringenchancer.strategies.EnchantStrategy;

public class Strategies {
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");

    public static void init(MultipleStrategyScript script) {
        DEFAULT.add(new EnchantStrategy());
        DEFAULT.add(new BankStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
