package com.farm.scripts.farmtrainer.strategies;

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
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmtrainer.FarmingTrainer;
import com.farm.scripts.farmtrainer.Strategies;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class BankStrategy extends Strategy {
    public static Item[] requiredItems;
    public static HashMap<String, Integer> requiredItemNames;

    static {
        BankStrategy.requiredItems = new Item[]{new Item(8431, 20), new Item(8013, 30), new Item(8007, 10)};
        (BankStrategy.requiredItemNames = new HashMap<String, Integer>()).put("Ring of dueling", 1);
        BankStrategy.requiredItemNames.put("Watering can(", 3);
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Debug.log((Object) "TEST");
        final List<Item> required = Arrays.asList(BankStrategy.requiredItems);
        if (!Bank.hasCache() || !Inventory.container().containsAllOne((Item[]) required.toArray(new Item[0]))) {
            Debug.log((Object) "BANK KURWO ");
            if (!this.goToBank()) {
                ScriptUtils.interruptCurrentLoop();
                return;
            }
            Bank.depositAllExcept(i -> BankStrategy.requiredItemNames.keySet().stream().anyMatch(item2 -> i.getName().contains(item2)) || Arrays.stream(BankStrategy.requiredItems).anyMatch(item2 -> item2.getId() == i.getId()));
            for (final Item item : BankStrategy.requiredItems) {
                Debug.log((Object) (item.getAmount() - Inventory.getCount(item.getId())));
                if (item.getAmount() - Inventory.getCount(item.getId()) < 0) {
                    Bank.deposit(item.getId(), Inventory.getCount(item.getId()) - item.getAmount());
                    return;
                }
                if (item.getId() != 5331 && !Bank.getCache().contains(item.getId()) && !Inventory.container().countNoted().contains(item.getId())) {
                    if (item.getId() == 8431) {
                        Debug.log((Object) "Starting plant buying strategies.");
                        FarmingTrainer.get().setCurrentlyExecutitng(Strategies.BUY_PLANTS);
                    } else {
                        Debug.log((Object) ("Missing " + item.getId()));
                        Debug.log((Object) "Starting Grand Exchange strategies.");
                        FarmingTrainer.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                    }
                    return;
                }
                if (!Inventory.container().hasSpace(item)) {
                    Bank.depositAll();
                    return;
                }
                Bank.withdraw(item.getId(), item.getAmount() - Inventory.getCount(item.getId()));
                Time.waitInventoryChange();
                Time.sleep(1000);
            }
        } else {
            if ((Bank.getCache().contains(i -> i.getName().contains("Watering can")) || Inventory.container().contains(i -> i.getName().contains("Watering can"))) && !Bank.getCache().contains(i -> i.getName().contains("Watering can(")) && !Inventory.container().contains(i -> i.getName().contains("Watering can("))) {
                Debug.log((Object) "Switching to watering can strategies");
                FarmingTrainer.get().setCurrentlyExecutitng(Strategies.FILLING_CAN);
                ScriptUtils.interruptCurrentLoop();
                return;
            }
            for (final String item2 : BankStrategy.requiredItemNames.keySet()) {
                final int amount = BankStrategy.requiredItemNames.get(item2);
                if (!Inventory.container().contains(i -> !i.getDefinition().isStackable() && i.getName().contains(item2)) || (Bank.isOpen() && Inventory.container().getCount(i -> !i.getDefinition().isStackable() && i.getName().contains(item2)) < amount)) {
                    if (Bank.getCache().get(i -> i.getName().contains(item2)) == null) {
                        Debug.log((Object) "Null etc lol");
                    } else {
                        if (!Bank.getCache().contains(i -> i.getName().contains(item2)) && !Inventory.container().countNoted().contains(i -> i.getName().contains(item2))) {
                            Debug.log((Object) ("Missing " + item2));
                            Debug.log((Object) "Starting Grand Exchange strategies.");
                            FarmingTrainer.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                            return;
                        }
                        Debug.log((Object) "WOTDRAW LLLL");
                        if (!this.goToBank()) {
                            ScriptUtils.interruptCurrentLoop();
                            return;
                        }
                        Bank.depositAllExcept(i -> (!i.getDefinition().isStackable() && BankStrategy.requiredItemNames.keySet().stream().anyMatch(n -> i.getName().contains(n))) || Arrays.stream(BankStrategy.requiredItems).anyMatch(n -> n.getId() == i.getId()));
                        if (!Inventory.container().hasSpace(item2, amount)) {
                            Bank.depositAll();
                            ScriptUtils.interruptCurrentLoop();
                            return;
                        }
                        Bank.withdraw(Bank.getCache().get(i -> i.getName().contains(item2)).getId(), amount - Inventory.container().getCount(new String[]{item2}));
                        Time.waitInventoryChange();
                        Time.sleep(1000);
                    }
                }
            }
        }
    }

    private boolean goToBank() {
        ScriptUtils.interruptCurrentLoop();
        if (WebWalking.canFindPath(Locations.getClosestBank())) {
            return Bank.openNearest();
        }
        if (Inventory.container().contains(i -> i.getName().contains("Ring of dueling"))) {
            Widgets.closeTopInterface();
            Inventory.container().get(i -> i.getName().contains("Ring of dueling")).interact("Rub");
            if (Time.sleep(() -> Widgets.get(w -> w.getText().contains("Castle Wars Arena.")) != null)) {
                Dialogue.selectOptionThatContains("Castle Wars Arena.");
            }
            return false;
        }
        Magic.LUMBRIDGE_HOME_TELEPORT.select();
        Time.sleep(15000);
        return false;
    }
}
