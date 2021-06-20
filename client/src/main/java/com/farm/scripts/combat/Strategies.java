package com.farm.scripts.combat;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.combat.strategy.*;
import com.farm.scripts.combat.strategy.ge.GrandExchangeStrategy;

public class Strategies {
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1)});
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;

    private static void initAccountTraining(Combat Combat) {
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 0, PRICES);
        muleManager.supplyFromMuleActionId = 8;
        muleManager.giveToMuleActionId = -1;
        muleManager.resupplyMuleTile = Locations.RESUPPLY_REGULAR_TILE;
        muleManager.setup(Combat, DEFAULT);
        Combat.maxCombat = 126;
        DEFAULT.add(new EatStrategy());
        DEFAULT.add(new BonesStrategy());
        DEFAULT.add(new EquipStrategy());
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new AttackStrategyNew());
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        Combat.setCurrentlyExecutitng(DEFAULT);
    }

    public static void init(Combat Combat) {
        if (!Combat.getStartArguments().contains("for_mule") && !Combat.getStartArguments().contains("acctrainer")) {

            if (Combat.getStartArguments().contains("woodcutter")) {
                Combat.maxCombat = 15;
            }

            DEFAULT.add(new AttackStrategy());
            Combat.setCurrentlyExecutitng(DEFAULT);
        } else {

            initAccountTraining(Combat);
        }

    }
}
