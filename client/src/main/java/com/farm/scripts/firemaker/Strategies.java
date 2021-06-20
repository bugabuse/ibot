package com.farm.scripts.firemaker;

import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.firemaker.strategies.BankStrategy;
import com.farm.scripts.firemaker.strategies.GrandExchangeStrategy;
import com.farm.scripts.firemaker.strategies.MakeFireStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 300000, PRICES);
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 500000)};
        muleManager.supplyFromMuleActionId = 2;
        muleManager.giveToMuleActionId = -1;
        muleManager.setup(script, DEFAULT);
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new MakeFireStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
