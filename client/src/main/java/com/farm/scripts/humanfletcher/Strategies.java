package com.farm.scripts.humanfletcher;

import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.humanfletcher.strategies.*;

public class Strategies {
    public static final StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand Exchange");
    public static final Tile STAND_TILE = new Tile(3162, 3489);
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(62, 0, 60), new WithdrawItem(63, 0, 60), new WithdrawItem(851, 0, 290), new WithdrawItem(852, 0, 290), new WithdrawItem(855, 0, 530), new WithdrawItem(856, 0, 530)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static MuleManager muleManager;
    public static LongbowsStringingStrategy longbowsStringingStrategy;
    public static AccountTermination accountTermination = new AccountTermination();

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 500000, 2000000, PRICES);
        muleManager.supplyFromMuleActionId = 12;
        muleManager.giveToMuleActionId = 14;
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 1000000)};
        muleManager.tradeOnWorldGive = 303;
        muleManager.tradeOnWorldResupply = WorldHopping.getRandomF2p();
        muleManager.setup(script, DEFAULT);
        GRAND_EXCHANGE.add(accountTermination);
        muleManager.muleStrategies.add(accountTermination);
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        DEFAULT.add(new WorldEnsure());
        longbowsStringingStrategy = new LongbowsStringingStrategy();
        DEFAULT.add(longbowsStringingStrategy);
        DEFAULT.add(new UnfinishedLongbowsStrategy());
        DEFAULT.add(accountTermination);
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
