package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;

public class ItemOnObjectAction extends Action {
    private final Item item;
    private final GameObject gameObject;

    public ItemOnObjectAction(Item item, GameObject gameObject) {
        this.item = item;
        this.gameObject = gameObject;
    }

    public static ItemOnObjectAction create(Item item, GameObject gameObject) {
        return new ItemOnObjectAction(item, gameObject);
    }

    public void send() {
        ItemAction.create(ItemMethod.USE, this.item).send();
        Time.sleep(100, 500);
        (new ObjectAction((int) this.gameObject.getUid(), this.gameObject.getIdUnsafe(), 1)).send();
    }

    public void sendByMouse() {
        if (this.item != null) {
            this.item.interact("Use");
            Time.sleep(50, 250);
            (new ObjectAction((int) this.gameObject.getUid(), this.gameObject.getIdUnsafe(), 1)).sendByMouse();
        }

    }
}
