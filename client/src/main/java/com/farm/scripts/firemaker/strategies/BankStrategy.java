package com.farm.scripts.firemaker.strategies;

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
import com.farm.scripts.firemaker.Firemaker;
import com.farm.scripts.firemaker.FiremakerSettings;
import com.farm.scripts.firemaker.Strategies;

import java.util.ArrayList;
import java.util.Iterator;

public class BankStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        ArrayList<Item> required = new ArrayList();
        required.add(new Item(590, 1));
        required.add(new Item(FiremakerSettings.getLogToBurn(), 28));
        if (!Inventory.container().containsAllOne((Item[]) required.toArray(new Item[0]))) {
            if (!this.goToBank()) {

                ScriptUtils.interruptCurrentLoop();
                return;
            }

            Bank.depositAllExcept((Item[]) required.toArray(new Item[0]));
            Iterator var2 = required.iterator();

            while (var2.hasNext()) {
                Item item = (Item) var2.next();
                if (!Bank.getContainer().contains(item.getId()) && !Inventory.container().countNoted().contains(item.getId())) {
                    Debug.log("Missing " + item.getId());

                    Firemaker.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                    return;
                }

                if (Inventory.getCount(item.getId()) > item.getAmount()) {
                    Bank.depositAll();
                    return;
                }

                Bank.withdraw(item.getId(), item.getAmount() - Inventory.getCount(item.getId()));
                Time.sleep(300, 600);
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
