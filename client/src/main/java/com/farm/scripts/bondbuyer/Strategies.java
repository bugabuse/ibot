package com.farm.scripts.bondbuyer;

import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.bondbuyer.strategies.BankStrategy;
import com.farm.scripts.bondbuyer.strategies.GrandExchangeStrategy;
import com.farm.scripts.bondbuyer.strategies.RedeemBondStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 0, PRICES);
        muleManager.supplyFromMuleActionId = 5;
        muleManager.giveToMuleActionId = -1;
        muleManager.tradeOnWorldResupply = 308;
        muleManager.tradeOnWorldGive = 308;
        muleManager.setEnabledCondition(() -> {
            return !Varbit.MEMBERSHIP_DAYS.booleanValue();
        });
        muleManager.setup(script, DEFAULT);
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new RedeemBondStrategy(script));
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
