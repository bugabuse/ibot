package com.farm.scripts.thiever;

import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.thiever.strategies.*;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(5295, 0, 50000), new WithdrawItem(5300, 0, 50000), new WithdrawItem(5304, 0, 50000)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 499000, PRICES);
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 8000)};
        muleManager.supplyFromMuleActionId = 16;
        muleManager.giveToMuleActionId = 14;
        muleManager.tradeOnWorldGive = 303;
        muleManager.tradeOnWorldResupply = 303;
        muleManager.setup(script, DEFAULT);
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        DEFAULT.add(new WorldEnsure());
        DEFAULT.add(new PouchEmpty());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new EatFoodStrategy());
        DEFAULT.add(new ThieveManStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
