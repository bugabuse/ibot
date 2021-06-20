package com.farm.scripts.cooker.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.cooker.Cooker;
import com.farm.scripts.cooker.CookerSettings;
import com.farm.scripts.cooker.Strategies;

import java.util.ArrayList;
import java.util.Iterator;

public class BankStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        ArrayList<Item> required = new ArrayList();
        required.add(new Item(590, 1));
        required.add(new Item(CookerSettings.getFishToCook(), 26));
        if (!Inventory.container().containsAllOne((Item[]) required.toArray(new Item[0]))) {
            if (!this.goToBank()) {
                ScriptUtils.interruptCurrentLoop();
                return;
            }

            CookStrategy.fireTile = null;
            ArrayList<Item> requiredTemp = new ArrayList(required);
            requiredTemp.add(new Item(CookerSettings.getLogToBurn(), 1));
            Bank.depositAllExcept((Item[]) requiredTemp.toArray(new Item[requiredTemp.size()]));
            Iterator var3 = required.iterator();

            while (var3.hasNext()) {
                Item item = (Item) var3.next();
                if (!Bank.getContainer().contains(item.getId()) && !Inventory.container().countNoted().contains(item.getId())) {
                    Debug.log("Missing " + item.getId());

                    Cooker.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                    return;
                }

                if (Inventory.getCount(item.getId()) > item.getAmount()) {
                    Bank.depositAll();
                    return;
                }

                Bank.withdraw(item.getId(), item.getAmount() - Inventory.getCount(item.getId()));
                Time.waitInventoryChange();
            }

            ScriptUtils.interruptCurrentLoop();
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
