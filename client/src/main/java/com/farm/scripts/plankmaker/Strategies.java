package com.farm.scripts.plankmaker;

import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.util.string.DynamicString;
import com.farm.ibot.api.util.string.WebConfigAsyncDynamicString;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.plankmaker.strategies.BankStrategy;
import com.farm.scripts.plankmaker.strategies.BuyPlankStrategy;
import com.farm.scripts.plankmaker.strategies.GrandExchangeStrategy;
import com.farm.scripts.plankmaker.strategies.WorldEnsure;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(1521, 0, 20), new WithdrawItem(1522, 0, 20), new WithdrawItem(8778, 0, 350), new WithdrawItem(8779, 0, 350)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;
    public static DynamicString forceTrade;

    public static void init(MultipleStrategyScript script) {
        forceTrade = new WebConfigAsyncDynamicString("mule_force_trade", 60000L);
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 300000, PRICES);
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 500000), new WithdrawItem(1521, 5000), new WithdrawItem(1522, 5000)};
        muleManager.supplyFromMuleActionId = 12;
        muleManager.giveToMuleActionId = 14;
        muleManager.tradeOnWorldGive = 303;
        muleManager.tradeOnWorldResupply = WorldHopping.getRandomF2p();
        if (forceTrade.intValue() == 1) {
            muleManager.itemsToKeep = null;
            muleManager.wealthToKeep = 0;
            muleManager.wealthToGive = 10000;
            muleManager.supplyFromMuleActionId = -1;
        }

        muleManager.setup(script, DEFAULT);
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        DEFAULT.add(new WorldEnsure());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new BuyPlankStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
