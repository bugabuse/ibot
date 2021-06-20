package com.farm.scripts.farmer.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmtrainer.Strategies;

public class WorldEnsure extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (WorldHopping.isF2p(Client.getCurrentWorld()) || Strategies.muleManager != null && WorldHopping.toRegularWorldNumber(Client.getCurrentWorld()) == WorldHopping.toRegularWorldNumber(Strategies.muleManager.tradeOnWorldGive)) {
            this.hopWorld();
        }
    }

    private void hopWorld() {
        ScriptUtils.interruptCurrentLoop();
        if (Dialogue.isInDialouge()) {
            WebWalking.walkTo(Locations.BANK_LUMBRIDGE, new Tile[0]);
        } else {
            Widgets.closeTopInterface();
            WorldHopping.hop(WorldHopping.getRandomP2p());
        }
    }
}
