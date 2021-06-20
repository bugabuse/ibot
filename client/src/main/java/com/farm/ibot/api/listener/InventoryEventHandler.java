package com.farm.ibot.api.listener;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;

import java.util.ArrayList;

public class InventoryEventHandler extends EventHandler {
    private InventoryListener listener;
    private ItemContainer lastInventory;

    public InventoryEventHandler(InventoryListener listener) {
        this.listener = listener;
    }

    public int listen() {
        if (this.lastInventory != null && this.lastInventory.isLoaded()) {
            ItemContainer current = Inventory.container();
            ArrayList<Integer> checked = new ArrayList();
            if (current != null) {
                Item[] var3 = current.getItems();
                int var4 = var3.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    Item item = var3[var5];
                    if (item.getId() > 0 && !checked.contains(item.getId())) {
                        checked.add(item.getId());
                        if (!this.lastInventory.contains(item.getId(), current.getCount(item.getId()))) {
                            this.listener.onItemAdded(new Item(item.getId(), current.getCount(item.getId()) - this.lastInventory.getCount(item.getId()), item.getSlot(), item.getWidget()));
                        }

                        if (!current.contains(item.getId(), this.lastInventory.getCount(item.getId()))) {
                            this.listener.onItemRemoved(new Item(item.getId(), this.lastInventory.getCount(item.getId()) - current.getCount(item.getId()), item.getSlot(), item.getWidget()));
                        }
                    }
                }

                this.lastInventory = current;
            }

            return 200;
        } else {
            this.lastInventory = Inventory.container();
            return 200;
        }
    }
}
