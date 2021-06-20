package com.farm.scripts.thiever.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.thiever.Strategies;

import java.util.Calendar;
import java.util.TimeZone;

public class WorldEnsure extends Strategy {
    public WebConfigDynamicString jagexWorktimeAutoTansfer = new WebConfigDynamicString("jagex_worktime_auto_transfer", 900000L);

    protected void onAction() {
        if (this.jagexWorktimeAutoTansfer.intValue() == 1) {
            Calendar rightNow = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
            int hour = rightNow.get(11);
            if (MathUtils.isBetween(hour, 7, 12)) {
                Strategies.muleManager.wealthToGive = 100000;
            }
        } else {
            Strategies.muleManager.wealthToGive = 199000;
        }

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
