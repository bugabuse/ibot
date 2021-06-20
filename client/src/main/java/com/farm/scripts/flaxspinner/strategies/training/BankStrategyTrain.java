package com.farm.scripts.flaxspinner.strategies.training;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fisher.util.RequiredItem;
import com.farm.scripts.flaxspinner.Constants;
import com.farm.scripts.flaxspinner.FlaxSpinner;
import com.farm.scripts.flaxspinner.Strategies;
import com.farm.scripts.flaxspinner.strategies.flax.WorldEnsure;

public class BankStrategyTrain extends Strategy {
    protected void onAction() {
        Walking.setRun(true);
        if (Skill.CRAFTING.getRealLevel() >= 10) {
            Strategies.muleManager.tradeOnWorldGive = Strategies.tradeOnWorld;
            FlaxSpinner.get().setDefaultStrategies(Strategies.DEFAULT_FLAXING);
            FlaxSpinner.get().setCurrentlyExecutitng(Strategies.DEFAULT_FLAXING);
            ScriptUtils.interruptCurrentLoop();
        } else if (WorldEnsure.ensureMembership()) {
            if (!Inventory.container().containsAllOne(Constants.getTrainingItems())) {
                if (Bank.openNearest(Locations.GRAND_EXCHANGE)) {
                    Bank.depositAllExcept(Constants.getTrainingItems());
                    RequiredItem[] var1 = Constants.getTrainingItems();
                    int var2 = var1.length;

                    for (int var3 = 0; var3 < var2; ++var3) {
                        RequiredItem item = var1[var3];
                        Debug.log(item.getName());
                        if (!Inventory.contains(item.getId(), 1)) {
                            if (Bank.getContainer().contains(item.getId(), 1)) {
                                Bank.withdraw(item.getId(), item.getAmountToWithdrawFromBank() - Inventory.getCount(item.getId()));
                                Time.waitInventoryChange();
                            } else if (!Inventory.contains(item.getId())) {
                                Debug.log("Missing " + item.getName());
                                FlaxSpinner.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE_TRAINING);
                            }
                        }
                    }
                }

            }
        }
    }
}
