package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IGroundItem;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.interfaces.Animable;
import com.farm.ibot.api.interfaces.Interactable;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;

import java.awt.*;

public class GroundItem extends Node implements Animable, Interactable {
    public int localX;
    public int localY;

    public GroundItem(Object instance, int localX, int localY) {
        super(instance);
        this.localX = localX;
        this.localY = localY;
    }

    public static IGroundItem getInterface() {
        return Bot.get().accessorInterface.groundItemInterface;
    }

    @HookName("GroundItem.Id")
    public int getId() {
        return getInterface().getId(this.instance);
    }

    @HookName("GroundItem.Amount")
    public int getAmount() {
        return getInterface().getAmount(this.instance);
    }

    public String[] getActions() {
        return this.getDefinition().interfaceOptions;
    }

    public ItemDefinition getDefinition() {
        return ItemDefinition.forId(this.getId());
    }

    public int getAnimableX() {
        return this.localX * 128;
    }

    public int getAnimableY() {
        return this.localY * 128;
    }

    public Point getScreenPoint() {
        return Screen.centroid(this.getPosition().getBounds());
    }

    public Tile getPosition() {
        Tile tile = (new Tile(this.getAnimableX(), this.getAnimableY(), Client.getPlane())).toWorldTile();
        tile.setModelHeight(this.getModelHeight());
        return tile;
    }

    public int getModelHeight() {
        ItemLayer layer = this.getItemLayer();
        return layer != null ? layer.getHeight() : 0;
    }

    public boolean interact(String action) {
        return Interact.interactHandler.groundItemInteract(action, this);
    }

    public ItemLayer getItemLayer() {
        RegionTile tile = RegionTile.get(this.localX, this.localY, Client.getPlane());
        return tile != null ? tile.getItemLayer() : null;
    }
}
