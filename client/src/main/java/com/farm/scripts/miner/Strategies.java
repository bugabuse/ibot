package com.farm.scripts.miner;

import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.miner.strategies.AntiCombatStrategy;
import com.farm.scripts.miner.strategies.BankStrategy;
import com.farm.scripts.miner.strategies.MiningStrategy;
import com.farm.scripts.miner.strategies.ge.GrandExchangeListener;
import com.farm.scripts.miner.strategies.ge.GrandExchangeStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(434, 0, 70), new WithdrawItem(440, 0, 120), new WithdrawItem(1617, 0, 8000), new WithdrawItem(1623, 0, 2000), new WithdrawItem(1621, 0, 1000), new WithdrawItem(1619, 0, 5000)});
    public static MuleManager muleManager;
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand Exchange");

    public static void init(Miner script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), 21000, Random.next(8000, 12000), PRICES);
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new AntiCombatStrategy());
        DEFAULT.add(new MiningStrategy());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new GrandExchangeListener());
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
