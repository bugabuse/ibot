package com.farm.ibot.api.wrapper.item;

import com.farm.ibot.api.accessors.Character;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interfaces.Interactable;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;

import java.awt.*;

public class Item implements Interactable {
    protected int id;
    protected int amount;
    protected int slot;
    private Widget widget;
    private Rectangle bounds;
    private ItemContainer container;

    public Item(int id, int amount, int slot, Rectangle bounds) {
        this(id, amount, slot);
        this.bounds = bounds;
    }

    public Item(int id, int amount, int slot, Widget widget) {
        this(id, amount, slot);
        this.widget = widget;
    }

    public Item(int id, int amount, int slot, Widget widget, Rectangle rect) {
        this(id, amount, slot, widget);
        this.bounds = rect;
    }

    public Item(int id, int amount, int slot) {
        this.id = id;
        this.amount = amount;
        this.slot = slot;
    }

    public Item(int id, int amount) {
        this(id, amount, -1);
    }

    public int getId() {
        return this.id;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Widget getWidget() {
        return this.widget;
    }

    public Item setWidget(Widget widget) {
        this.widget = widget;
        return this;
    }

    public Rectangle getBounds() {
        if (this.bounds != null) {
            return this.bounds;
        } else {
            return this.widget != null && Screen.CLIENT_SCREEN.contains(this.widget.getBounds()) ? this.widget.getBounds() : new Rectangle(0, 0, 30, 30);
        }
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean interact(String action) {
        return Interact.interactHandler.itemInteract(action, this);
    }

    public boolean interact(ItemMethod method) {
        return Interact.interactHandler.itemInteract(method, this);
    }

    public boolean interactWith(GameObject object) {
        return Interact.interactHandler.itemOnObject(this, object);
    }

    public boolean interactWith(Item item) {
        return Interact.interactHandler.itemOnItem(this, item);
    }

    public boolean interactWith(Character character) {
        return Interact.interactHandler.itemOnCharacter(this, character);
    }

    public String[] getActions() {
        return this.getDefinition().interfaceOptions;
    }

    public ItemDefinition getDefinition() {
        return ItemDefinition.forId(this.getId());
    }

    public boolean isInventoryItem() {
        return this.widget != null && this.widget.getId() == 9764864;
    }

    public boolean isSelected() {
        return this.isInventoryItem() && Client.getSelectedItemIndex() == this.slot;
    }

    public String getName() {
        return this.getDefinition().name;
    }

    public String toString() {
        return "Item[" + this.id + "," + this.amount + "]";
    }
}
