package com.farm.ibot.api.methods.banking;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.impl.ItemAction;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class DepositBox {
    public static ItemContainer getContainer() {
        return new ItemContainer(Widgets.getChildren(192, 2));
    }

    public static boolean openNearest() {
        return openNearest(Player.getLocal().getPosition());
    }

    public static boolean openNearest(Tile nearestTo) {
        if (isOpen()) {
            return true;
        } else {
            GameObject object = GameObjects.get("Bank deposit box");
            return WebWalking.walkTo(Locations.getClosestDepositBox(nearestTo)) && object != null && object.isReachable() ? open() : false;
        }
    }

    public static boolean open() {
        if (isOpen()) {
            return true;
        } else {
            GameObject object = GameObjects.get("Bank deposit box");
            return object != null && object.interact("Deposit") ? Time.sleepHuman(DepositBox::isOpen) : Bank.isOpen();
        }
    }

    public static boolean isOpen() {
        return Widgets.get(192, 2) != null;
    }

    public static boolean depositAll() {
        if (isOpen()) {
            Widgets.get((w) -> {
                return StringUtils.containsEqual("Deposit inventory", w.getActions());
            }).interact("");
            if (Time.sleep(5000, Inventory::isEmpty)) {
                Time.sleep(100, 300);
                return Inventory.isEmpty();
            }
        }

        return false;
    }

    public static boolean depositAllExcept(Item... items) {
        return depositAll((i) -> {
            return !Arrays.stream(items).anyMatch((item) -> {
                return item.getId() == i.getId();
            });
        });
    }

    public static boolean depositAllExcept(int... ids) {
        return depositAll((i) -> {
            return !Ints.contains(ids, i.getId());
        });
    }

    public static boolean depositAllExcept(Filter<Item> filter) {
        return depositAll((i) -> {
            return !filter.accept(i);
        });
    }

    public static boolean depositAll(Filter<Item> filter) {
        ArrayList<Item> items = Inventory.container().getAll(filter);
        Iterator var2 = items.iterator();

        while (var2.hasNext()) {
            Item item = (Item) var2.next();
            if (getContainer().contains(item.getId())) {
                deposit(item.getId(), Inventory.getCount(item.getId()));
            }
        }

        return Inventory.container().getAll(filter).size() <= 0;
    }

    private static void deposit(int itemId, int amount) {
        if (open()) {
            ItemContainer container = getContainer();
            ArrayList<ItemAction> requiredActions = new ArrayList();
            int current = amount;
            boolean tooManyActions = false;

            while (true) {
                if (requiredActions.size() > 7) {
                    tooManyActions = true;
                    break;
                }

                if (current <= 0) {
                    break;
                }

                if (current >= 10) {
                    current -= 10;
                    requiredActions.add((ItemAction) ItemAction.create(ItemMethod.BOX_DEPOSIT_10, container.get(itemId)));
                } else if (current >= 5) {
                    current -= 5;
                    requiredActions.add((ItemAction) ItemAction.create(ItemMethod.BOX_DEPOSIT_5, container.get(itemId)));
                } else if (current >= 1) {
                    --current;
                    requiredActions.add((ItemAction) ItemAction.create(ItemMethod.BOX_DEPOSIT_1, container.get(itemId)));
                }
            }

            if (amount >= container.getCount(itemId)) {
                if (container.get(itemId) != null) {
                    container.get(itemId).interact(ItemMethod.BOX_DEPOSIT_ALL);
                    Time.waitInventoryChange();
                }
            } else if (tooManyActions) {
                if (container.get(itemId) != null) {
                    container.get(itemId).interact(ItemMethod.BOX_DEPOSIT_X);
                    InputBox.input(amount);
                    Time.waitInventoryChange();
                }
            } else {
                Iterator var6 = requiredActions.iterator();

                while (var6.hasNext()) {
                    ItemAction action = (ItemAction) var6.next();
                    container.get(itemId).interact(action.method);
                    Time.sleep(100, 300);
                }

                Time.sleep(100, 300);
            }

        }
    }
}
