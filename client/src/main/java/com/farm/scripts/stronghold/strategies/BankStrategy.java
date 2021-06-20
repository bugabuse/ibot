package com.farm.scripts.stronghold.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.stronghold.Strategies;
import com.farm.scripts.stronghold.StrongholdMoney;
import com.farm.scripts.stronghold.util.RewardBox;

public class BankStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        RewardBox box = DoStrongholdStrategy.getRewardBox();
        if (box != null) {
            if (!DoStrongholdStrategy.isAtStronghold() && Inventory.container().getCount(new int[]{1993}) < 10) {
                ScriptUtils.interruptCurrentLoop();
                Walking.setRun(false);
                if (this.goToBank()) {
                    if (Bank.depositAllExcept(new int[]{1993})) {
                        if (Inventory.container().getCount(new int[]{1993}) < 10) {
                            if (!Bank.getContainer().contains(1993)) {
                                StrongholdMoney.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                            } else {
                                Bank.openAndWithdraw(new Item[]{new Item(1993, 15)});
                            }
                        } else {
                            Bank.openAndWithdraw(new Item[]{new Item(1993, 15)});
                        }
                    }
                }
            }
        }
    }

    private boolean goToBank() {
        ScriptUtils.interruptCurrentLoop();
        if (!WebWalking.canFindPath(Locations.GRAND_EXCHANGE)) {
            Magic.LUMBRIDGE_HOME_TELEPORT.select();
            Time.sleep(15000);
            return false;
        } else {
            return Bank.openNearest(Locations.GRAND_EXCHANGE);
        }
    }
}
