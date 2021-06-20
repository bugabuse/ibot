package com.farm.scripts.humanfletcher.api.util;

import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;

public class ItemMover {
    public static boolean moveToSlot(ItemContainer container, int itemId, int slot) {
        Item item = container.get(itemId);
        if (item.getSlot() == slot) {
            return true;
        } else {
            Mouse.move((int) item.getBounds().getCenterX() + Random.next(0, 10), (int) item.getBounds().getCenterY() + Random.next(0, 10));
            Mouse.press(Mouse.getLocation().x, Mouse.getLocation().y, 1, 0);
            Time.sleep(10, 200);
            Mouse.move((int) container.getBoundsForSlot(slot).getCenterX(), (int) container.getBoundsForSlot(slot).getCenterY());
            Mouse.release(Mouse.getLocation().x, Mouse.getLocation().y, 1, 0);
            Time.sleep(300, 600);
            return false;
        }
    }
}
