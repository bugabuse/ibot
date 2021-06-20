// Decompiled with: Procyon 0.5.36
package com.farm.ibot.api.methods.banking;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.data.WidgetId;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.impl.ItemAction;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.interfaces.Interactable;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.farm.ibot.core.Bot;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Bank {
    public static HashMap<Bot, ItemContainer> cache;

    static {
        Bank.cache = new HashMap<Bot, ItemContainer>();
    }

    public static ArrayList<Item> getAll() {
        return getContainer().getAll();
    }

    public static Item[] getItemsArray() {
        return getContainer().getItems();
    }

    private static void doCache(final boolean checkOpened) {
        if (!checkOpened || isOpen()) {
            setCache(getContainer());
        }
    }

    public static ItemContainer getCache() {
        doCache(true);
        return Bank.cache.getOrDefault(Bot.get(), getContainer());
    }

    public static void setCache(final ItemContainer container) {
        Bank.cache.put(Bot.get(), container);
    }

    public static boolean hasCache() {
        return Bank.cache.containsKey(Bot.get());
    }

    public static ItemContainer getContainer() {
        return new ItemContainer(Widgets.getChildren(12, 12)).bank();
    }

    public static boolean openAndWithdraw(final Item... items) {
        return openAndWithdraw(false, items);
    }

    public static boolean openAndWithdraw(final boolean noted, final Item... items) {
        if (!openNearest()) {
            return false;
        }
        for (final Item item : items) {
            if (!Inventory.container().hasSpace(item)) {
                depositAllExcept(items);
            }
            withdraw(noted, item.getId(), item.getAmount());
            Time.sleep(600, 800);
        }
        return Inventory.container().containsAll(items);
    }

    public static boolean openNearest() {
        return openNearest(Player.getLocal().getPosition());
    }

    public static boolean openNearest(final Tile nearestTo) {
        return isOpen() || (WebWalking.walkTo(Locations.getClosestBank(nearestTo), 12, new Tile[0]) && open());
    }

    public static boolean open() {
        if (isOpen()) {
            return true;
        }
        if (clickOpen()) {
            return Time.sleepHuman(Bank::isOpen);
        }
        return isOpen();
    }

    private static boolean clickOpen() {
        final Interactable toInteract = getInteractable();

        String action = "Bank";
        if (toInteract instanceof GameObject && ((GameObject) toInteract).getName().contains("chest")) {
            action = "Use";
        }
        return toInteract != null && toInteract.interact(action);
    }

    public static Interactable getInteractable() {
        final GameObject obj = GameObjects.get(f -> f.getName().equalsIgnoreCase("bank chest") || (f.getName().equalsIgnoreCase("bank booth") && Arrays.asList(f.getActions()).contains("Bank")) || (f.getName().equalsIgnoreCase("grand exchange booth") && Arrays.asList(f.getActions()).contains("Bank")));
        final Npc npc = Npcs.get(f -> Arrays.asList(f.getActions()).contains("Bank"));
        Interactable toInteract = obj;
        if (npc != null && npc.isReachable()) {
            toInteract = npc;
        }
        if (obj != null && obj.isReachable()) {
            if (npc != null && npc.isReachable()) {
                toInteract = (Interactable) ((Math.abs(obj.getPosition().distance() - npc.getPosition().distance()) < 4) ? obj : npc);
            } else {
                toInteract = obj;
            }
        }
        return toInteract;
    }

    private static boolean isReachable(final Interactable interactable) {
        Tile t = null;
        if (interactable != null) {
            if (interactable instanceof GameObject) {
                t = ((GameObject) interactable).getPosition();
                if (!((GameObject) interactable).isReachable()) {
                    return false;
                }
            } else if (interactable instanceof Npc) {
                t = ((Npc) interactable).getPosition();
            }
        }
        return t != null && t.distance() <= 7;
    }

    public static boolean isOpen() {
        final Widget bankWidget = Widgets.get(12, WidgetId.Bank.BANK_ITEM_COUNT);
        if (bankWidget == null) {
            return false;
        }
        final String text = bankWidget.getText();
        if (text == null || text.length() < 1) {
            return false;
        }
        if (getAll().size() <= 0) {
            final Widget deposit = Widgets.get(w -> StringUtils.containsEqual("Deposit inventory", w.getActions()));
            deposit.interact("");
            return Time.sleep(() -> getAll().size() > 0);
        }
        try {
            final int amount = Integer.parseInt(text);
            if (amount == getAll().size()) {
                final Widget close = Widgets.forId(43515932);
                if (close != null && close.isRendered()) {

                    Mouse.click(335, 195);
                    if (!Time.sleep(() -> !close.isRendered())) {
                        Walking.walkTo(Player.getLocal().getPosition(), 0);
                        return false;
                    }
                }
                doCache(false);
                return true;
            }
        } catch (NumberFormatException ex) {
        }
        return false;
    }

    public static boolean depositAll() {
        Debug.log(Inventory.isEmpty());
        Debug.log(isOpen());
        if (Inventory.isEmpty()) {
            return true;
        }
        if (isOpen()) {
            Debug.log(Widgets.get(w -> StringUtils.containsEqual("Deposit inventory", w.getActions())).getId());
            Widgets.get(w -> StringUtils.containsEqual("Deposit inventory", w.getActions())).interact("");
            if (Time.sleep(5000, Inventory::isEmpty)) {
                Time.sleep(100, 300);
                return Inventory.isEmpty();
            }
        }
        return false;
    }

    public static boolean depositAllExcept(final Item... items) {
        final int[] ids = Arrays.stream(items).mapToInt(Item::getId).toArray();
        return depositAllExcept(ids);
    }

    public static boolean depositAllExcept(final int... ids) {
        return depositAllExcept(i -> Ints.contains(ids, i.getId()));
    }

    public static boolean depositAllExcept(final Filter<Item> filter) {
        return depositAll(i -> !filter.accept(i));
    }

    public static boolean depositAll(final Filter<Item> filter) {
        final ArrayList<Item> items = Inventory.container().getAll(filter);
        if (Inventory.container().getAll().stream().allMatch(filter::accept)) {
            depositAll();
        } else {
            for (final Item item : items) {
                if (Inventory.contains(item.getId())) {
                    Debug.log("Deposit: " + item.getId());
                    deposit(item.getId(), Inventory.getCount(item.getId()));
                }
            }
        }
        return Inventory.container().getAll(filter).size() <= 0;
    }

    public static void deposit(final int itemId, final int amount) {
        doAction(true, itemId, amount);
    }

    public static void withdraw(final int itemId, final int amount) {
        withdraw(false, itemId, amount);
    }

    public static void withdraw(final int itemId, final int amount, final boolean quickActions) {
        withdraw(false, itemId, amount, quickActions);
    }

    public static void withdraw(final boolean noted, final int itemId, final int amount) {
        if (amount > 0 && getContainer().contains(itemId) && setNoted(noted)) {
            doAction(false, itemId, amount);
        }
    }

    public static void withdraw(final boolean noted, final int itemId, final int amount, final boolean quickActions) {
        if (amount > 0 && getContainer().contains(itemId) && setNoted(noted)) {
            doAction(false, itemId, amount, quickActions, true);
        }
    }

    public static boolean isNoted() {
        return (Config.get(115) & 0x1) == 0x1;
    }

    public static boolean setNoted(final boolean noted) {
        if (isNoted() == noted) {
            return true;
        }
        if (noted) {
            Widgets.get(w -> StringUtils.containsEqual("Note", w.getActions())).interact("");
        } else {
            Widgets.get(w -> StringUtils.containsEqual("Item", w.getActions())).interact("");
        }
        return Time.sleep(() -> isNoted() == noted);
    }

    public static void doAction(final boolean deposit, final int itemId, final int amount) {
        doAction(deposit, itemId, amount, false, true);
    }

    public static void doAction(final boolean deposit, final int itemId, final int amount, final boolean quickActions, final boolean waitInventoryChange) {
        if (!open()) {
            return;
        }
        final ItemContainer container = deposit ? Inventory.container() : getContainer();
        final ArrayList<ItemAction> requiredActions = new ArrayList<ItemAction>();
        int current = amount;
        boolean tooManyActions = !quickActions;
        while (quickActions) {
            if (requiredActions.size() > 7) {
                tooManyActions = true;
                break;
            }
            if (current <= 0) {
                break;
            }
            if (current >= 10) {
                current -= 10;
                requiredActions.add((ItemAction) ItemAction.create(deposit ? ItemMethod.DEPOSIT_10 : ItemMethod.WITHDRAW_10, container.get(itemId)));
            } else if (current >= 5) {
                current -= 5;
                requiredActions.add((ItemAction) ItemAction.create(deposit ? ItemMethod.DEPOSIT_5 : ItemMethod.WITHDRAW_5, container.get(itemId)));
            } else {
                if (current < 1) {
                    continue;
                }
                --current;
                requiredActions.add((ItemAction) ItemAction.create(deposit ? ItemMethod.DEPOSIT_1 : ItemMethod.WITHDRAW_1, container.get(itemId)));
            }
        }
        if (!deposit && amount == Config.get(304) / 2) {
            container.get(itemId).interact(ItemMethod.WITHDRAW_LAST);
            if (waitInventoryChange) {
                Time.waitInventoryChange();
            }
            doCache(true);
        } else if (amount >= container.getCount(itemId) || (!deposit && amount >= Inventory.getFreeSlots() && !isNoted() && !container.get(itemId).getDefinition().isStackable())) {
            if (container.get(itemId) != null) {
                if (deposit) {
                    container.get(itemId).interact(ItemMethod.DEPOSIT_ALL.stringValue);
                } else {
                    container.get(itemId).interact(ItemMethod.WITHDRAW_ALL);
                }
                if (waitInventoryChange) {
                    Time.waitInventoryChange();
                }
                doCache(true);
            }
        } else if (tooManyActions) {
            if (container.get(itemId) != null) {
                if (deposit) {
                    container.get(itemId).interact(ItemMethod.DEPOSIT_X.stringValue);
                } else {
                    container.get(itemId).interact(ItemMethod.WITHDRAW_X);
                }
                InputBox.input(amount);
                if (waitInventoryChange) {
                    Time.waitInventoryChange();
                }
                doCache(true);
            }
        } else {
            for (final ItemAction action : requiredActions) {
                if (deposit) {
                    container.get(itemId).interact(action.method.stringValue);
                } else {
                    container.get(itemId).interact(action.method);
                }
                Time.sleep(100, 300);
            }
            if (waitInventoryChange) {
                Time.waitInventoryChange();
            }
            doCache(true);
        }
    }

    public static void close() {
        Keyboard.press(27);
        Time.sleep(10, 50);
        Keyboard.release(27);
    }
}
