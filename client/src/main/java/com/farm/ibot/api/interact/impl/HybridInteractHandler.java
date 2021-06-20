package com.farm.ibot.api.interact.impl;

import com.farm.ibot.api.accessors.Character;
import com.farm.ibot.api.accessors.*;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interact.InteractHandler;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.data.PlayerMethod;
import com.farm.ibot.api.interact.action.impl.*;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;

import java.awt.*;

public class HybridInteractHandler implements InteractHandler {
    private MouseInteractHandler mouseInteractHandler = new MouseInteractHandler();

    public boolean itemOnItem(Item item, Item item2) {
        if (Client.getSelectedItemId() != -1) {
            Client.deselectItem();
        }

        return item.interact("Use") && Time.sleep(() -> {
            return Client.getSelectedItemId() != -1;
        }) && item2.interact("Use");
    }

    public boolean itemOnObject(Item item, GameObject object) {
        ItemOnObjectAction.create(item, object).sendByMouse();
        return true;
    }

    public boolean playerInteract(String action, Player player) {
        PlayerAction.create(PlayerMethod.forName(action), player).sendByMouse();
        return true;
    }

    public boolean objectInteract(String action, GameObject gameObject) {
        ObjectAction.create(action, gameObject).sendByMouse();
        return true;
    }

    public boolean npcInteract(String action, Npc npc) {
        NpcAction.create(action, npc).sendByMouse();
        return true;
    }

    public boolean widgetInteract(String action, Widget widget) {
        return this.mouseInteractHandler.widgetInteract(action, widget);
    }

    public boolean menuInteract(String action) {
        return this.mouseInteractHandler.menuInteract(action);
    }

    public boolean groundItemInteract(String action, GroundItem groundItem) {
        GroundItemAction.create(action, groundItem).sendByMouse();
        return true;
    }

    public boolean itemOnCharacter(Item item, Character character) {
        if (this.itemInteract("Use", item) && Time.sleep(() -> {
            return Client.getSelectedItemId() == item.getId();
        })) {
            if (character instanceof Npc) {
                return this.npcInteract("Use", (Npc) character);
            }

            if (character instanceof Player) {
                return this.playerInteract("Use", (Player) character);
            }
        }

        return false;
    }

    public void walk(Tile tile) {
        WalkAction.create(tile).sendByMouse();
    }

    public boolean itemInteract(String action, Item item) {
        return Screen.CLIENT_SCREEN.contains(item.getBounds()) && this.mouseInteractHandler.itemInteract(action, item) ? true : this.itemInteract(ItemMethod.forName(action), item);
    }

    public boolean itemInteract(ItemMethod action, Item item) {
        if (action != null) {
            Point p = Random.human(item.getBounds());
            Mouse.move(p.x, p.y);
            ItemAction.create(action, item).sendByMouse();
            return true;
        } else {
            return false;
        }
    }
}
