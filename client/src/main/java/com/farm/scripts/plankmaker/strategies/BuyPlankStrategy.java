package com.farm.scripts.plankmaker.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;

public class BuyPlankStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Client.getRunEnergy() > 30) {
            Walking.setRun(true);
        }

        if (WebWalking.walkTo(new Tile(3302, 3491, 0), 7, new Tile[0])) {
            if (Widgets.get(403, 94) != null) {

                (new Action(0, 26411102, 24, 0, "", "", 0, 0)).send();
                Time.waitInventoryChange();
            } else {
                Npc operator = Npcs.get("Sawmill operator");
                if (operator != null) {
                    operator.interact("Buy-plank");
                    Time.sleep(() -> {
                        return Widgets.get(403, 94) != null;
                    });
                }
            }
        }

    }
}
