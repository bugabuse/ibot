package com.farm.ibot.api.methods;

import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;

import java.util.ArrayList;

public class Inventory {
    public static boolean isFull() {
        return container().isFull();
    }

    public static Item[] getAll() {
        return container().getItems();
    }

    public static Item get(int id) {
        return container() != null ? container().get((f) -> {
            return f.getId() == id;
        }) : null;
    }

    public static ItemContainer container() {
        return (new ItemContainer(Widgets.get(149, 0))).inventory();
    }

    public static boolean isEmpty() {
        return container().getAll().size() == 0;
    }

    public static int getFreeSlots() {
        return container().getFreeSlots();
    }

    public static Item get(String name) {
        return container().get(name);
    }

    public static ArrayList<Item> getAll(String name) {
        return container().getAll(name);
    }

    public static boolean contains(int id) {
        return container().contains(id);
    }

    public static boolean contains(int id, int amount) {
        return container().contains(id, amount);
    }

    public static int getCount(int id) {
        return container().getCount(id);
    }
}
