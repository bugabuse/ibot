package com.farm.scripts.birthday;

import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.birthday.strategies.BirthdayStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(1989, 0, 15000), new WithdrawItem(1990, 0, 15000)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        DEFAULT.add(new BirthdayStrategy());
        script.getScriptHandler().webNotFoundRandom.active = false;
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 1000, PRICES);
        muleManager.giveToMuleActionId = 1;
        muleManager.supplyFromMuleActionId = -1;
        muleManager.requireMuleHandlerAssigned = false;
        muleManager.setup(script, DEFAULT);
        muleManager.determineBankCache = false;
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
