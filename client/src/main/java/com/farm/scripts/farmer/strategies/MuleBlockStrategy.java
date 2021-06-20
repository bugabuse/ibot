package com.farm.scripts.farmer.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmer.Strategies;

public class MuleBlockStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (WebWalking.canFindPath(Locations.GRAND_EXCHANGE)) {
            Strategies.muleManager.giveToMuleActionId = 14;
        } else {
            Strategies.muleManager.giveToMuleActionId = -1;
        }

    }
}
