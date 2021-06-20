package com.farm.scripts.fletcher.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fletcher.Strategies;

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
            WebWalking.walkTo(Player.getLocal().getPosition(), -1, new Tile[0]);
        } else {
            Widgets.closeTopInterface();
            WorldHopping.hop(WorldHopping.getRandomP2p());
        }
    }
}
