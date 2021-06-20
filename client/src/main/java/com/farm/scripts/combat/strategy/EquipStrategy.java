package com.farm.scripts.combat.strategy;

import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.combat.Combat;
import com.farm.scripts.combat.EquipmentItem;
import com.farm.scripts.combat.Settings;
import com.farm.scripts.combat.Strategies;

import java.util.ArrayList;
import java.util.HashMap;

public class EquipStrategy extends Strategy {
    boolean loaded = false;

    public boolean active() {
        return true;
    }


    public void onAction() {
        if (!Bank.hasCache()) {
            if (Bank.openNearest()) {
                this.loaded = true;
            }
            ScriptUtils.interruptCurrentLoop();
            return;
        }
        final HashMap<Equipment.Slot, EquipmentItem> bestItems = new HashMap<Equipment.Slot, EquipmentItem>();
        for (final EquipmentItem item : Settings.getEquipmentItems()) {
            if (item.canWear()) {
                bestItems.put(item.slot, item);
            }
        }
        final ArrayList<EquipmentItem> itemsToWear = new ArrayList<EquipmentItem>();
        for (final EquipmentItem item2 : bestItems.values()) {
            final Filter<Item> itemFilter = (Filter<Item>) (i -> !i.getDefinition().isNoted() && i.getName().equalsIgnoreCase(item2.itemName));
            if (!Equipment.isEquipped(item2.slot, (Filter) itemFilter)) {
                if (!Inventory.container().contains((Filter) itemFilter)) {
                    if (Inventory.container().contains(item2.itemName)) {
                        if (Bank.openNearest()) {
                            Bank.depositAll();
                        }
                        ScriptUtils.interruptCurrentLoop();
                        return;
                    }
                    if (Bank.getCache().contains((Filter) itemFilter)) {
                        if (Bank.openNearest()) {
                            Debug.log((Object) itemFilter);
                            if (Inventory.getFreeSlots() > 5) {
                                Bank.withdraw(Bank.getContainer().get((Filter) itemFilter).getId(), 1);
                            } else {
                                Bank.depositAll();
                            }
                        }
                        ScriptUtils.interruptCurrentLoop();
                        return;
                    }
                    if (!Equipment.isEquipped(item2.slot, item2.itemName) && !Inventory.container().contains(item2.itemName) && !Bank.getCache().contains(item2.itemName)) {
                        if (item2.isRequired) {
                            Debug.log((Object) bestItems.values());
                            Debug.log((Object) ("No required item found: " + item2.itemName));
                            ScriptUtils.interruptCurrentLoop();
                            Combat.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                            return;
                        }
                        continue;
                    } else {
                        bestItems.put(item2.slot, item2);
                    }
                } else {
                    Debug.log((Object) ("Wear: " + item2.itemName + Inventory.get(item2.itemName).getDefinition().isNoted()));
                    itemsToWear.add(item2);
                }
            }
        }
        if (itemsToWear.size() > 0) {
            Widgets.closeTopInterface();
            this.equip((EquipmentItem[]) itemsToWear.toArray(new EquipmentItem[0]));
            ScriptUtils.interruptCurrentLoop();
        }
    }

    private void equip(EquipmentItem... items) {
        EquipmentItem[] var2 = items;
        int var3 = items.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            EquipmentItem item = var2[var4];
            Filter<Item> filter = (i) -> {
                return i.getName().startsWith(item.itemName);
            };
            Item inventoryItem = Inventory.container().get(filter);
            if (!Equipment.isEquipped(item.slot, filter) && inventoryItem != null && inventoryItem.interact(ItemMethod.WEAR)) {
                Time.sleep(30, 160);
            }
        }

    }
}
