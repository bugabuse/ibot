package com.farm.scripts.farmtrainer;

import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.farmtrainer.strategies.*;
import com.farm.scripts.farmtrainer.strategies.outfit.GrandExchangeOutfitStrategy;
import com.farm.scripts.farmtrainer.strategies.outfit.OutfitWearStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static StrategyContainer FILLING_CAN = new StrategyContainer("Filling can");
    public static StrategyContainer BUY_PLANTS = new StrategyContainer("Buying plants");
    public static StrategyContainer GRAND_EXCHANGE_OUTFIT = new StrategyContainer("Grand exchange outfit");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 9300000, PRICES);
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 1500000)};
        muleManager.supplyFromMuleActionId = 13;
        muleManager.tradeOnWorldGive = 303;
        muleManager.tradeOnWorldResupply = WorldHopping.getRandomF2p();
        muleManager.setup(script, DEFAULT);
        GRAND_EXCHANGE_OUTFIT.add(new GrandExchangeOutfitStrategy());
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        FILLING_CAN.add(new FillWateringCan());
        DEFAULT.add(new WorldEnsure());
        DEFAULT.add(new OutfitWearStrategy());
        DEFAULT.add(new HouseEnsureStrategy());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new MakePlants());
        BUY_PLANTS.add(new BuyPlants());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
