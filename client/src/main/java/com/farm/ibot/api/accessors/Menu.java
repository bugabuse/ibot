// Decompiled with: CFR 0.150
package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.accessors.interfaces.IMenu;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.interfaces.Interactable;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.api.wrapper.MenuNode;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.Bot;

import java.awt.*;
import java.util.Arrays;

public class Menu
        extends Wrapper {
    public Menu(Object instance) {
        super(instance);
    }

    public static IMenu getMenuInterface() {
        return Bot.get().accessorInterface.menuInterface;
    }

    @HookName(value = "Menu.ActionNames")
    public static String[] getActionNames() {
        return Menu.getMenuInterface().getActionNames(null);
    }

    @HookName(value = "Menu.Actions")
    public static String[] getActions() {
        return Menu.getMenuInterface().getActions(null);
    }

    @HookName(value = "Menu.Count")
    public static int getCount() {
        return Menu.getMenuInterface().getCount(null);
    }

    @HookName(value = "Menu.Count")
    public static void setCount(int value) {
        Menu.getMenuInterface().setCount(null, value);
    }

    @HookName(value = "Menu.Height")
    public static int getHeight() {
        return Menu.getMenuInterface().getHeight(null);
    }

    @HookName(value = "Menu.Width")
    public static int getWidth() {
        return Menu.getMenuInterface().getWidth(null);
    }

    @HookName(value = "Menu.Opcodes")
    public static int[] getOpcodes() {
        return Menu.getMenuInterface().getOpcodes(null);
    }

    @HookName(value = "Menu.Options")
    public static String[] getOptions() {
        return Menu.getMenuInterface().getOptions(null);
    }

    @HookName(value = "Menu.Variable")
    public static int[] getVariable() {
        return Menu.getMenuInterface().getVariable(null);
    }

    @HookName(value = "Menu.Visible")
    public static boolean isVisible() {
        return Menu.getMenuInterface().isVisible(null);
    }

    @HookName(value = "Menu.Visible")
    public static void setVisible(boolean visible) {
        Menu.getMenuInterface().setVisible(null, visible);
    }

    @HookName(value = "Menu.X")
    public static int getX() {
        return Menu.getMenuInterface().getX(null);
    }

    @HookName(value = "Menu.Y")
    public static int getY() {
        return Menu.getMenuInterface().getY(null);
    }

    @HookName(value = "Menu.XInteractions")
    public static int[] getXInteractions() {
        return Menu.getMenuInterface().getXInteractions(null);
    }

    @HookName(value = "Menu.YInteractions")
    public static int[] getYInteractions() {
        return Menu.getMenuInterface().getYInteractions(null);
    }

    public static Rectangle getBounds() {
        return new Rectangle(Menu.getX(), Menu.getY(), Menu.getWidth(), Menu.getHeight());
    }

    public static boolean open(Rectangle rect) {
        return Menu.open(Random.human(rect));
    }

    public static boolean openRandom() {
        return Menu.open(Screen.GAME_SCREEN);
    }

    public static boolean open(Point point) {
        Mouse.move(point);
        return Menu.open();
    }

    public static boolean open() {
        Mouse.click(false);
        return Time.sleep(() -> Menu.isVisible());
    }

    public static boolean forceOpen() {
        return Time.sleep(() -> {
            if (Menu.isVisible()) {
                return true;
            }
            Mouse.move(Random.next(20, Screen.GAME_SCREEN.width - 20), Random.next(20, Screen.GAME_SCREEN.height - 20));
            Time.waitNextGameCycle();
            Mouse.click(false);
            return Time.sleep(2000, Menu::isVisible);
        });
    }

    public static Rectangle getChildBounds(int i) {
        return new Rectangle(Menu.getX() + 5, Menu.getY() + 19 + 15 * i + 3, Menu.getWidth() - 10, 10);
    }

    public static void addItem(int x, int y, int opcode, int uid, String action, String objectName) {
        Menu.setCount(Menu.getCount() + 1);
        int i = Menu.getCount() - 1;
        Menu.getOpcodes()[i] = opcode;
        Menu.getVariable()[i] = uid;
        Menu.getOptions()[i] = objectName;
        Menu.getActions()[i] = action;
        Menu.getActionNames()[i] = objectName;
        Menu.getXInteractions()[i] = x;
        Menu.getYInteractions()[i] = y;
    }

    public static void setFirstItem(int x, int y, int opcode, int uid, String action, String objectName) {
        Menu.setCount(Menu.getCount() + 1);
        int i = Menu.getCount() - 1;
        Menu.getOpcodes()[i] = opcode;
        Menu.getVariable()[i] = uid;
        Menu.getOptions()[i] = objectName;
        Menu.getActions()[i] = action;
        Menu.getActionNames()[i] = objectName;
        Menu.getXInteractions()[i] = x;
        Menu.getYInteractions()[i] = y;
    }

    public static void setAllItems(int x, int y, int opcode, int uid, String action, String objectName) {
        for (int i = 0; i < 255; ++i) {
            Menu.getOpcodes()[i] = opcode;
            Menu.getVariable()[i] = uid;
            Menu.getOptions()[i] = objectName;
            Menu.getActions()[i] = action;
            Menu.getActionNames()[i] = objectName;
            Menu.getXInteractions()[i] = x;
            Menu.getYInteractions()[i] = y;
        }
    }

    public static MenuNode[] getNodes() {
        MenuNode[] nodes = new MenuNode[Menu.getCount()];
        int i = nodes.length - 1;
        int i2 = 0;
        while (i >= 0) {
            nodes[i2] = new MenuNode(i2, i);
            --i;
            ++i2;
        }
        return nodes;
    }

    public static MenuNode find(Filter<MenuNode> filter) {
        return Arrays.stream(Menu.getNodes()).filter(filter::accept).findAny().orElse(null);
    }

    public static MenuNode find(String action) {
        return Menu.find(action, null);
    }

    public static MenuNode find(String action, Interactable interactable) {
        if (interactable == null) {
            return Menu.find((MenuNode n) -> action == null || n.getAction().equalsIgnoreCase(action));
        }
        if (interactable instanceof Player) {
            Player p = (Player) interactable;
            return Menu.find((MenuNode n) -> (action == null || n.getAction().equalsIgnoreCase(action)) && n.getId() == p.getIndex());
        }
        if (interactable instanceof Npc) {
            Npc p = (Npc) interactable;
            return Menu.find((MenuNode n) -> (action == null || n.getAction().equalsIgnoreCase(action)) && n.getId() == p.getIndex());
        }
        if (interactable instanceof GameObject) {
            GameObject p = (GameObject) interactable;
            return Menu.find((MenuNode n) -> (action == null || n.getAction().equalsIgnoreCase(action)) && (long) n.getId() == p.getUid());
        }
        if (interactable instanceof Item) {
            Item item = (Item) interactable;
            return Menu.find((MenuNode n) -> action == null && n.getId() == item.getId() || n.getAction().equalsIgnoreCase(action));
        }
        if (interactable instanceof Widget) {
            return Menu.find((MenuNode n) -> action == null || action.length() <= 0 || n.getAction().equalsIgnoreCase(action));
        }
        if (interactable instanceof GroundItem) {
            GroundItem p = (GroundItem) interactable;
            return Menu.find((MenuNode n) -> (action == null || n.getAction().equalsIgnoreCase(action)) && n.getId() == p.getId());
        }
        return null;
    }

    public static boolean contains(Interactable interactable) {
        return Menu.find(null, interactable) != null;
    }

    public static void close() {
        Menu.setVisible(false);
    }

    public static MenuNode getHovered() {
        if (Menu.isVisible()) {
            for (MenuNode node : Menu.getNodes()) {
                if (!node.getBounds().contains(Mouse.getLocation())) continue;
                return node;
            }
        }
        return null;
    }
}
