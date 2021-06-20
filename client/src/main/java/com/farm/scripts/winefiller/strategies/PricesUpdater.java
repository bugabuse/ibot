package com.farm.scripts.winefiller.strategies;

import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.winefiller.Strategies;
import com.farm.scripts.winefiller.WineFiller;

public class PricesUpdater extends Strategy {
    public boolean active() {
        return WineFiller.currentState == WineFiller.GRAND_EXCHANGE;
    }

    public void onAction() {
        GrandExchangeStrategy.sellWineFor = OsbuddyExchange.forId(Strategies.JUG_OF_WATER).getSellAverage() - 1;
        GrandExchangeStrategy.buyWineFor = 6;
        this.sleep(10000);
    }

    public boolean isBackground() {
        return true;
    }
}
