package com.farm.scripts.flaxspinner;

import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.flaxspinner.strategies.flax.BankStrategy;
import com.farm.scripts.flaxspinner.strategies.flax.SpinStrategy;
import com.farm.scripts.flaxspinner.strategies.flax.WorldEnsure;
import com.farm.scripts.flaxspinner.strategies.ge.GrandExchangeStrategy;
import com.farm.scripts.flaxspinner.strategies.training.BankStrategyTrain;
import com.farm.scripts.flaxspinner.strategies.training.CraftingStrategy;

import java.util.Arrays;
import java.util.Random;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(1609, 0, 200), new WithdrawItem(1610, 0, 200), new WithdrawItem(1625, 0, 200), new WithdrawItem(1626, 0, 200), new WithdrawItem(1777, 0, 110), new WithdrawItem(1778, 0, 110)});
    public static int tradeOnWorld = (new Random()).nextInt(2) == 1 ? 303 : 304;
    public static StrategyContainer DEFAULT_TRAINING = new StrategyContainer("Default Training");
    public static StrategyContainer DEFAULT_FLAXING = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE_TRAINING = new StrategyContainer("Grand Exchange(leather)");
    public static StrategyContainer GRAND_EXCHANGE_FLAXING = new StrategyContainer("Grand Exchange");
    public static MuleManager muleManager;

    public static void init(FlaxSpinner script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 250000, PRICES);
        muleManager.supplyFromMuleActionId = 8;
        muleManager.giveToMuleActionId = 14;
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 35000)};
        muleManager.setup(script, DEFAULT_FLAXING);
        muleManager.tradeOnWorldGive = tradeOnWorld;
        muleManager.tradeOnWorldResupply = tradeOnWorld;
        DEFAULT_TRAINING.add(new BankStrategyTrain());
        DEFAULT_TRAINING.add(new WorldEnsure());
        DEFAULT_TRAINING.add(new CraftingStrategy());
        DEFAULT_FLAXING.add(new WorldEnsure());
        DEFAULT_FLAXING.add(new SpinStrategy());
        DEFAULT_FLAXING.add(new BankStrategy());
        GRAND_EXCHANGE_TRAINING.add((new GrandExchangeStrategy(script, muleManager, Arrays.asList(Constants.getTrainingItems()))).membersOnly(true));
        GRAND_EXCHANGE_FLAXING.add((new GrandExchangeStrategy(script, muleManager, Arrays.asList(Constants.ITEMS_FLAXING))).membersOnly(true));
        script.setCurrentlyExecutitng(DEFAULT_TRAINING);
    }
}
