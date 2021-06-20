package com.farm.scripts.claysoftener.strategies.grandexchange;

import com.farm.ibot.api.util.string.OsbuddyItemDynamicString;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.claysoftener.Constants;

public class PricesUpdater extends Strategy {
    OsbuddyItemDynamicString sellClayFor = new OsbuddyItemDynamicString(1761, false);
    OsbuddyItemDynamicString buyClayFor = new OsbuddyItemDynamicString(434, true);

    public boolean active() {
        return Constants.currentState == 2;
    }

    public void onAction() {

        GrandExchangeStrategy.sellClayFor = this.sellClayFor.intValue() != -1 ? this.sellClayFor.intValue() - 7 : GrandExchangeStrategy.sellClayFor;
        GrandExchangeStrategy.buyClayFor = this.buyClayFor.intValue() != -1 ? this.buyClayFor.intValue() + 7 : GrandExchangeStrategy.buyClayFor;
        this.sleep(10000);
    }

    public boolean isBackground() {
        return true;
    }
}
