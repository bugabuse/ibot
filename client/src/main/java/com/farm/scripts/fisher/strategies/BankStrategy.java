package com.farm.scripts.fisher.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.DepositBox;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fisher.FishSettings;
import com.farm.scripts.fisher.Fisher;
import com.farm.scripts.fisher.Strategies;
import com.farm.scripts.fisher.util.RequiredItem;

public class BankStrategy extends Strategy {
    boolean hasCache = false;

    public static boolean hasFishingEquipment() {
        return Inventory.container().containsAll(FishSettings.getConfig().getFishingEquipment());
    }

    public boolean active() {
        return Inventory.isFull() && FishSettings.getConfig().isBankingEnabled() || !hasFishingEquipment();
    }

    public void onAction() {
        boolean depositBox = FishSettings.getConfig().isUsingDepositBox() && hasFishingEquipment() && this.hasCache;
        if (!depositBox) {
            if (!Bank.openNearest()) {
                return;
            }

            if (Bank.open()) {
                this.hasCache = true;
                Bank.depositAllExcept(FishSettings.getConfig().getFishingEquipment());
            }

            RequiredItem[] var2 = FishSettings.getConfig().getFishingEquipment();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                RequiredItem item = var2[var4];
                if (!Inventory.container().contains(item.getId(), item.getAmountMinimum())) {
                    if (!Bank.getContainer().contains(item.getId(), item.getAmountMinimum())) {
                        Debug.log("NO FISHING EQUIPMENT IN BANK BROOOO " + item.getName());
                        ScriptUtils.interruptCurrentLoop();
                        if (item.getId() == 995) {
                            Strategies.muleManager.activateResupplyState();
                        } else {
                            Fisher.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                        }

                        return;
                    }

                    Bank.withdraw(item.getId(), item.getAmountToWithdrawFromBank());
                    Time.waitInventoryChange();
                    Time.sleep(1000, 2000);
                }
            }
        } else {
            if (!DepositBox.openNearest()) {
                return;
            }


            if (DepositBox.open()) {

                DepositBox.depositAllExcept(FishSettings.getConfig().getFishingEquipment());
            }
        }

    }
}
