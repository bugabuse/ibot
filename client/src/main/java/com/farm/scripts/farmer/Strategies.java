package com.farm.scripts.farmer;

import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.farmer.api.Herb;
import com.farm.scripts.farmer.strategies.*;
import com.farm.scripts.farmer.strategies.ge.GrandExchangeStrategy;

public class Strategies {
    public static final PriceHandler PRICES;
    public static StrategyContainer DEFAULT = new StrategyContainer("Just farming");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static Herb herbToFarm;
    public static MuleManager muleManager;

    static {
        herbToFarm = Herb.TOADFLAX;
        PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(207, 0, 7500), new WithdrawItem(208, 0, 7500), new WithdrawItem(8431, 0, 1800), new WithdrawItem(8432, 0, 1800), new WithdrawItem(3049, 0, 3000), new WithdrawItem(3050, 0, 3000)});
    }

    public static void init(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 500000, PRICES);
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 500000)};
        muleManager.supplyFromMuleActionId = 12;
        muleManager.giveToMuleActionId = -1;
        muleManager.determineBankCache = false;
        muleManager.tradeOnWorldGive = 303;
        muleManager.tradeOnWorldResupply = WorldHopping.getRandomF2p();
        DEFAULT.add(new MuleBlockStrategy());
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new WorldEnsure());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new FarmingStateListener(script));
        DEFAULT.add(new MaintainPatch(script));
        DEFAULT.add(new WalkToPatch());
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
