package com.farm.ibot.api.interact.impl;

import com.farm.ibot.api.accessors.Character;
import com.farm.ibot.api.accessors.*;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interact.InteractHandler;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.data.PlayerMethod;
import com.farm.ibot.api.interact.action.impl.*;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;

public class ActionInteractHandler implements InteractHandler {
    public boolean menuInteract(String action) {
        return false;
    }

    public boolean groundItemInteract(String action, GroundItem groundItem) {
        GroundItemAction.create(action, groundItem).send();
        return true;
    }

    public boolean itemOnCharacter(Item item, Character character) {
        ItemAction.create(ItemMethod.USE, item).send();
        Time.sleep(10, 150);
        ItemOnCharacterAction.create(character).send();
        return false;
    }

    public void walk(Tile tile) {
        WalkAction.create(tile).send();
    }

    public boolean itemOnItem(Item item, Item item2) {
        ItemOnItemAction.create(item, item2).send();
        return true;
    }

    public boolean itemOnObject(Item item, GameObject object) {
        ItemOnObjectAction.create(item, object).send();
        return true;
    }

    public boolean itemInteract(String action, Item item) {
        ItemAction.create(ItemMethod.forName(action), item).send();
        return true;
    }

    public boolean itemInteract(ItemMethod action, Item item) {
        ItemAction.create(action, item).send();
        return true;
    }

    public boolean playerInteract(String action, Player player) {
        PlayerAction.create(PlayerMethod.forName(action), player).send();
        return true;
    }

    public boolean objectInteract(String action, GameObject gameObject) {
        ObjectAction.create(action, gameObject).send();
        return true;
    }

    public boolean npcInteract(String action, Npc npc) {
        NpcAction.create(action, npc).send();
        return true;
    }

    public boolean widgetInteract(String action, Widget widget) {
        WidgetAction.create(action, widget).send();
        return true;
    }
}
