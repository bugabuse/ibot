package com.farm.ibot.api.interact.impl;

import com.farm.ibot.api.accessors.Character;
import com.farm.ibot.api.accessors.*;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interact.InteractHandler;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.impl.ItemAction;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;

public class HybridInteractHandler2 implements InteractHandler {
    private HybridInteractHandler menuInteractHandler = new HybridInteractHandler();
    private ActionInteractHandler actionInteractHandler = new ActionInteractHandler();

    public boolean itemOnItem(Item item, Item item2) {
        return this.actionInteractHandler.itemOnItem(item, item2);
    }

    public boolean itemOnObject(Item item, GameObject object) {
        return this.actionInteractHandler.itemOnObject(item, object);
    }

    public boolean playerInteract(String action, Player player) {
        return this.actionInteractHandler.playerInteract(action, player);
    }

    public boolean objectInteract(String action, GameObject gameObject) {
        return this.actionInteractHandler.objectInteract(action, gameObject);
    }

    public boolean npcInteract(String action, Npc npc) {
        return this.actionInteractHandler.npcInteract(action, npc);
    }

    public boolean widgetInteract(String action, Widget widget) {
        return this.actionInteractHandler.widgetInteract(action, widget);
    }

    public boolean menuInteract(String action) {
        return this.menuInteractHandler.menuInteract(action);
    }

    public boolean groundItemInteract(String action, GroundItem groundItem) {
        return this.actionInteractHandler.groundItemInteract(action, groundItem);
    }

    public boolean itemOnCharacter(Item item, Character character) {
        return this.actionInteractHandler.itemOnCharacter(item, character);
    }

    public void walk(Tile tile) {
        this.actionInteractHandler.walk(tile);
    }

    public boolean itemInteract(String action, Item item) {
        return this.itemInteract(ItemMethod.forName(action), item);
    }

    public boolean itemInteract(ItemMethod action, Item item) {
        if (action != null) {
            ItemAction.create(action, item).send();
            return true;
        } else {
            return false;
        }
    }
}
