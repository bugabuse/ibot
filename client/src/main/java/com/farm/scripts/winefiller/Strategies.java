package com.farm.scripts.winefiller;

import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.SeedRandom;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.winefiller.strategies.BankStrategy;
import com.farm.scripts.winefiller.strategies.JugFillStrategy;
import com.farm.scripts.winefiller.strategies.PricesUpdater;
import com.farm.scripts.winefiller.strategies.ShopStrategy;

public class Strategies {
    public static final WineSpot[] spots;
    public static int JUG_OF_WINE = 1993;
    public static int JUG_EMPTY = 1935;
    public static int JUG_OF_WATER = 1937;
    public static int JUG_TO_WITHDRAW;
    public static MuleManager muleManager;
    public static StrategyContainer DEFAULT;
    public static PriceHandler PRICES;
    public static JugFillStrategy jugFillingStrategy;

    static {
        JUG_TO_WITHDRAW = JUG_EMPTY;
        spots = new WineSpot[]{(new WineSpot()).fountain("Fountain").tile((new Tile(3239, 3433, 0)).setNote("Varrock")), (new WineSpot()).fountain("Waterpump").tile((new Tile(2949, 3382, 0)).setNote("Falador"))};
        DEFAULT = new StrategyContainer("Default");
        PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(JUG_OF_WATER, 0, 10), new WithdrawItem(JUG_OF_WATER + 1, 0, 10)});
    }

    public static void init(MultipleStrategyScript script) {
        PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(JUG_OF_WATER, 0, 10), new WithdrawItem(JUG_OF_WATER + 1, 0, 10), new WithdrawItem(ItemDefinition.forName("Raw tuna").getUnnotedId(), 0, 50), new WithdrawItem(ItemDefinition.forName("Raw tuna").getUnnotedId() + 1, 0, 50), new WithdrawItem(ItemDefinition.forName("Tuna").getUnnotedId(), 0, 50), new WithdrawItem(ItemDefinition.forName("Tuna").getUnnotedId() + 1, 0, 50), new WithdrawItem(ItemDefinition.forName("Raw salmon").getUnnotedId(), 0, 50), new WithdrawItem(ItemDefinition.forName("Raw salmon").getUnnotedId() + 1, 0, 50), new WithdrawItem(ItemDefinition.forName("Salmon").getUnnotedId(), 0, 50), new WithdrawItem(ItemDefinition.forName("Salmon").getUnnotedId() + 1, 0, 50), new WithdrawItem(ItemDefinition.forName("Raw trout").getUnnotedId(), 0, 50), new WithdrawItem(ItemDefinition.forName("Raw trout").getUnnotedId() + 1, 0, 50), new WithdrawItem(ItemDefinition.forName("Trout").getUnnotedId(), 0, 50), new WithdrawItem(ItemDefinition.forName("Trout").getUnnotedId() + 1, 0, 50), new WithdrawItem(ItemDefinition.forName("Raw sardine").getUnnotedId(), 0, 50), new WithdrawItem(ItemDefinition.forName("Sardine").getUnnotedId() + 1, 0, 50)});
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 30000, PRICES);
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(JUG_EMPTY, 2000)};
        muleManager.supplyFromMuleActionId = 16;
        muleManager.setup(script, DEFAULT);
        WebWalking.enableRunningCondition = WebWalking.RUNNING_ALWAYS_ON;
        jugFillingStrategy = new JugFillStrategy();
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(jugFillingStrategy);
        DEFAULT.add(new ShopStrategy());
        DEFAULT.add(new PricesUpdater());
        script.setCurrentlyExecutitng(DEFAULT);
    }

    public static WineSpot getSpot() {
        return spots[(new SeedRandom(AccountData.seed())).next(0, spots.length)];
    }
}
