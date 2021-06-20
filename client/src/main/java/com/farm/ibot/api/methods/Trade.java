package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.impl.ItemAction;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;

import java.util.ArrayList;
import java.util.Iterator;

public class Trade {
    public static final int SCREEN_NONE = 0;
    public static final int SCREEN_FIRST = 1;
    public static final int SCREEN_SECOND = 2;
    public static final int TRADE_SCREEN_ONE_PARENT = 335;
    public static final int TRADE_SCREEN_ONE_CHILD_MY_ITEMS = 25;
    public static final int TRADE_SCREEN_TWO_PARENT = 334;
    public static final int TRADE_SCREEN_TWO_PLAYER_NAME = 30;
    private static final int TRADE_SCREEN_ONE_CHILD_THEIR_ITEMS = 28;
    private static final int TRADE_SCREEN_ONE_PLAYER_NAME = 9;
    private static final int TRADE_SCREEN_ONE_ACCEPT_BUTTON = 10;
    private static final int TRADE_SCREEN_ONE_DECLINE_BUTTON = 13;
    private static final int TRADE_SCREEN_TWO_ACCEPT_BUTTON = 13;
    private static final int TRADE_SCREEN_TWO_DECLINE_BUTTON = 14;

    public static boolean isOpen() {
        return getOpenedScreen() != 0;
    }

    public static int getOpenedScreen() {
        Widget child = Widgets.get(335, 25);
        if (child != null && !child.isVisible()) {
            Debug.log("1 " + Client.getGameCycle() + " " + child.getLoopCycle());
        }

        if (child != null && child.isVisible() && child.getChildren() != null && child.getChildren().length > 50) {
            return 1;
        } else {
            child = Widgets.get(334, 0);
            if (child != null && !child.isVisible()) {
                Debug.log("2 " + Client.getGameCycle() + " " + child.getLoopCycle());
            }

            return child != null && child.isVisible() && getOponentNameSecondScreen().length() > 0 ? 2 : 0;
        }
    }

    public static void decline() {
        if (isOpen()) {
            Widgets.closeTopInterface();
            Time.sleep(3000, () -> {
                return !isOpen();
            });
        }
    }

    public static void accept() {
        if (!hasAccepted()) {
            Widget wc;
            if (getOpenedScreen() == 1) {
                wc = Widgets.get(335, 10);
                wc.interact("");
                Time.sleep(3000, () -> {
                    return getOpenedScreen() != 1;
                });
            } else if (getOpenedScreen() == 2) {
                wc = Widgets.get(334, 13);
                wc.interact("");
                Time.sleep(3000, () -> {
                    return getOpenedScreen() != 2;
                });
            }

        }
    }

    public static boolean hasAccepted() {
        Widget widget = null;
        if (getOpenedScreen() == 1) {
            widget = Widgets.get(335, 30);
        } else {
            widget = Widgets.get(334, 4);
        }

        return widget != null && widget.getText().contains("Waiting for other player");
    }

    public static boolean hasOtherAccepted() {
        Widget widget;
        if (getOpenedScreen() == 1) {
            widget = Widgets.get(335, 30);
        } else {
            widget = Widgets.get(334, 4);
        }

        return widget != null && widget.getText().contains("has accepted");
    }

    private static String getOponentNameSecondScreen() {
        Widget wc = Widgets.get(334, 30);
        if (wc != null) {
            String text = wc.getText();
            String[] arr = text != null ? text.split("<br>") : null;
            if (arr != null && arr.length > 1) {
                return arr[1];
            }
        }

        return "";
    }

    public static String getOponentName() {
        if (getOpenedScreen() == 1) {
            Widget wc = Widgets.get(335, 9);
            if (wc != null && wc.getText() != null) {
                String text = wc.getText();
                if (text != null) {
                    return text.split(" has")[0];
                }

                return text;
            }
        } else if (getOpenedScreen() == 2) {
            return getOponentNameSecondScreen();
        }

        return "";
    }

    public static ItemContainer getMyItems() {
        return getOpenedScreen() == 1 ? new ItemContainer(Widgets.get(335, 25).getChildren()) : new ItemContainer();
    }

    public static ItemContainer getOponentItems() {
        return getOpenedScreen() == 1 ? new ItemContainer(Widgets.get(335, 28).getChildren()) : new ItemContainer();
    }

    public static int getSecondScreenOponentAmount(String itemName) {
        try {
            for (int i = 0; i < 15; ++i) {
                Widget w = Widgets.get(334, 29, i);
                if (w != null) {
                    String text = w.getText();
                    if (text != null && text.startsWith(itemName + "<")) {
                        text = text.split("f00>")[1];
                        text = text.replace(",", "");
                        return Integer.parseInt(text);
                    }
                }
            }
        } catch (Exception var4) {
        }

        return -1;
    }

    public static boolean add(int itemId, int amount) {
        int amount1 = amount - getMyItems().getCount(itemId);
        if (getOpenedScreen() == 2) {
            return true;
        } else if (getMyItems().contains(itemId, amount)) {
            return true;
        } else if (Inventory.get(itemId) == null) {
            return false;
        } else if (amount <= 0) {
            return true;
        } else {
            return Inventory.getCount(itemId) >= amount1 && offer(itemId, amount1) ? Time.sleep(5000, () -> {
                return getMyItems().contains(itemId, amount1);
            }) : false;
        }
    }

    private static boolean offer(int itemId, int amount) {
        ArrayList<Action> requiredActions = new ArrayList();
        int current = amount;
        boolean tooManyActions = false;
        boolean var5 = false;

        while (true) {
            if (requiredActions.size() > 5) {
                tooManyActions = true;
                break;
            }

            if (current <= 0) {
                break;
            }

            if (current >= 10) {
                current -= 10;
                requiredActions.add(ItemAction.create(ItemMethod.OFFER_10, Inventory.container().getFromLastSlot(itemId)));
            } else if (current >= 5) {
                current -= 5;
                requiredActions.add(ItemAction.create(ItemMethod.OFFER_5, Inventory.container().getFromLastSlot(itemId)));
            } else if (current >= 1) {
                --current;
                requiredActions.add(ItemAction.create(ItemMethod.OFFER_1, Inventory.container().getFromLastSlot(itemId)));
            }
        }

        if (Inventory.getCount(itemId) <= amount) {
            Inventory.get(itemId).setWidget(Widgets.forId(22020096)).interact("Offer-All");
            return Time.waitInventoryChange();
        } else if (tooManyActions) {
            Inventory.get(itemId).setWidget(Widgets.forId(22020096)).interact("Offer-X");
            return InputBox.input(amount) ? Time.waitInventoryChange() : false;
        } else {
            Iterator var6 = requiredActions.iterator();

            while (var6.hasNext()) {
                Action action = (Action) var6.next();
                if (action instanceof ItemAction) {
                    Item item = ((ItemAction) action).item;
                    item.setWidget(Widgets.forId(22020096));
                    item.interact(((ItemAction) action).method.stringValue);
                }
            }

            Time.sleep(5, 30);
            return true;
        }
    }

    public static int getValue() {
        try {
            Widget widget = Widgets.get(335, 24);
            if (widget != null) {
                String str = widget.getText();
                str = str.split("<col=ffffff>")[1].split("</col>")[0].replace(",", "").replace("One", "1").replace("Two", "2").replace("Three", "3");
                int i = Integer.parseInt(str);
                return i;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return -1;
    }
}
