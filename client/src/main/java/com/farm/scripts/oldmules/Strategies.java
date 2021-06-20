package com.farm.scripts.oldmules;

import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.oldmules.strategy.BankStrategy;

public class Strategies {
    public static final int CLAY = 434;
    public static final int SOFT_CLAY = 1761;
    public static final int RUNE_DEATH = 560;
    public static final PriceHandler PRICES;
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static MuleManager muleManager;
    public static int JUG_OF_WINE = 1993;
    public static int JUG_EMPTY = 1935;
    public static int JUG_OF_WATER = 1937;

    static {
        PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(1359, 0, 2000), new WithdrawItem(1358, 0, 2000), new WithdrawItem(1357, 0, 2000), new WithdrawItem(JUG_EMPTY + 1, 0, 2), new WithdrawItem(JUG_EMPTY, 0, 2), new WithdrawItem(JUG_OF_WINE + 1, 0, 15), new WithdrawItem(JUG_OF_WINE, 0, 15), new WithdrawItem(JUG_OF_WATER, 0, 20), new WithdrawItem(JUG_OF_WATER + 1, 0, 20), new WithdrawItem(560, 0, 250), new WithdrawItem(561, 0, 250), new WithdrawItem(1521, 0, 60), new WithdrawItem(1522, 0, 60), new WithdrawItem(1515, 0, 300), new WithdrawItem(1516, 0, 300), new WithdrawItem(435, 0, 50), new WithdrawItem(434, 0, 50), new WithdrawItem(1761, 0, 100), new WithdrawItem(1762, 0, 100), new WithdrawItem(1618, 0, 3000), new WithdrawItem(1624, 0, 1000), new WithdrawItem(1622, 0, 500), new WithdrawItem(1620, 0, 2000), new WithdrawItem(1739, 0, 100), new WithdrawItem(1740, 0, 100), new WithdrawItem(2133, 0, 80), new WithdrawItem(527, 0, 80)});
    }

    public static void init(OldMules script) {
        WebWalking.enableRunningCondition = WebWalking.RUNNING_MANUAL;
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), 0, 2000, PRICES);
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new BankStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
