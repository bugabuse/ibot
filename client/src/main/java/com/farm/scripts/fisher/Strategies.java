package com.farm.scripts.fisher;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.fisher.strategies.*;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(377, 0, 180), new WithdrawItem(378, 0, 180), new WithdrawItem(371, 0, 320), new WithdrawItem(372, 0, 320), new WithdrawItem(335, 0, 20), new WithdrawItem(331, 0, 60), new WithdrawItem(359, 0, 88), new WithdrawItem(314, 0, 0)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;

    public static void init(Fisher script) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 27000, PRICES);
        muleManager.itemsToKeep = new WithdrawItem[]{new WithdrawItem(995, 6000)};
        muleManager.supplyFromMuleActionId = 16;
        muleManager.giveToMuleActionId = 15;
        muleManager.receivingMuleTile = Locations.BANK_RIMMINGTON;
        muleManager.resupplyMuleTile = Locations.RESUPPLY_REGULAR_TILE;
        if (FishSettings.powerFishing) {
            muleManager.giveToMuleActionId = -1;
        }

        muleManager.setEnabledCondition(() -> {
            return FishSettings.LOCATION_KARAMJA.distance() > 100;
        });
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new UnpackItems());
        DEFAULT.add(new FishStrategy());
        DEFAULT.add(new DropStrategy());
        DEFAULT.add(new BankStrategy());
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
