package com.farm.scripts.thiever.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.thiever.Strategies;
import com.farm.scripts.thiever.Thiever;

import java.util.ArrayList;
import java.util.Arrays;

public class BankStrategy extends Strategy {
    public static Item[] requiredItems = new Item[]{new Item(1993, 22)};

    public boolean active() {
        return true;
    }

    protected void onAction() {
        ArrayList<Item> required = new ArrayList(Arrays.asList(requiredItems));
        if (!Inventory.container().containsAllOne((Item[]) required.toArray(new Item[0]))) {
            if (Inventory.container().getCount(new String[]{"Coin pouch"}) > 0) {
                Inventory.get("Coin pouch").interact(ItemMethod.EAT);
                return;
            }

            if (!this.goToBank()) {

                ScriptUtils.interruptCurrentLoop();
                return;
            }

            Bank.depositAllExcept(requiredItems);
            Item[] var2 = requiredItems;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Item item = var2[var4];
                if (!Bank.getContainer().contains(item.getId()) && !Inventory.container().countNoted().contains(item.getId())) {
                    Debug.log("Missing " + item.getId());

                    Thiever.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
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
