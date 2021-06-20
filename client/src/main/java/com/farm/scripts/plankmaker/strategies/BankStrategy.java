package com.farm.scripts.plankmaker.strategies;

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
import com.farm.scripts.plankmaker.Plankmaker;
import com.farm.scripts.plankmaker.Strategies;

import java.util.ArrayList;
import java.util.Arrays;

public class BankStrategy extends Strategy {
    public static Item[] requiredItems = new Item[]{new Item(995, 10000), new Item(1521, 27)};

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Strategies.forceTrade.intValue() == 1) {
            this.goToBank();
        } else {
            ArrayList<Item> required = new ArrayList(Arrays.asList(requiredItems));
            if (!Inventory.container().containsAll((Item[]) required.toArray(new Item[0]))) {
                if (!this.goToBank()) {
                    ScriptUtils.interruptCurrentLoop();
                    return;
                }

                Bank.depositAllExcept(requiredItems);
                Item[] var2 = requiredItems;
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    Item item = var2[var4];
                    if (!Bank.getContainer().contains(item.getId()) && !Inventory.container().countNoted().contains(item.getId(), item.getAmount())) {
                        if (item.getId() == 995) {
                            Strategies.muleManager.activateResupplyState();
                            return;
                        }

                        Debug.log("Missing " + item.getId());

                        Plankmaker.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                        return;
                    }

                    if (item.getId() != 995 && Inventory.getCount(item.getId()) > item.getAmount()) {
                        Bank.depositAll();
                        return;
                    }

                    int amount = item.getAmount() - Inventory.getCount(item.getId());
                    if (item.getId() == 995) {
                        amount = 500000;
                    }

                    Bank.withdraw(item.getId(), amount);
                    Time.sleep(300, 600);
                }

                ScriptUtils.interruptCurrentLoop();
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
