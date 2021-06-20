package com.farm.scripts.fletcher.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fisher.util.RequiredItem;
import com.farm.scripts.fletcher.Constants;
import com.farm.scripts.fletcher.Fletching;
import com.farm.scripts.fletcher.Strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class BankStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        ArrayList<RequiredItem> required = new ArrayList(Arrays.asList(Constants.getRequiredItems().getItems()));
        if (!Inventory.container().containsAllOne((Item[]) required.toArray(new Item[0]))) {
            if (!this.goToBank()) {
                ScriptUtils.interruptCurrentLoop();
                return;
            }

            Bank.depositAllExcept((Item[]) required.toArray(new Item[0]));
            Iterator var2 = required.iterator();

            while (var2.hasNext()) {
                RequiredItem item = (RequiredItem) var2.next();
                if (Bank.getContainer().getCount(new int[]{item.getId()}) + Inventory.container().countNoted().getCount(new int[]{item.getId()}) < item.getAmountMinimum()) {
                    Debug.log("Missing " + item.getId());

                    Fletching.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                    return;
                }

                if (Inventory.getCount(item.getId()) > item.getAmountToWithdrawFromBank()) {
                    Bank.depositAll();
                    return;
                }

                int amount = item.getAmountToWithdrawFromBank() - Inventory.getCount(item.getId());
                if (amount > 0) {
                    Bank.doAction(false, item.getId(), amount, false, false);
                    Time.sleep(100, 300);
                }
            }

            Keyboard.press(27);
            Time.sleep(200, 300);
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
