package com.farm.ibot.api.methods.shop;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.impl.ItemAction;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.farm.ibot.core.Bot;

import java.util.ArrayList;
import java.util.HashMap;

public class Shop {
    private static HashMap<Bot, ItemContainer> cache = new HashMap();

    public static ArrayList<Item> getAll() {
        return getContainer().getAll();
    }

    public static Item[] getItemsArray() {
        return getContainer().getItems();
    }

    private static void doCache(boolean checkOpened) {
        if (!checkOpened || isOpen()) {
            cache.put(Bot.get(), getContainer());
        }

    }

    public static ItemContainer getCache() {
        doCache(true);
        return (ItemContainer) cache.getOrDefault(Bot.get(), getContainer());
    }

    public static ItemContainer getContainer() {
        return new ItemContainer(Widgets.getChildren(300, 16));
    }

    public static boolean open(String npcName) {
        if (isOpen()) {
            return true;
        } else {
            Npc npc = Npcs.get(npcName);
            return npc != null && npc.interact("Trade") ? Time.sleepHuman(Shop::isOpen) : isOpen();
        }
    }

    public static boolean isOpen() {
        return Widgets.get(300, 16) != null && Widgets.get(300, 16).isRendered();
    }

    public static void buy(Item item, int amount) {
        if (item != null && item.getAmount() > 0) {
            item.interact("Buy " + amount);
        }

    }

    public static void quickBuy(Item item, int amount) {
        if (item != null) {
            ItemAction.create(ItemMethod.BUY_10, item).send();
        }

    }

    public static void buy(int itemId, int amount) {
        buy(getContainer().get(itemId), amount);
    }
}
