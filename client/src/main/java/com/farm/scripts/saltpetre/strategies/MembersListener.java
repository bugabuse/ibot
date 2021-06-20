package com.farm.scripts.saltpetre.strategies;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.saltpetre.Strategies;

public class MembersListener extends Strategy {
    public boolean active() {
        return !AccountData.current().isMembers;
    }

    protected void onAction() {
        Item bond = Inventory.get(13192);
        if (bond != null) {
            Widget confirm = Widgets.get(66, 81);
            if (Widgets.get((w) -> {
                return w.getText().contains("Select a membership package");
            }) != null) {
                Widgets.get((w) -> {
                    return w.getText().contains("14 days");
                }).interact("");
                Time.sleep(1000);
            } else if (confirm != null) {
                confirm.interact("");
                this.sleep(60000);
            } else {
                bond.interact("Redeem");
                Time.sleep(5000);
            }
        } else if (Bank.openNearest()) {
            if (!Time.sleep(() -> {
                return Bank.getContainer().contains(13192);
            })) {

                Strategies.muleManager.activateResupplyState();
            } else {
                Bank.withdraw(13192, 1);
            }
        }

    }
}
