package com.farm.scripts.christmas;

import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.christmas.strategies.ChristmasStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(1038, 0, 5000), new WithdrawItem(1039, 0, 5000), new WithdrawItem(1040, 0, 5000), new WithdrawItem(1041, 0, 5000), new WithdrawItem(1042, 0, 5000), new WithdrawItem(1043, 0, 5000), new WithdrawItem(1044, 0, 5000), new WithdrawItem(1045, 0, 5000), new WithdrawItem(1046, 0, 5000), new WithdrawItem(1047, 0, 5000), new WithdrawItem(1048, 0, 5000), new WithdrawItem(1049, 0, 5000), new WithdrawItem(1050, 0, 5000), new WithdrawItem(1051, 0, 5000), new WithdrawItem(962, 0, 5000), new WithdrawItem(963, 0, 5000)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        DEFAULT.add(new ChristmasStrategy());
        script.getScriptHandler().webNotFoundRandom.active = false;
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 1000, PRICES);
        muleManager.giveToMuleActionId = 1;
        muleManager.supplyFromMuleActionId = -1;
        muleManager.requireMuleHandlerAssigned = false;
        muleManager.setup(script, DEFAULT);
        muleManager.determineBankCache = false;
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
