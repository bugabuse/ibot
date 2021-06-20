package com.farm.scripts.stronghold;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.stronghold.strategies.BankStrategy;
import com.farm.scripts.stronghold.strategies.DoStrongholdStrategy;
import com.farm.scripts.stronghold.strategies.GrandExchangeStrategy;
import com.farm.scripts.stronghold.strategies.HealStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 1000, PRICES);
        muleManager.supplyFromMuleActionId = -1;
        muleManager.giveToMuleActionId = 1;
        muleManager.determineBankCache = false;
        muleManager.setEnabledCondition(() -> {
            return DoStrongholdStrategy.getRewardBox() == null && Client.getLoginState() == 30 && !DoStrongholdStrategy.isAtStronghold();
        });
        muleManager.setup(script, DEFAULT);
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        DEFAULT.add(new HealStrategy());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new DoStrongholdStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
