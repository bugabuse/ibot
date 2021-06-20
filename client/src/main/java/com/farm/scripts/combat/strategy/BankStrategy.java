package com.farm.scripts.combat.strategy;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.combat.Combat;
import com.farm.scripts.combat.Settings;
import com.farm.scripts.combat.Strategies;

public class BankStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    public void onAction() {
        if (!Inventory.container().contains(Settings.FOOD_ID) || !this.hasRunes()) {
            if (!Bank.openNearest()) {
                ScriptUtils.interruptCurrentLoop();
            } else {
                if (!Inventory.contains(Settings.FOOD_ID) && Inventory.getFreeSlots() < 15) {
                    Bank.depositAll();
                }

                if (Settings.usingMagic()) {
                    Item[] var1 = Settings.RUNES;
                    int var2 = var1.length;

                    for (int var3 = 0; var3 < var2; ++var3) {
                        Item rune = var1[var3];
                        if (!Inventory.contains(rune.getId(), 5)) {
                            if (!Inventory.container().hasSpace(rune)) {
                                Bank.depositAll();
                                ScriptUtils.interruptCurrentLoop();
                                return;
                            }

                            if (!Bank.getContainer().contains(rune.getId())) {

                                ScriptUtils.interruptCurrentLoop();
                                Combat.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                                return;
                            }

                            Bank.withdraw(rune.getId(), rune.getAmount());
                            Time.waitInventoryChange();
                        }
                    }
                }

                Bank.withdraw(Settings.FOOD_ID, 15);
                ScriptUtils.interruptCurrentLoop();
            }
        }
    }

    private boolean hasRunes() {
        if (Settings.usingMagic()) {
            Item[] var1 = Settings.RUNES;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                Item rune = var1[var3];
                if (!Inventory.contains(rune.getId(), 5)) {
                    return false;
                }
            }
        }

        return true;
    }
}
