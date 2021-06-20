package com.farm.scripts.bondbuyer.strategies;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.bondbuyer.BondBuyer;
import com.farm.scripts.bondbuyer.Strategies;

public class BankStrategy extends Strategy {
    public static final int BOND = 13190;
    public static final int BOND_REEDEMABLE = 13192;

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Varbit.MEMBERSHIP_DAYS.booleanValue()) {
            BondBuyer.onMembershipActivated();
        } else {
            Widget widget = Widgets.get((w) -> {
                return w.isRendered() && w.getText().contains("Sending redemption request");
            });
            if (widget != null) {
                BondBuyer.onMembershipActivated();
            } else {
                if (!Inventory.container().contains(13192)) {
                    if (!this.goToBank()) {

                        ScriptUtils.interruptCurrentLoop();
                        return;
                    }

                    Bank.depositAllExcept(new int[]{13192, 995});
                    if (!Bank.getContainer().contains(13192) && !Inventory.container().countNoted().contains(13192)) {

                        BondBuyer.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                        return;
                    }

                    if (Bank.getCache().getFreeSlots() < 1) {
                        Bank.depositAll();
                        return;
                    }

                    Bank.withdraw(13192, 1);
                    Time.sleep(300, 600);
                    ScriptUtils.interruptCurrentLoop();
                }

            }
        }
    }

    private boolean goToBank() {
        ScriptUtils.interruptCurrentLoop();
        if (!WebWalking.canFindPath(Locations.getClosestBank())) {
            Magic.LUMBRIDGE_HOME_TELEPORT.select();
            Time.sleep(15000);
            return false;
        } else {
            return Bank.openNearest();
        }
    }
}
