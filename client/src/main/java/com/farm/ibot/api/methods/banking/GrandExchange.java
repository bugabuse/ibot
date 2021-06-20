package com.farm.ibot.api.methods.banking;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;

public class GrandExchange {
    public static boolean open() {
        if (isOpen()) {
            return true;
        } else if (!WebWalking.walkTo(new Tile(3168, 3488, 0), 8)) {
            return false;
        } else {
            Npc object = Npcs.get((o) -> {
                return o.getName().toLowerCase().contains("exchange");
            });
            return object != null && object.interact("Exchange") ? Time.sleep(GrandExchange::isOpen) : false;
        }
    }

    public static boolean canCollect() {
        Widget widget = Widgets.get(465, 6, 1);
        return widget != null && widget.isVisible();
    }

    public static boolean collect() {
        Widget widget = Widgets.get(465, 6, 1);
        if (widget != null && widget.isVisible()) {
            widget.interact("");
            return Time.sleepHuman(() -> {
                return !canCollect();
            });
        } else {
            return !canCollect();
        }
    }

    public static boolean isMainScreenOpen() {
        Widget widget = Widgets.get(465, 7, 2);
        return widget != null && widget.isRendered();
    }

    public static boolean isOpen() {
        Widget widget = Widgets.get(465, 3, 0);
        return widget != null && widget.isRendered();
    }

    public static boolean goToMainScreen() {
        if (isMainScreenOpen()) {
            return true;
        } else if (!open()) {
            return false;
        } else if (!collect()) {
            return false;
        } else {
            Widget goBack = Widgets.get(465, 4);
            if (goBack != null && goBack.isVisible()) {
                goBack.interact("");
                return Time.sleepHuman(GrandExchange::isMainScreenOpen);
            } else {
                return false;
            }
        }
    }

    public static boolean ensurePrice(int itemId, int price) {
        GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(itemId, 1), price, false);
        if (buyOffer.exists() && buyOffer.price != price) {
            buyOffer.abort();
            return false;
        } else {
            return true;
        }
    }
}
