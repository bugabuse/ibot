package com.farm.scripts.farmer.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmer.api.FarmingPatch;
import com.farm.scripts.farmer.api.PatchState;

public class WalkToPatch extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        FarmingPatch patch = FarmingPatch.current();
        if (patch == null) {
            this.walkNextPatch();
        } else {
            if (PatchState.fromConfig(patch.getName()).equals(PatchState.GROWN)) {

                Bot.get().getScriptHandler().loginRandom.active = true;
                Bot.get().getScriptHandler().antiKick.active = true;
            } else {
                this.walkNextPatch();
            }

        }
    }

    private void walkNextPatch() {
        Widgets.closeTopInterface();
        FarmingPatch patch = null;
        FarmingPatch[] var2 = FarmingPatch.getPatches();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            FarmingPatch p = var2[var4];
            if (PatchState.fromConfig(p.getName()).equals(PatchState.GROWN)) {
                patch = p;
                break;
            }
        }

        if (patch != null) {
            Bot.get().getScriptHandler().loginRandom.active = true;
            Bot.get().getScriptHandler().antiKick.active = true;
            if (Walking.isInPath(Player.getLocal().getPosition(), patch.getPatchFromTeleport(), 20)) {
                WebWalking.walkTo(patch.getTile(), 2, new Tile[0]);
            } else if (patch.getName().equals("Falador")) {
                Inventory.get("Falador teleport").interact("Break");
                Time.waitRegionChange();
            } else if (patch.getName().equals("Catherby") || patch.getName().equals("Ardougne")) {
                Inventory.get("Camelot teleport").interact("Break");
                Time.waitRegionChange();
            }
        } else {
            Bot.get().getScriptHandler().loginRandom.active = false;
            Bot.get().getScriptHandler().antiKick.active = false;
            Login.logout();
        }

    }
}
