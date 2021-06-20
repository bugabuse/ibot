package com.farm.scripts.plankpicker;

import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.plankpicker.strategies.BankStrategy;
import com.farm.scripts.plankpicker.strategies.GrandExchangeStrategy;
import com.farm.scripts.plankpicker.strategies.PickPlankStrategy;
import com.farm.scripts.plankpicker.strategies.WalkToMuleSpot;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(960, 0, 250), new WithdrawItem(961, 0, 250)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 80000, PRICES);
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 50000)};
        muleManager.supplyFromMuleActionId = 8;
        muleManager.giveToMuleActionId = 14;
        muleManager.tradeOnWorldGive = 303;
        muleManager.tradeOnWorldResupply = WorldHopping.getRandomF2p();
        muleManager.walkToSpotStrategy = new WalkToMuleSpot();
        muleManager.setup(script, DEFAULT);
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new PickPlankStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
