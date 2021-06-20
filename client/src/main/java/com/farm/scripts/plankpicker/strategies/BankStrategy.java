package com.farm.scripts.plankpicker.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.plankpicker.Plankpicker;
import com.farm.scripts.plankpicker.Strategies;

import java.util.HashMap;

public class BankStrategy extends Strategy {
    public static HashMap<String, Integer> requiredItemNames = new HashMap();

    static {
        requiredItemNames.put("Ring of dueling", 1);
        requiredItemNames.put("Games necklace", 1);
    }

    public static boolean goToBank() {
        ScriptUtils.interruptCurrentLoop();
        if (!WebWalking.canFindPath(Locations.getClosestBank())) {
            if (Inventory.container().contains((i) -> {
                return i.getName().contains("Ring of dueling");
            })) {
                Widgets.closeTopInterface();
                Inventory.container().get((i) -> {
                    return i.getName().contains("Ring of dueling");
                }).interact("Rub");
                if (Time.sleep(() -> {
                    return Widgets.get((w) -> {
                        return w.getText().contains("Castle Wars Arena.");
                    }) != null;
                })) {
                    Dialogue.selectOptionThatContains("Castle Wars Arena.");
                }

                return false;
            } else {
                Magic.LUMBRIDGE_HOME_TELEPORT.select();
                Time.sleep(15000);
                return false;
            }
        } else {
            return Bank.openNearest();
        }
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        for (final String item : BankStrategy.requiredItemNames.keySet()) {
            final int amount = BankStrategy.requiredItemNames.get(item);
            if (Inventory.isFull() || !Inventory.container().contains(i -> !i.getDefinition().isStackable() && i.getName().contains(item)) || (Bank.isOpen() && Inventory.container().getCount(i -> !i.getDefinition().isStackable() && i.getName().contains(item)) < amount)) {
                System.out.println("Bank here.");
                if (!Bank.getCache().contains(i -> i.getName().contains(item)) && !Inventory.container().countNoted().contains(i -> i.getName().contains(item))) {
                    Debug.log((Object) ("Missing " + item));
                    Debug.log((Object) "Starting Grand Exchange strategies.");
                    Plankpicker.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                    return;
                }
                if (Inventory.container().getCount(new String[]{item}) > amount) {
                    Bank.depositAll();
                    return;
                }
                Debug.log((Object) "WOTDRAW LLLL");
                if (!goToBank()) {
                    ScriptUtils.interruptCurrentLoop();
                    return;
                }
                Bank.depositAllExcept(i -> !i.getDefinition().isStackable() && BankStrategy.requiredItemNames.keySet().stream().anyMatch(item2 -> i.getName().contains(item2)));
                if (!Inventory.container().hasSpace(item, amount)) {
                    Bank.depositAll();
                    ScriptUtils.interruptCurrentLoop();
                    return;
                }
                Bank.withdraw(Bank.getCache().get(i -> i.getName().contains(item)).getId(), amount - Inventory.container().getCount(new String[]{item}));
                Time.waitInventoryChange();
                Time.sleep(1000);
            }
        }
    }
}
