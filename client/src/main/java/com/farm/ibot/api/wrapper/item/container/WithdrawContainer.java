// Decompiled with: Procyon 0.5.36
package com.farm.ibot.api.wrapper.item.container;

import com.farm.ibot.api.accessors.GrandExchangeItem;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.wrapper.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WithdrawContainer {
    private List<WithdrawItem> items;
    private PriceHandler priceHandler;

    public WithdrawContainer(final GrandExchangeItem... items) {
        this(new PriceHandler(new WithdrawItem[0]), items);
    }

    public WithdrawContainer(final WithdrawItem... items) {
        this(new PriceHandler(new WithdrawItem[0]), items);
    }

    public WithdrawContainer(final Item... items) {
        this(new PriceHandler(new WithdrawItem[0]), items);
    }

    public WithdrawContainer(final PriceHandler priceHandler, final Item... items) {
        this.items = new ArrayList<WithdrawItem>();
        this.priceHandler = new PriceHandler(new WithdrawItem[0]);
        this.priceHandler = priceHandler;
        Arrays.stream(items).forEach(i -> this.add(new WithdrawItem(i)));
    }

    public WithdrawContainer(final PriceHandler priceHandler, final WithdrawItem... items) {
        this.items = new ArrayList<WithdrawItem>();
        this.priceHandler = new PriceHandler(new WithdrawItem[0]);
        this.priceHandler = priceHandler;
        Arrays.stream(items).forEach(this::add);
    }

    public WithdrawContainer(final PriceHandler priceHandler, final GrandExchangeItem... items) {
        this.items = new ArrayList<WithdrawItem>();
        this.priceHandler = new PriceHandler(new WithdrawItem[0]);
        this.priceHandler = priceHandler;
        Arrays.stream(items).forEach(i -> this.add(new WithdrawItem(i.getId(), i.getAmount())));
    }

    public void add(final WithdrawItem item) {
        if (item.getId() > 0) {
            final WithdrawItem newItem = this.getItemOrCreate(item.getId());
            newItem.setAmount(item.getAmount() + newItem.getAmount());
            newItem.setPriceHandler(this.priceHandler);
            if (item.getSlot() != -1) {
                newItem.setSlot(item.getSlot());
            }
        }
    }

    public WithdrawContainer removeAll(final Filter<WithdrawItem> filter) {
        new ArrayList<WithdrawItem>(this.items).stream().filter(filter::accept).forEach(this::remove);

        return this;
    }

    public void remove(final WithdrawItem item) {
        final WithdrawItem newItem = this.getItemOrCreate(item.getId());
        newItem.setAmount(newItem.getAmount() - item.getAmount());
        newItem.setPriceHandler(this.priceHandler);
        if (newItem.getAmount() < 0) {
            newItem.setAmount(0);
        }
    }

    public WithdrawContainer merge(final WithdrawContainer container) {
        final WithdrawContainer newContainer = new WithdrawContainer(this.priceHandler, this.getItemsArray());
        container.getItems().forEach(newContainer::add);
        return newContainer;
    }

    public WithdrawContainer subtract(final WithdrawContainer container) {
        final WithdrawContainer newContainer = new WithdrawContainer(this.priceHandler, this.getItemsArray());
        for (final WithdrawItem item : container.getItems()) {
            newContainer.remove(item);
        }
        return newContainer;
    }

    public WithdrawContainer subtractForPriceAndIgnore(final int priceToSubstract, final WithdrawItem[] requiredItems) {
        return this.subtractForPrice(priceToSubstract, i -> new WithdrawContainer(requiredItems).contains(i.getId()));
    }

    public WithdrawContainer subtractForPrice(final int priceToSubstract) {
        return this.subtractForPrice(priceToSubstract, i -> true);
    }

    public WithdrawContainer subtractForPrice(final int priceToSubstract, final Filter<WithdrawItem> filter) {
        final WithdrawContainer newContainer = new WithdrawContainer(this.priceHandler, this.getItemsArray());
        int currentValue = priceToSubstract;
        for (final WithdrawItem item : newContainer.getPriceableItemArray()) {
            if (item.getUnitPrice() != 0) {
                if (currentValue <= 0) {
                    break;
                }
                if (filter.accept(item)) {
                    item.setAmount(item.getAmount() - this.priceHandler.getAmount(item.getId()));
                    if (item.getTotalPrice() - currentValue > 0) {
                        final int temp = item.getTotalPrice();
                        item.setAmount((item.getTotalPrice() - currentValue) / item.getUnitPrice());
                        currentValue -= temp;
                    } else {
                        currentValue -= item.getTotalPrice();
                        newContainer.remove(item);
                    }
                    if (item.getAmount() < 0) {
                        newContainer.remove(item);
                    }
                }
            }
        }
        return newContainer;
    }

    public WithdrawItem getItemOrCreate(final int id) {
        WithdrawItem item = this.getItem(id);
        if (item != null) {
            return item;
        }
        item = new WithdrawItem(id, 0);
        this.items.add(item);
        return item;
    }

    public WithdrawItem getItem(final int id) {
        for (final WithdrawItem item : this.items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public int calculateWealth() {
        int total = 0;
        for (final WithdrawItem item : this.items) {
            item.setPriceHandler(this.priceHandler);
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getCount(final int... ids) {
        int total = 0;
        for (final int id : ids) {
            for (final WithdrawItem item : this.items) {
                if (item != null && (item.getId() == id || item.getId() + 1 == id || item.getId() - 1 == id) && ItemDefinition.forId(id).name.equals(item.getDefinition().name)) {
                    total += item.getAmount();
                }
            }
        }
        return total;
    }

    public boolean contains(final int id) {
        return this.contains(id, 1);
    }

    public boolean containsAnyOf(final int... ids) {
        return Arrays.stream(ids).anyMatch(id -> this.contains(id, 1));
    }

    @Deprecated
    public boolean contains(final WithdrawContainer container) {
        for (final WithdrawItem item : container.getPriceableItemArray()) {
            if (!this.contains(item.getId(), container.getCount(item.getId()))) {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(final WithdrawContainer container) {
        for (final WithdrawItem item : container.getItems()) {
            if (!this.contains(item.getId(), container.getCount(item.getId()))) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(final int id, final int amount) {
        return this.getCount(id) >= amount;
    }

    public List<WithdrawItem> getItems() {
        return this.items;
    }

    public WithdrawItem[] getItemsArray() {
        return this.items.toArray(new WithdrawItem[this.items.size()]);
    }

    public WithdrawItem[] getPriceableItemArray() {
        final ArrayList<WithdrawItem> tempItems = new ArrayList<WithdrawItem>();
        for (final Map.Entry<Integer, Integer> id : this.priceHandler.priceList) {
            final WithdrawItem item = this.getItem(id.getKey());
            if (item != null) {
                tempItems.add(item);
            }
        }
        return tempItems.toArray(new WithdrawItem[tempItems.size()]);
    }

    public WithdrawContainer setPriceHandler(final PriceHandler priceHandler) {
        this.priceHandler = priceHandler;
        return this;
    }
}
