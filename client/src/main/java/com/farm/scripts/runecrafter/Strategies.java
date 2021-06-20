package com.farm.scripts.runecrafter;

import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.scripts.runecrafter.strategies.crafting.BankStrategy;
import com.farm.scripts.runecrafter.strategies.crafting.CraftingStrategy;
import com.farm.scripts.runecrafter.strategies.crafting.IdleStrategy;
import com.farm.scripts.runecrafter.strategies.slave.DeliverStrategy;
import com.farm.scripts.runecrafter.strategies.slave.SlaveBankStrategy;
import com.farm.scripts.runecrafter.strategies.slave.WorldSwitchStrategy;
import com.farm.scripts.runecrafter.strategies.slave.grandexchange.GrandExchangeListener;
import com.farm.scripts.runecrafter.strategies.slave.grandexchange.GrandExchangeStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand Exchange");
    public static MuleManager muleManager;

    public static void init(MultipleStrategyScript script) {
        WebWalking.enableRunningCondition = WebWalking.RUNNING_ON_COMBAT_OR_ENOUGH_ENERGY;
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        if (Constants.IS_SLAVE) {
            Debug.log(AccountData.current().getGameUsername() + " is a slave account. ");
            DEFAULT.add(new WorldSwitchStrategy());
            DEFAULT.add(new GrandExchangeListener());
            DEFAULT.add(new SlaveBankStrategy());
            DEFAULT.add(new DeliverStrategy());
        } else if (Constants.IS_USING_SLAVES) {
            Debug.log(AccountData.current().getGameUsername() + " is a normal account. With slaves. ");
            DEFAULT.add(new CraftingStrategy());
            DEFAULT.add(new IdleStrategy(script));
        } else {

            DEFAULT.add(new GrandExchangeListener());
            DEFAULT.add(new BankStrategy());
            DEFAULT.add(new CraftingStrategy());
        }

        script.setCurrentlyExecutitng(DEFAULT);
    }
}
