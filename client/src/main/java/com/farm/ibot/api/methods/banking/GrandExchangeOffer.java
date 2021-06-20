package com.farm.ibot.api.methods.banking;

import com.farm.ibot.api.accessors.GrandExchangeItem;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.data.WidgetId;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.init.Settings;

import java.awt.*;

public class GrandExchangeOffer {
    public Item item;
    public boolean isSelling;
    public int price;

    public GrandExchangeOffer(Item item, int price, boolean isSelling) {
        this.item = item;
        this.price = price;
        this.isSelling = isSelling;
    }

    private static Widget getBuyWidget() {
        int slot = getFirstFreeSlot();
        if (slot != -1) {
            Widget widget = Widgets.get(465, slot + 7, 0);
            if (widget != null && widget.isVisible()) {
                return widget;
            }
        }

        return null;
    }

    public static int getFirstFreeSlot() {
        GrandExchangeItem[] items = GrandExchangeItem.getSlots();

        for (int i = 0; i < items.length; ++i) {
            if (items[i].getId() == 0) {
                return i;
            }
        }

        return -1;
    }

    public boolean create() {
        GrandExchangeItem item = this.findGrandExchangeItem();
        if (!GrandExchange.open()) {
            return false;
        } else if (!GrandExchange.collect()) {
            return false;
        } else if (this.exists() && this.getStatus() == GrandExchangeOffer.Status.COMPLETED) {
            this.abort();
            return false;
        } else if (item != null && item.getPrice() == this.price) {
            return true;
        } else {
            if (item != null) {
                Debug.log("Price " + item.getPrice());
                Debug.log("Amount " + item.getAmount());
                if (!this.abort() || !GrandExchange.collect()) {
                    return false;
                }
            }

            return this.isSelling ? this.createSell() : this.createBuy();
        }
    }

    public boolean abort() {
        if (!GrandExchange.goToMainScreen()) {
            return false;
        } else {
            Widget abort = this.getAbortWidget();
            return abort != null && abort.interact("Abort offer") ? Time.sleepHuman(() -> {
                return GrandExchange.canCollect() && this.getStatus() == GrandExchangeOffer.Status.COMPLETED;
            }) : false;
        }
    }

    private boolean createSell() {
        Item offerItem = Inventory.container().get((i) -> {
            return i.getId() == this.item.getId() || i.getId() == this.item.getId() + 1;
        });
        if (offerItem != null) {
            offerItem.setWidget((Widget) null);
            if (!this.hasOfferOpened() || !this.isSellOptionsOpen()) {
                offerItem.interact(ItemMethod.OFFER_GE);
                if (!Time.sleep(() -> {
                    return this.hasOfferOpened() && this.isSellOptionsOpen();
                })) {
                    return false;
                }
            }

            if (this.setAmount(true) && this.setPrice()) {
                this.send();
                return Time.sleep(this::exists);
            }
        }

        return false;
    }

    private boolean createBuy() {

        if (!this.isBuyOptionsOpen()) {

            if (!GrandExchange.goToMainScreen()) {

                return false;
            }


            Widget widget = getBuyWidget();
            if (widget == null) {

                GrandExchange.goToMainScreen();
                return false;
            }


            widget.interact("");

            if (!Time.sleep(() -> {
                return this.isBuyOptionsOpen();
            })) {

                return false;
            }
        }


        ItemDefinition def = ItemDefinition.forId(this.item.getId());
        if (def == null) {

            return false;
        } else {

            if (!this.hasOfferOpened()) {

                if (!Time.sleep(3000, InputBox::isOpen)) {
                    Widgets.get((w) -> {
                        return w.getId() == 30474264 && w.getActions() != null;
                    }).interact("Choose item");
                    if (!Time.sleep(3000, InputBox::isOpen)) {

                        return false;
                    }


                    return false;
                }


                InputBox.input(def.name.substring(0, Random.next(3, def.name.length())).toLowerCase(), 2000, false);
                Widget[] itemWidgets = Widgets.getChildren(162, WidgetId.GrandExchange.ITEM_LIST);
                int index = 0;

                if (itemWidgets != null) {

                    Widget[] var4 = itemWidgets;
                    int var5 = itemWidgets.length;

                    for (int var6 = 0; var6 < var5; ++var6) {
                        Widget widget = var4[var6];
                        if (widget.getItemId() == this.item.getId()) {
                            if (index == 2) {
                                Time.sleep(500, 1600);
                                Keyboard.enter();
                            } else {
                                Time.sleep(500, 1600);
                                if (Interact.useNaturalMouse) {
                                    if (this.scrollToItemId(this.item.getId())) {
                                        Point p = Random.human(this.findRectangleForItem(this.item.getId()));
                                        Mouse.click(p.x, p.y);
                                    }
                                } else if (Settings.actionInteract) {
                                    (new Action(index - 2, 10616885, 57, 1, "", "", Random.human(13, 170), Random.human(370, 400))).send();
                                } else {
                                    (new Action(index - 2, 10616885, 57, 1, "", "", Random.human(13, 170), Random.human(370, 400))).sendByMouse();
                                }
                            }
                            break;
                        }

                        ++index;
                    }
                }


                if (!Time.sleep(() -> {
                    return this.hasOfferOpened();
                })) {

                    return false;
                }
            }


            if (this.setPrice() && this.setAmount(false)) {
                this.send();
                return Time.sleep(this::exists);
            } else {
                return this.exists();
            }
        }
    }

    private void send() {
        Widgets.get((w) -> {
            return w.getId() == 30474267;
        }).interact("");
        Time.sleep(1000, 2000);
    }

    private boolean setAmount(boolean clamp) {
        int amt = clamp ? MathUtils.clamp(this.item.getAmount(), 0, Inventory.container().getCount(this.item.getId(), this.item.getId() + 1)) : this.item.getAmount();
        if (this.getAmount() != amt) {
            Widgets.get(465, 24, 7).interact("");
            InputBox.input(this.item.getAmount());
            return Time.sleep(() -> {
                return this.getAmount() == amt;
            });
        } else {
            return true;
        }
    }

    private boolean setPrice() {
        if (this.getCurrentPrice() != this.price) {
            Widgets.get(465, 24, 12).interact("");
            InputBox.input(this.price);
            return Time.sleep(() -> {
                return this.getCurrentPrice() == this.price;
            });
        } else {
            return true;
        }
    }

    private int getAmount() {
        Widget widget = Widgets.get(465, 24, 32);
        if (widget != null && widget.getText().length() > 0) {
            String text = widget.getText().replaceAll(",", "");
            return Integer.parseInt(text);
        } else {
            return -1;
        }
    }

    public int getCurrentPrice() {
        Widget widget = Widgets.get(465, 24, 39);
        if (widget != null && widget.getText().length() > 0) {
            String text = widget.getText().split(" ")[0].replaceAll(",", "");
            return Integer.parseInt(text);
        } else {
            GrandExchangeItem item = this.findGrandExchangeItem();
            return item != null ? item.getPrice() : -1;
        }
    }

    public boolean exists() {
        return this.findGrandExchangeItem() != null;
    }

    private boolean hasOfferOpened() {
        ItemDefinition def = ItemDefinition.forId(this.item.getId());
        Widget itemWidget = Widgets.get(465, 24, 25);
        return itemWidget != null && itemWidget.isVisible() && def != null ? itemWidget.getText().equalsIgnoreCase(def.name) : false;
    }

    public boolean isSellOptionsOpen() {
        Widget itemWidget = Widgets.get(465, 24, 18);
        return itemWidget != null && itemWidget.isVisible() && itemWidget.getText().contains("Sell offer");
    }

    public boolean isBuyOptionsOpen() {
        Widget itemWidget = Widgets.get(465, 24, 18);
        return itemWidget != null && itemWidget.isVisible() && itemWidget.getText().contains("Buy offer");
    }

    private Widget getAbortWidget() {
        GrandExchangeItem item = this.findGrandExchangeItem();
        if (item != null) {
            int index = item.getIndex();
            return Widgets.get(465, 7 + index, 2);
        } else {
            return null;
        }
    }

    private Widget findWidgetForItem(int itemId) {
        Widget w = Widgets.get((ww) -> {
            return ww.getId() == 10616885;
        });
        Widget[] var3 = w.getChildren();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Widget child = var3[var5];
            if (child.getItemId() == itemId) {
                return child;
            }
        }

        return null;
    }

    private Rectangle findRectangleForItem(int itemId) {
        Widget w = Widgets.get((ww) -> {
            return ww.getId() == 10616885;
        });
        Widget[] var3 = w.getChildren();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Widget child = var3[var5];
            if (child.getItemId() == itemId) {
                return new Rectangle(child.getBounds().x, child.getBounds().y, child.getBounds().width + 100, child.getBounds().height);
            }
        }

        return new Rectangle(0, 0);
    }

    private boolean scrollToItemId(int itemId) {
        Rectangle inputBoxRectangle = new Rectangle(9, 367, 485, 104);

        for (int i = 0; i < 10; ++i) {
            Rectangle rect = this.findRectangleForItem(itemId);
            if (inputBoxRectangle.contains(rect)) {
                Time.sleep(2000, 3000);
                return true;
            }

            Mouse.move(Random.human(inputBoxRectangle));
            Mouse.scroll(rect.getCenterY() > inputBoxRectangle.getCenterY() ? 1 : -1);
            Time.sleep(1000, 2000);
        }

        return false;
    }

    public GrandExchangeItem findGrandExchangeItem() {
        GrandExchangeItem[] var1 = GrandExchangeItem.getSlots();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            GrandExchangeItem item = var1[var3];
            if (item != null && item.getId() == this.item.getId()) {
                return item;
            }
        }

        return null;
    }

    public GrandExchangeOffer.Status getStatus() {
        GrandExchangeItem item = this.findGrandExchangeItem();
        if (item != null) {
            switch (item.getState()) {
                case 2:
                case 10:
                    return GrandExchangeOffer.Status.IN_PROGRESS;
                case 5:
                case 13:
                    return GrandExchangeOffer.Status.COMPLETED;
                default:
                    return GrandExchangeOffer.Status.NONE;
            }
        } else {
            return GrandExchangeOffer.Status.NONE;
        }
    }

    public static enum Status {
        NONE((byte) -1),
        IN_PROGRESS((byte) 10),
        COMPLETED((byte) 13);

        public byte id;

        private Status(byte id) {
            this.id = id;
        }
    }
}
