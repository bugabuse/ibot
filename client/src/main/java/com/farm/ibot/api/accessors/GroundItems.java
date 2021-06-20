package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IGroundItems;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.util.SafeArrayList;
import com.farm.ibot.api.util.Sorting;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;

import java.util.ArrayList;

public class GroundItems extends Node {
    public GroundItems(Object instance) {
        super(instance);
    }

    @HookName("GroundItems.List")
    public static LinkedList[][][] getList() {
        return getGroundItemsInterface().getList(null);
    }

    public static IGroundItems getGroundItemsInterface() {
        return Bot.get().accessorInterface.groundItemsInterface;
    }

    public static ArrayList<GroundItem> getAt(Tile tile) {
        SafeArrayList<GroundItem> temp = new SafeArrayList();
        LinkedList[][] list = getList()[Client.getPlane()];
        tile = tile.toLocalTile();
        if (list.length >= tile.getX() && list[tile.getX()].length >= tile.getY()) {
            LinkedList currentList = list[tile.getX()][tile.getY()];
            if (currentList == null) {
                return temp;
            } else {
                Node tail = currentList.getTail();

                for (Node current = tail.getNext(); current != null && !current.equals(tail); current = current.getNext()) {
                    GroundItem item = new GroundItem(current.instance, tile.getX(), tile.getY());
                    temp.add(item);
                }

                return temp;
            }
        } else {
            return temp;
        }
    }

    public static GroundItem getAny(Filter<GroundItem> filter) {
        return (GroundItem) getAll(filter).get(0);
    }

    public static SafeArrayList<GroundItem> getAll(Filter<GroundItem> filter) {
        SafeArrayList<GroundItem> temp = new SafeArrayList();
        LinkedList[][] list = getList()[Client.getPlane()];

        for (int x = 0; x < list.length; ++x) {
            for (int y = 0; y < list[x].length; ++y) {
                LinkedList currentList = list[x][y];
                if (currentList != null) {
                    Node tail = currentList.getTail();

                    for (Node current = tail.getNext(); current != null && !current.equals(tail); current = current.getNext()) {
                        GroundItem item = new GroundItem(current.instance, x, y);
                        if (filter.accept(item)) {
                            temp.add(item);
                        }
                    }
                }
            }
        }

        return temp;
    }

    public static GroundItem get(int itemId) {
        return (GroundItem) Sorting.getNearest(getAll((i) -> {
            return i.getId() == itemId;
        }));
    }

    public static GroundItem get(Filter<GroundItem> filter) {
        return (GroundItem) Sorting.getNearest(getAll(filter));
    }
}
