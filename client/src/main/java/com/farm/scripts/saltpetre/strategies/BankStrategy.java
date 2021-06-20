package com.farm.scripts.saltpetre.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;

public class BankStrategy extends Strategy {
    public boolean active() {
        return AccountData.current().isMembers && (Inventory.isFull() || !Inventory.contains(952));
    }

    protected void onAction() {
        if (WorldHopping.isF2p(Client.getCurrentWorld())) {
            if (WorldHopping.hop(WorldHopping.getRandomP2p())) {
                Time.sleep(3000, () -> {
                    return GameObjects.get("Saltpetre") != null;
                });
            }

        } else {
            Walking.setRun(true);
            if (WebWalking.walkTo(Locations.BANK_ZEAH, new Tile[0])) {
                if (Bank.open()) {
                    Bank.depositAllExcept(new int[]{952});
                    if (!Inventory.contains(952)) {
                        Bank.withdraw(952, 1);
                    }
                }

            }
        }
    }
}
