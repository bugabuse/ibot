package com.farm.scripts.fletcher;

import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.fletcher.strategies.BankStrategy;
import com.farm.scripts.fletcher.strategies.FletchStrategy;
import com.farm.scripts.fletcher.strategies.GrandExchangeStrategy;
import com.farm.scripts.fletcher.strategies.WorldEnsure;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(58, 0, 30), new WithdrawItem(56, 0, 30), new WithdrawItem(851, 0, 230), new WithdrawItem(62, 0, 60), new WithdrawItem(66, 0, 380), new WithdrawItem(855, 0, 560)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 600000000, PRICES);
        muleManager.supplyFromMuleActionId = 17;
        muleManager.giveToMuleActionId = 14;
        muleManager.tradeOnWorldGive = 303;
        muleManager.tradeOnWorldResupply = WorldHopping.getRandomF2p();
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 1000000000)};
        muleManager.setup(script, DEFAULT);
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        DEFAULT.add(new WorldEnsure());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new FletchStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
