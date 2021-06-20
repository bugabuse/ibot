package com.farm.ibot.api.listener;

import com.farm.ibot.api.wrapper.item.Item;

public interface InventoryListener extends Listener {
    void onItemAdded(Item var1);

    default void onItemRemoved(Item item) {
    }
}
