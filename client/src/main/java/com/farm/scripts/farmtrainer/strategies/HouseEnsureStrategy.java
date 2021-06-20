package com.farm.scripts.farmtrainer.strategies;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmtrainer.Strategies;

public class HouseEnsureStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (!Varbit.HAS_HOUSE.booleanValue()) {
            ScriptUtils.interruptCurrentLoop();
            if (!Inventory.contains(995, 1000)) {
                if (!Bank.getCache().contains(995, 1000)) {
                    Strategies.muleManager.activateResupplyState();
                    return;
                }

                Bank.openAndWithdraw(new Item[]{new Item(995, 1000)});
            }

            if (WebWalking.walkTo(new Tile(3240, 3475), 8, new Tile[0])) {
                if (Dialogue.talkTo("Estate agent", true)) {
                    Dialogue.goNext(new String[]{"How can I get a house?", "Yes"});
                    Time.sleep(2000, 2300);
                }

            }
        }
    }
}
