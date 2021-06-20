package com.farm.ibot.api.interact.impl;

import com.farm.ibot.api.accessors.Character;
import com.farm.ibot.api.accessors.Menu;
import com.farm.ibot.api.accessors.*;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interact.InteractHandler;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.impl.WalkAction;
import com.farm.ibot.api.interfaces.Animable;
import com.farm.ibot.api.interfaces.Interactable;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.api.wrapper.MenuNode;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;

import java.awt.*;

public class MouseInteractHandler implements InteractHandler {
    public boolean itemOnCharacter(Item item, Character character) {
        return this.itemInteract("Use", item) && this.animableInteract("Use", character);
    }

    public void walk(Tile tile) {
        WalkAction.create(tile).sendByMouse();
    }

    public boolean itemOnItem(Item item, Item item2) {
        return item.interact("Use") && item2.interact("Use");
    }

    public boolean itemOnObject(Item item, GameObject object) {
        return this.itemInteract("Use", item) && this.objectInteract("Use", object);
    }

    public boolean playerInteract(String action, Player player) {
        return this.animableInteract(action, player);
    }

    public boolean objectInteract(String action, GameObject gameObject) {
        return this.animableInteract(action, gameObject);
    }

    public boolean npcInteract(String action, Npc npc) {
        return this.animableInteract(action, npc);
    }

    public boolean widgetInteract(String action, Widget widget) {
        Point p = Random.human(widget.getBounds());
        Mouse.move(p.x, p.y);
        if (action.equals("")) {
            Mouse.click(true);
            return true;
        } else {
            return this.menuInteract(action, widget);
        }
    }

    public boolean menuInteract(String action) {
        return this.menuInteract(action, (Interactable) null);
    }

    public boolean groundItemInteract(String action, GroundItem groundItem) {
        return this.animableInteract(action, groundItem);
    }

    public boolean itemInteract(String action, Item item) {
        GameTab.INVENTORY.open();
        if (!item.isSelected()) {
            Point p = Random.human(item.getBounds());
            Mouse.move(p.x, p.y);
        }

        return item.isInventoryItem() && Time.sleep(1000, () -> {
            return Menu.contains(item);
        }) && Menu.find(action, item) == null && !Widgets.closeTopInterface() ? false : this.menuInteract(action, item);
    }

    public boolean itemInteract(ItemMethod action, Item item) {
        return this.itemInteract(action.stringValue, item);
    }

    public boolean menuInteract(String action, Interactable interactable) {
        Time.sleep(1000, () -> {
            return Menu.find(action, interactable) != null;
        });
        MenuNode node = Menu.find(action, interactable);
        if (node == null) {
            if (Menu.isVisible()) {
                Menu.close();
            }

            if (Client.getSelectedItemId() > 0) {
                Client.deselectItem();
            }
        }

        node = Menu.find(action, interactable);
        if (node != null) {
            if (node.getRealIndex() == 0) {
                Mouse.click(true);
                return true;
            }

            if (!Menu.isVisible()) {
                Menu.open();
                Time.sleep(10);
                node = Menu.find(action, interactable);
            }

            if (node == null && Menu.isVisible()) {
                Menu.close();
                return false;
            }

            if (node != null && Time.sleep(100, Menu::isVisible)) {
                Point point = Random.human(node.getBounds());
                Mouse.move(point.x, point.y);
                int op = node.getOpcodes();
                if (Time.sleep(() -> {
                    MenuNode h = Menu.getHovered();
                    return h != null && h.getOpcodes() == op;
                })) {
                    Mouse.click(true);
                }

                return true;
            }
        }

        return false;
    }

    public boolean animableInteract(String action, Animable animable) {
        if (!Widgets.closeTopInterface()) {
            return false;
        } else {
            if (!Screen.isOnGameScreen(animable.getScreenPoint())) {
                Walking.walkTo(animable.getPosition());
                Time.sleep(() -> {
                    return Screen.isOnGameScreen(animable.getScreenPoint());
                });
            }

            if (Screen.isOnGameScreen(animable.getScreenPoint())) {
                Mouse.move(animable.getScreenPoint().x, animable.getScreenPoint().y);
                if (Time.sleep(1000, () -> {
                    Mouse.move(animable.getScreenPoint().x, animable.getScreenPoint().y);
                    return Menu.find(action, (Interactable) animable) != null;
                })) {
                    return this.menuInteract(action, (Interactable) animable);
                }
            }

            return false;
        }
    }
}
