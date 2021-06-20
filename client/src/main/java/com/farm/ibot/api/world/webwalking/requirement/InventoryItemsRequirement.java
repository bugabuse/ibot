package com.farm.ibot.api.world.webwalking.requirement;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.wrapper.item.Item;

public class InventoryItemsRequirement extends Requirement {
    private final Item[] items;

    public InventoryItemsRequirement(Item... items) {
        this.items = items;
    }

    public boolean hasPassed() {
        return Inventory.container().containsAll(this.items);
    }
}
