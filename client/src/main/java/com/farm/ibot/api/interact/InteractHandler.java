package com.farm.ibot.api.interact;

import com.farm.ibot.api.accessors.Character;
import com.farm.ibot.api.accessors.*;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;

public interface InteractHandler {
    boolean itemOnItem(Item var1, Item var2);

    boolean itemOnObject(Item var1, GameObject var2);

    boolean itemInteract(String var1, Item var2);

    boolean itemInteract(ItemMethod var1, Item var2);

    boolean playerInteract(String var1, Player var2);

    boolean objectInteract(String var1, GameObject var2);

    boolean npcInteract(String var1, Npc var2);

    boolean widgetInteract(String var1, Widget var2);

    boolean menuInteract(String var1);

    boolean groundItemInteract(String var1, GroundItem var2);

    boolean itemOnCharacter(Item var1, Character var2);

    void walk(Tile var1);
}
