package com.farm.scripts.woodcutter;

import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.woodcutter.strategy.ge.GrandExchangeListener;
import com.farm.scripts.woodcutter.strategy.ge.GrandExchangeStrategy;
import com.farm.scripts.woodcutter.strategy.main.BankStrategy;
import com.farm.scripts.woodcutter.strategy.main.ChopStrategy;
import com.farm.scripts.woodcutter.strategy.main.DropStrategy;
import com.farm.scripts.woodcutter.strategy.main.OakAccountSwitchStrategy;

public class Strategies {
    public static final BankStrategy BANK_STRATEGY = new BankStrategy();
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(1511, 0, OsbuddyExchange.forId(1511).getSellAverage()), new WithdrawItem(1512, 0, OsbuddyExchange.forId(1511).getSellAverage()), new WithdrawItem(1521, 0, OsbuddyExchange.forId(1521).getSellAverage()), new WithdrawItem(1522, 0, OsbuddyExchange.forId(1521).getSellAverage()), new WithdrawItem(1515, 0, 200), new WithdrawItem(1516, 0, 200)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand Exchange");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        if (ChopSettings.powerChopping) {
            initPowerChopping(script);
        } else {
            initNormal(script);
        }

    }

    public static void initPowerChopping(MultipleStrategyScript script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 6000, 0, PRICES);
        muleManager.supplyFromMuleActionId = 16;
        muleManager.giveToMuleActionId = -1;
        DEFAULT.add(new GrandExchangeListener());
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new ChopStrategy());
        DEFAULT.add(new DropStrategy());
        DEFAULT.add(BANK_STRATEGY);
        DEFAULT.add(new OakAccountSwitchStrategy());
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }

    public static void initNormal(MultipleStrategyScript script) {
        WebWalking.enableRunningCondition = WebWalking.RUNNING_MANUAL;
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 6000, Random.next(35000, 45000), PRICES);
        muleManager.supplyFromMuleActionId = 11;
        DEFAULT.add(new GrandExchangeListener());
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new ChopStrategy());
        DEFAULT.add(BANK_STRATEGY);
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
