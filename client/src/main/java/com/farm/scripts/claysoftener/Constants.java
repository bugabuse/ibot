package com.farm.scripts.claysoftener;

import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.claysoftener.strategies.BankStrategy;
import com.farm.scripts.claysoftener.strategies.BucketFillStrategy;
import com.farm.scripts.claysoftener.strategies.SoftenClayStrategy;
import com.farm.scripts.claysoftener.strategies.grandexchange.GrandExchangeListenStrategy;
import com.farm.scripts.claysoftener.strategies.grandexchange.GrandExchangeStrategy;
import com.farm.scripts.claysoftener.strategies.grandexchange.PricesUpdater;

public class Constants {
    public static final Tile TILE_FOUNTAIN = new Tile(3192, 3471, 0);
    public static final int BUCKET_OF_WATER_REQUIRED_COUNT = 14;
    public static final int BUCKET_OF_WATER = 1929;
    public static final int BUCKET = 1925;
    public static final int CLAY = 434;
    public static final int SOFT_CLAY = 1761;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_GRAND_EXCHANGE = 2;
    public static final int STATE_GRAND_EXCHANGE_PREPARE = 1;
    public static final int STATE_MULE_TRANSFER_SUPPLY_WAIT = 3;
    public static final int STATE_MULE_TRANSFER = 4;
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(435, 800, 190), new WithdrawItem(434, 800, 190), new WithdrawItem(995, 0, 1), new WithdrawItem(1761, 0, 190), new WithdrawItem(1762, 0, 190)});
    public static MuleManager muleManager;
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand Exchange");
    public static int currentState = 0;
    public static boolean needBuckets = false;
    private static String[] states = new String[]{"Soften clay", "Preparing for Grand Exchange", "Grand Exchange", "Wait for mule", "Trade with mule"};

    public static String getCurrentState() {
        return states[currentState];
    }

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), 27000, Random.next(15000, 18000), PRICES);
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new PricesUpdater());
        DEFAULT.add(new GrandExchangeListenStrategy());
        DEFAULT.add(new GrandExchangeStrategy());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new BucketFillStrategy());
        DEFAULT.add(new SoftenClayStrategy(script));
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
