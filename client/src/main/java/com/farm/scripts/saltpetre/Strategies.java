package com.farm.scripts.saltpetre;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.saltpetre.strategies.BankStrategy;
import com.farm.scripts.saltpetre.strategies.DigStrategy;
import com.farm.scripts.saltpetre.strategies.MembersListener;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(13421, 0, 200)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        WebWalking.enableRunningCondition = WebWalking.RUNNING_MANUAL;
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, Random.next(250000, 350000), PRICES);
        muleManager.giveToMuleActionId = 6;
        muleManager.supplyFromMuleActionId = 5;
        muleManager.receivingMuleTile = Locations.BANK_ZEAH;
        muleManager.resupplyMuleTile = Locations.RESUPPLY_REGULAR_TILE;
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new MembersListener());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new DigStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
