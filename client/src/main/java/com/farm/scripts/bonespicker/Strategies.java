package com.farm.scripts.bonespicker;

import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.world.area.PolygonArea;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.bonespicker.strategies.BankStrategy;
import com.farm.scripts.bonespicker.strategies.PickStrategy;
import com.farm.scripts.bonespicker.util.CowSpot;

public class Strategies {
    public static final CowSpot[] COW_SPOTS = new CowSpot[]{new CowSpot((new Tile(3259, 3279, 0)).setNote("Lumbridge East"), 20), new CowSpot((new Tile(3203, 3292, 0)).setNote("Lumbridge North"), 11), new CowSpot((new Tile(3177, 3328, 0)).setNote("Lumbridge North Big"), new PolygonArea(new Tile[]{new Tile(3178, 3317), new Tile(3183, 3314), new Tile(3192, 3308), new Tile(3199, 3309), new Tile(3302, 3322), new Tile(3199, 3333), new Tile(3187, 3339), new Tile(3176, 3341), new Tile(3160, 3346), new Tile(3156, 3345), new Tile(3153, 3335), new Tile(3152, 3324), new Tile(3157, 3315), new Tile(3169, 3318), new Tile(3176, 3316)}))};
    public static final PriceHandler PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(995, 0, 1), new WithdrawItem(1739, 0, 100), new WithdrawItem(1740, 0, 100), new WithdrawItem(2132, 0, 80), new WithdrawItem(2133, 0, 80), new WithdrawItem(526, 0, 80), new WithdrawItem(527, 0, 80)});
    public static CowSpot COW_SPOT = null;
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");
    public static MuleManager muleManager;

    public static void init(BonesPicker script) {
        WebWalking.enableRunningCondition = WebWalking.RUNNING_MANUAL;
        COW_SPOT = COW_SPOTS[script.currentSpotId()];
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), 0, Random.next(10000, 12000), PRICES);
        muleManager.setup(script, DEFAULT);
        DEFAULT.add(new BankStrategy());
        DEFAULT.add(new PickStrategy());
        script.setCurrentlyExecutitng(DEFAULT);
    }
}
