package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IGrandExchangeItem;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

import java.util.ArrayList;

public class GrandExchangeItem extends Wrapper {
    private int index = 0;

    public GrandExchangeItem(Object instance) {
        super(instance);
    }

    public static IGrandExchangeItem getGrandExchangeItemInterface() {
        return Bot.get().accessorInterface.grandExchangeItemInterface;
    }

    public static GrandExchangeItem[] getItems(Filter<GrandExchangeItem> filter) {
        ArrayList<GrandExchangeItem> temp = new ArrayList();
        GrandExchangeItem[] var2 = getSlots();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            GrandExchangeItem item = var2[var4];
            if (filter.accept(item)) {
                temp.add(item);
            }
        }

        return (GrandExchangeItem[]) temp.toArray(new GrandExchangeItem[temp.size()]);
    }

    public static GrandExchangeItem[] getItems() {
        return getItems((i) -> {
            return i.getState() > 0;
        });
    }

    public static GrandExchangeItem[] getSlots() {
        GrandExchangeItem[] items = (GrandExchangeItem[]) getStatic("GrandExchangeItem.Items", GrandExchangeItem[].class);

        for (int i = 0; i < items.length; ++i) {
            items[i].setIndex(i);
        }

        return items;
    }

    @HookName("GrandExchangeItem.Id")
    public int getId() {
        return getGrandExchangeItemInterface().getId(this.instance);
    }

    @HookName("GrandExchangeItem.Amount")
    public int getAmount() {
        return getGrandExchangeItemInterface().getAmount(this.instance);
    }

    @HookName("GrandExchangeItem.Price")
    public int getPrice() {
        return getGrandExchangeItemInterface().getPrice(this.instance);
    }

    @HookName("GrandExchangeItem.State")
    public byte getState() {
        return getGrandExchangeItemInterface().getState(this.instance);
    }

    public int getIndex() {
        return this.index;
    }

    protected void setIndex(int index) {
        this.index = index;
    }
}
