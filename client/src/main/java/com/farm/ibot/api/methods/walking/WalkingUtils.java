package com.farm.ibot.api.methods.walking;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;

public class WalkingUtils {
    public static Item getTeleportItem(String itemName) {
        return Inventory.container().get((i) -> {
            return i.getName().contains(itemName) && !i.getDefinition().isStackable();
        });
    }

    public static boolean teleportByNecklace(String location, String itemName) {
        return teleportByItemDialog(location, "Rub", itemName);
    }

    public static boolean teleportByItemDialog(String location, String itemAction, String itemName) {
        Item item = getTeleportItem(itemName);
        if (item != null) {
            Widgets.closeTopInterface();
            item.interact(itemAction);
            if (Time.sleep(() -> {
                return Widgets.get((w) -> {
                    return w.getText().contains(location);
                }) != null;
            })) {
                Dialogue.selectOptionThatContains(location);
                return true;
            }
        }

        return false;
    }
}
