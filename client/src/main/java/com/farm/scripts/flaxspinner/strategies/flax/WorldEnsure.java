package com.farm.scripts.flaxspinner.strategies.flax;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.flaxspinner.FlaxSpinner;
import com.farm.scripts.flaxspinner.Strategies;

public class WorldEnsure extends Strategy {
    public static boolean ensureMembership() {
        if (Client.isInGame() && !Varbit.MEMBERSHIP_DAYS.booleanValue()) {
            AccountData.current().autostartScript = "&script=FlaxMembershipReady";
            WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + AccountData.current().username + "&script=FlaxMembershipReady");
            Bot.get().getSession().setAccount((AccountData) null);

            while (!Login.logout()) {
                Time.sleep(1000);
            }

            FlaxSpinner.get().getScriptHandler().stop();
            return false;
        } else {
            return true;
        }
    }

    protected void onAction() {
        if (Skill.CRAFTING.getRealLevel() >= 10) {
            Strategies.muleManager.supplyFromMuleActionId = 2;
        }

        if (ensureMembership()) {
            if (WorldHopping.isF2p(Client.getCurrentWorld()) || Strategies.muleManager != null && WorldHopping.toRegularWorldNumber(Client.getCurrentWorld()) == WorldHopping.toRegularWorldNumber(Strategies.muleManager.tradeOnWorldGive)) {
                this.hopWorld();
            }

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
