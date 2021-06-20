package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.api.wrapper.item.Item;

public class Equipment {
    public static boolean isEquipped(int itemId) {
        return isEquipped((i) -> {
            return i.getId() == itemId;
        });
    }

    public static boolean isEquippedIgnoreAmmo(int itemId) {
        return isEquipped((i) -> {
            return i.getId() == itemId;
        }, true);
    }

    public static boolean isEquipped(Equipment.Slot slot, String str) {
        return isEquipped(slot, (i) -> {
            return i.getName().equalsIgnoreCase(str);
        });
    }

    public static boolean isEquipped(Equipment.Slot slot, Filter<Item> filter) {
        return filter.accept(slot.getItem());
    }

    public static boolean isEquipped(String str) {
        return isEquipped((i) -> {
            return i.getName().equalsIgnoreCase(str);
        });
    }

    public static boolean isEquipped(Filter<Item> filter) {
        return isEquipped(filter, false);
    }

    public static boolean isEquipped(Filter<Item> filter, boolean ignoreAmmoSlots) {
        Equipment.Slot[] var2 = Equipment.Slot.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Equipment.Slot slot = var2[var4];
            if ((!ignoreAmmoSlots || slot.index != -1) && isEquipped(slot, filter)) {
                return true;
            }
        }

        return false;
    }

    public static enum Slot {
        HELM(6, 0),
        CAPE(7, 1),
        AMULET(8, 2),
        SWORD(9, 3),
        CHEST(10, 4),
        SHIELD(11, 5),
        LEGS(12, 7),
        GLOVES(13, 9),
        BOOTS(14, 10),
        RING(15, -1),
        ARROWS(16, -1);

        public static long lastCheck = 0L;
        public int widgetId;
        public int index;
        private Item lastItem = new Item(-1, 0);

        private Slot(int widgetId, int index) {
            this.widgetId = widgetId;
            this.index = index;
        }

        public Widget getWidget() {
            Widget w1 = Widgets.get(387, this.widgetId, 1);
            Widget w2 = Widgets.get(84, this.widgetId + 5, 1);
            return w2 != null && !w1.isHidden() ? w2 : w1;
        }

        public Item getItem() {
            if (this.index != -1 && Player.getLocal().getComposite().getAppearance().length > this.index) {
                int id = Player.getLocal().getComposite().getAppearance()[this.index];
                if (id > 512) {
                    id -= 512;
                }

                if (id > -1) {
                    return new Item(id, 1, 1, this.getWidget());
                }
            } else {
                if (System.currentTimeMillis() - lastCheck <= 3000L) {
                    return this.lastItem;
                }

                lastCheck = System.currentTimeMillis();
                GameTab.EQUIPMENT.open();
                Time.waitNextGameCycle();
                Widget widget = this.getWidget();
                if (widget != null && !widget.isHidden()) {
                    widget = this.getWidget();
                    if (widget != null && widget.isVisible()) {
                        this.lastItem = new Item(widget.getItemId(), widget.getItemAmount(), 0, widget);
                        return this.lastItem;
                    }

                    this.lastItem = new Item(-1, 0);
                }
            }

            return new Item(-1, -1);
        }
    }
}
