package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.wrapper.item.Item;
import com.google.common.primitives.Ints;

import java.awt.*;
import java.util.Arrays;

public class ItemAction extends Action {
    public Item item;
    public ItemMethod method;

    public ItemAction(int slot, int widgetUID, int index, int itemId) {
        super(slot, widgetUID, index, itemId, "", "", 0, 0);
    }

    public static Action create(ItemMethod method, Item item) {
        ItemAction itemAction = null;
        if (item != null && item.getWidget() != null) {
            if (!item.getWidget().isRendered()) {

                Widget realWidget = Widgets.get((w) -> {
                    if (!w.isRendered()) {
                        return false;
                    } else if (w.getItemId() == item.getId() || w.getItemIds() != null && Ints.contains(w.getItemIds(), item.getId())) {
                        Debug.log(w.getId() + "   " + Arrays.toString(w.getIndexes()));
                        if (item.getWidget().getParent() != null && item.getWidget().getParent().getBounds().contains(w.getBounds().getLocation())) {
                            return true;
                        } else if (w.getParent() != null && w.getParent().getBounds().contains(item.getWidget().getBounds().getLocation())) {
                            return true;
                        } else if (item.getWidget().getBounds().contains(w.getBounds())) {
                            return true;
                        } else {
                            return w.getBounds().contains(item.getWidget().getBounds());
                        }
                    } else {
                        return false;
                    }
                });
                if (realWidget != null) {
                    Debug.log("Real widget: " + realWidget.getId());
                    Debug.log("Other widget " + item.getWidget().getId());
                    item.setWidget(realWidget);
                }
            }

            if (method.isBank) {
                itemAction = new ItemAction(item.getSlot(), item.getWidget().getId(), method.value, method.additionalIndex);
            } else {
                itemAction = new ItemAction(item.getSlot(), item.getWidget().getId(), method.value, item.getId());
            }
        } else {
            Debug.log("Item null: " + (item == null));
            Debug.log("Widget null: " + (item.getWidget() == null));
            if (method == ItemMethod.OFFER_GE) {
                itemAction = new ItemAction(item.getSlot(), 30605312, method.value, method.additionalIndex);
            }
        }

        if (itemAction != null) {
            Point p = Random.human(item.getBounds());
            p.translate(Random.next(-15, 15), Random.next(-15, 15));
            itemAction.clickPointX = p.x;
            itemAction.clickPointY = p.y;
            itemAction.item = item;
            itemAction.method = method;
            return itemAction;
        } else {
            return new Action("ItemAction");
        }
    }
}
