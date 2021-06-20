package com.farm.scripts.mulereceiver.strategy;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.mulereceiver.MuleReceiver;

import java.util.Iterator;
import java.util.Map.Entry;

public class BankEnsureStrategy extends Strategy {
    static boolean loaded = false;

    public boolean active() {
        return true;
    }

    public void onAction() {
        Debug.log("Open: " + Bank.isOpen());
        if (!loaded || !Bank.hasCache()) {
            Bot.get().getScriptHandler().loginRandom.active = true;
            Bot.get().getScriptHandler().antiKick.active = true;
        }

        if (!loaded || !Bank.hasCache()) {

            if (Bank.openNearest()) {
                loaded = true;
                Iterator var1 = MuleReceiver.PRICES.priceList.iterator();

                while (var1.hasNext()) {
                    Entry<Integer, Integer> entry = (Entry) var1.next();
                    if (Bank.getCache().contains((Integer) entry.getKey()) && Bank.openNearest()) {
                        if (Inventory.container().getFreeSlots() < 2) {
                            Bank.depositAll();
                        }

                        Bank.withdraw(true, (Integer) entry.getKey(), Bank.getCache().getCount(new int[]{(Integer) entry.getKey()}));
                        ScriptUtils.interruptCurrentLoop();
                    }
                }
            }
        }

    }
}
