package com.farm.scripts.shopper;

import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.shopper.strategy.ge.GrandExchangeStrategy;
import com.farm.scripts.shopper.strategy.main.OpenPackStrategy;
import com.farm.scripts.shopper.strategy.main.ShopStrategy;
import com.farm.scripts.shopper.strategy.main.UpdateItemsStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(558, 0, 15), new WithdrawItem(560, 0, 250)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand Exchange");

    public static void init(MultipleStrategyScript script) {
        MuleManager muleManager = new MuleManager(new OnDemandMuleDynamicString(), 30000, 60000, PRICES);
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new UpdateItemsStrategy());
        DEFAULT.add(new ShopStrategy());
        DEFAULT.add(new OpenPackStrategy());
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        muleManager.withGeStrategies(GRAND_EXCHANGE);
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
