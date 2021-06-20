package com.farm.scripts.quester.quests.goblindiplomacy;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.quester.quest.Quest;
import com.farm.scripts.quester.quests.goblindiplomacy.strategy.GetGoblinShit;
import com.farm.scripts.quester.quests.goblindiplomacy.strategy.GrandExchangeStrategy;

import java.awt.*;

public class GoblinDiplomacy extends Quest implements PaintHandler {
    public static StrategyContainer GRAND_EXCHANGE = new StrategyContainer("Grand exchange");
    public static MuleManager muleManager;
    public static GoblinDiplomacy instance;

    public GoblinDiplomacy() {
        super(new GetGoblinShit());
        muleManager = new MuleManager(new OnDemandMuleDynamicString(), new OnDemandMuleDynamicString(), 0, 1000000000, new PriceHandler(new WithdrawItem[0]));
        muleManager.requiredItems = null;
        muleManager.itemsToKeep = null;
        muleManager.supplyFromMuleActionId = 16;
        muleManager.giveToMuleActionId = -1;
        muleManager.setup(this, this.getDefaultStrategies());
        GRAND_EXCHANGE.add(new GrandExchangeStrategy());
        instance = this;
    }

    public void onPaint(Graphics g) {
        g.drawString("Goblin diplomacy", 5, 25);
        g.drawString("State: " + GoblinState.getState(), 5, 45);
        g.drawString("Executing: " + this.getCurrentlyExecuting(), 5, 65);
    }

    public boolean isCompleted() {
        return GoblinState.isInState(GoblinState.QUEST_DONE);
    }

    public String getStateString() {
        return GoblinState.getState().toString();
    }
}
