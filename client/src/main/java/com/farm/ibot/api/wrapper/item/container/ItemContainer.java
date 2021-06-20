// Decompiled with: CFR 0.150
package com.farm.ibot.api.wrapper.item.container;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.wrapper.item.Item;
import com.google.common.primitives.Ints;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ItemContainer {
    private Item[] items = new Item[0];
    private Widget widget;
    private boolean isLoaded = false;
    private int height;
    private int width;
    private int widgetX;
    private int widgetY;
    private boolean countNotedItems = false;
    private boolean inventory = false;
    private boolean bank = false;

    public ItemContainer() {
    }

    public ItemContainer(ArrayList<Item> items) {
        this((Item[]) items.toArray());
    }

    public ItemContainer(Item[]... items) {
        ArrayList<Item> itemList = new ArrayList<Item>();
        Item[][] arritem = items;
        int n = arritem.length;
        for (int i = 0; i < n; ++i) {
            Item[] item1;
            for (Item item : item1 = arritem[i]) {
                itemList.add(item);
            }
        }
        this.items = itemList.toArray(new Item[itemList.size()]);
    }

    public ItemContainer(Item[] items) {
        this.items = items;
    }

    public ItemContainer(Widget widget) {
        this.widget = widget;
        if (widget != null) {
            this.isLoaded = true;
            this.width = widget.getWidth();
            this.height = widget.getHeight();
            this.widgetX = widget.getScreenX();
            this.widgetY = widget.getScreenY();
            if (widget.getItemIds() == null) {
                return;
            }
            this.items = new Item[widget.getItemIds().length];
            for (int i = 0; i < this.items.length; ++i) {
                Rectangle rect = new Rectangle(this.widgetX + 42 * (i % this.width), this.widgetY + 36 * (i / this.width), 32, 32);
                this.items[i] = new Item(widget.getItemIds()[i] - 1, widget.getItemAmounts() != null ? widget.getItemAmounts()[i] : 1, i, widget, rect);
            }
        }
    }

    public ItemContainer(Widget[] children) {
        if (children != null) {
            this.isLoaded = true;
            this.items = new Item[children.length];
            for (int i = 0; i < this.items.length; ++i) {
                Widget child = children[i];
                if (child == null) continue;
                if (this.widget == null) {
                    this.widget = child.getParent();
                    this.width = this.widget.getWidth();
                    this.height = this.widget.getHeight();
                    this.widgetX = this.widget.getScreenX();
                    this.widgetY = this.widget.getScreenY();
                }
                Rectangle rect = child.getBounds();
                this.items[i] = new Item(child.getItemId() == 6512 ? -1 : child.getItemId(), child.getItemAmount(), i, child, rect);
            }
        }
    }

    public ItemContainer countNoted() {
        this.countNotedItems = true;
        return this;
    }

    public Item[] getItems() {
        return this.items;
    }

    public Rectangle getBoundsForSlot(int slot) {
        if (Bank.getCache() != null && Bank.getCache().widget != null && this.widget.getId() == Bank.getCache().widget.getId()) {
            int cols = 8;
            return new Rectangle(3 + this.widgetX + 48 + 48 * (slot % cols), this.widgetY + 36 * (slot / cols), 36, 32);
        }
        return new Rectangle(this.widgetX + 42 * (slot % this.width), this.widgetY + 36 * (slot / this.width), 32, 32);
    }

    public ArrayList<Item> getAll() {
        return this.getAll(Objects::nonNull);
    }

    public ArrayList<Item> getAll(Filter<Item> filter) {
        ArrayList<Item> items = new ArrayList<Item>();
        for (Item item : this.getItems()) {
            if (item == null || item.getId() == -1 || !filter.accept(item)) continue;
            items.add(item);
        }
        return items;
    }

    public ItemContainer inventory() {
        this.inventory = true;
        return this;
    }

    public ItemContainer bank() {
        this.bank = true;
        return this;
    }

    public Item get(int id) {
        return this.get((Item f) -> f.getId() == id);
    }

    public Item getFromLastSlot(int id) {
        return this.getFromLastSlot(f -> f.getId() == id);
    }

    public Item getFromPoint(Point point, int id) {
        return this.getFromPoint(point, f -> f.getId() == id);
    }

    public Item get(Filter<Item> filter) {
        ArrayList<Item> available = this.getAll(filter);
        return available.size() > 0 ? available.get(0) : null;
    }

    public Item getFromLastSlot(Filter<Item> filter) {
        ArrayList<Item> available = this.getAll(filter);
        return available.size() > 0 ? available.get(available.size() - 1) : null;
    }

    public Item getFromPoint(Point point, Filter<Item> filter) {
        ArrayList<Item> available = this.getAll(filter::accept);
        Item nearest = null;
        double lastDistance = -1.0;
        for (Item item : available) {
            if (nearest != null) {
                Point point2 = new Point((int) item.getBounds().getCenterX(), (int) item.getBounds().getCenterY());
                if (!(point2.distance(point) < lastDistance)) continue;
            }
            lastDistance = new Point((int) item.getBounds().getCenterX(), (int) item.getBounds().getCenterY()).distance(point);
            nearest = item;
        }
        return nearest;
    }

    public boolean isInventory() {
        return this.inventory;
    }

    public boolean isBank() {
        return this.bank;
    }

    public boolean contains(String itemName) {
        return this.contains(itemName, 1);
    }

    public boolean contains(String itemName, int count) {
        return this.contains((Item i) -> i.getName().equalsIgnoreCase(itemName), 1);
    }

    public int getCount(Filter<Item> filter) {
        int total = 0;
        for (Item item : this.getAll()) {
            if (!filter.accept(item)) continue;
            total += item.getAmount();
        }
        return total;
    }

    public int getCount(String... ids) {
        return this.getCount((Item i) -> StringUtils.containsEqual(i.getName(), ids));
    }

    public int getCount(int... ids) {
        int[] finalIds = this.addNotedIds(ids);
        return this.getCount((Item i) -> Ints.contains(finalIds, i.getId()));
    }

    public Item getAny(int... ids) {
        return this.get((Item i) -> Ints.contains(ids, i.getId()));
    }

    public boolean contains(Filter<Item> filter) {
        return this.contains(filter, 1);
    }

    public boolean contains(Filter<Item> filter, int amount) {
        return this.getCount(filter) >= amount;
    }

    public boolean contains(int id) {
        return this.contains(id, 1);
    }

    public boolean containsAll(int... ids) {
        for (int id : ids = this.addNotedIds(ids)) {
            if (this.contains(id)) continue;
            return false;
        }
        return true;
    }

    public boolean containsAny(int... ids) {
        for (int id : ids = this.addNotedIds(ids)) {
            if (!this.contains(id)) continue;
            return true;
        }
        return false;
    }

    public boolean containsAny(Item... items) {
        for (Item i : items) {
            if (!this.contains(i.getId(), 1)) continue;
            return true;
        }
        return false;
    }

    public boolean contains(int id, int amount) {
        return this.getCount(this.addNotedIds(id)) >= amount;
    }

    public Widget getWidget() {
        return this.widget;
    }

    public boolean isFull() {
        return this.getAll().size() >= 28;
    }

    public int getFreeSlots() {
        return 28 - this.getAll().size();
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    public Item get(String name) {
        return this.get((Item i) -> i.getName().equalsIgnoreCase(name));
    }

    public ArrayList<Item> getAll(String name) {
        return this.getAll((Item i) -> i.getName().equalsIgnoreCase(name));
    }

    public boolean containsAll(Item... items) {
        for (Item item : items) {
            if (this.contains(item.getId(), item.getAmount())) continue;
            return false;
        }
        return true;
    }

    public boolean containsAllOne(Item... items) {
        for (Item item : items) {
            if (this.contains(item.getId(), 1)) continue;
            return false;
        }
        return true;
    }

    private int[] addNotedIds(int... unnotedIds) {
        if (this.countNotedItems) {
            ArrayList<Integer> newIds = new ArrayList<Integer>(Ints.asList(unnotedIds));
            for (int id : unnotedIds) {
                if (ItemDefinition.forId(id).isNoted() || ItemDefinition.forId((int) id).notedID == -1) continue;
                newIds.add(ItemDefinition.forId((int) id).notedID);
            }
            return Ints.toArray(newIds);
        }
        return unnotedIds;
    }

    public boolean hasSpace(String item, int amount) {
        int id = Arrays.stream(ItemDefinition.definitions).filter(d -> d.name.contains(item)).findAny().map(d -> d.id).orElse(-1);
        if (id != -1) {
            return this.hasSpace(new Item(id, amount));
        }
        return false;
    }

    public boolean hasSpace(Item item) {
        if (item.getDefinition().isStackable()) {
            return this.getFreeSlots() > 0 || this.contains(item.getId());
        }
        return this.getFreeSlots() >= item.getAmount();
    }
}
