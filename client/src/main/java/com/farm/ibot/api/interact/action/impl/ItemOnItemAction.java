package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;

public class ItemOnItemAction extends Action {
    private final Item item;
    private final Item item2;

    public ItemOnItemAction(Item item, Item item2) {
        this.item = item;
        this.item2 = item2;
    }

    public static ItemOnItemAction create(Item item, Item item2) {
        return new ItemOnItemAction(item, item2);
    }

    public void send() {
        ItemAction.create(ItemMethod.USE, this.item).send();
        Time.sleep(50, 150);
        ItemAction.create(ItemMethod.ITEM_ON_ITEM, this.item2).send();
    }
}
