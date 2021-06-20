package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IWidget;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.interfaces.Interactable;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

import java.awt.*;
import java.util.HashMap;

public class Widget extends Node implements Interactable {
    public static HashMap<Bot, Widget.WidgetsCache> widgetsCache = new HashMap();
    public int offsetX = 0;
    public int offsetY = 0;
    public int index1 = -1;
    public int index2 = -1;
    public int index3 = -1;
    private Point screenPointCached = null;
    private int visible = -1;

    public Widget(Object instance) {
        super(instance);
    }

    public static IWidget getWidgetInterface() {
        return Bot.get().accessorInterface.widgetInterface;
    }

    @HookName("Widget.Widgets")
    private static Widget[][] hookGetWidgets() {
        return getWidgetInterface().hookGetWidgets(null);
    }

    @HookName("Widget.WidgetBoundsX")
    public static int[] getWidgetBoundsX() {
        return getWidgetInterface().getWidgetBoundsX(null);
    }

    @HookName("Widget.WidgetBoundsY")
    public static int[] getWidgetBoundsY() {
        return getWidgetInterface().getWidgetBoundsY(null);
    }

    public static Object[][] getWidgetsRaw() {
        return getWidgetInterface().getWidgetsRaw(null);
    }

    public static Widget[][] getWidgets() {
        Bot bot = Bot.get();
        Widget.WidgetsCache cache = (Widget.WidgetsCache) widgetsCache.get(bot);
        if (cache == null) {
            cache = new Widget.WidgetsCache((Widget[][]) null, 0L);
            widgetsCache.put(bot, cache);
        }

        if (System.currentTimeMillis() - cache.lastTime > 600L || cache.widgets == null) {
            cache.lastTime = System.currentTimeMillis();
            cache.widgets = hookGetWidgets();
        }

        return cache.widgets;
    }

    private static int toChild(int id) {
        return id & '\uffff';
    }

    private static int toGroup(int id) {
        return id >>> 16;
    }

    @HookName("Widget.Text")
    public String getText() {
        return StringUtils.format(getWidgetInterface().getText(this.instance));
    }

    @HookName("Widget.Text")
    public void setText(String text) {
        getWidgetInterface().setText(this.instance, text);
    }

    @HookName("Widget.X")
    public int getX() {
        return getWidgetInterface().getX(this.instance);
    }

    @HookName("Widget.Y")
    public int getY() {
        return getWidgetInterface().getY(this.instance);
    }

    @HookName("Widget.ScrollX")
    public int getScrollX() {
        return getWidgetInterface().getScrollX(this.instance);
    }

    @HookName("Widget.ScrollY")
    public int getScrollY() {
        return getWidgetInterface().getScrollY(this.instance);
    }

    @HookName("Widget.scrollMax")
    public int getScrollMax() {
        return getWidgetInterface().getScrollMax(this.instance);
    }

    @HookName("Widget.Actions")
    public String[] getActions() {
        return getWidgetInterface().getActions(this.instance);
    }

    @HookName("Widget.TextureId")
    public int getTextureId() {
        return getWidgetInterface().getTextureId(this.instance);
    }

    @HookName("Widget.ItemAmount")
    public int getItemAmount() {
        return getWidgetInterface().getItemAmount(this.instance);
    }

    @HookName("Widget.ItemId")
    public int getItemId() {
        return getWidgetInterface().getItemId(this.instance);
    }

    @HookName("Widget.ItemIds")
    public int[] getItemIds() {
        return getWidgetInterface().getItemIds(this.instance);
    }

    @HookName("Widget.ItemAmounts")
    public int[] getItemAmounts() {
        return getWidgetInterface().getItemAmounts(this.instance);
    }

    @HookName("Widget.Children")
    public Widget[] getChildren() {
        return getWidgetInterface().getChildren(this.instance);
    }

    @HookName("Widget.Id")
    public int getId() {
        return getWidgetInterface().getId(this.instance);
    }

    @HookName("Widget.ParentUid")
    public int getParentUid() {
        return getWidgetInterface().getParentUid(this.instance);
    }

    @HookName("Widget.Index")
    public int getIndex() {
        return getWidgetInterface().getIndex(this.instance);
    }

    @HookName("Widget.Hidden")
    public boolean isHidden() {
        return getWidgetInterface().isHidden(this.instance);
    }

    @HookName("Widget.SelectedAction")
    public String getSelectedAction() {
        return getWidgetInterface().getSelectedAction(this.instance);
    }

    public void setHidden() {
        Wrapper.set("Widget.Hidden", this.instance, false);
    }

    @HookName("Widget.Width")
    public int getWidth() {
        return getWidgetInterface().getWidth(this.instance);
    }

    @HookName("Widget.Height")
    public int getHeight() {
        return getWidgetInterface().getHeight(this.instance);
    }

    @HookName("Widget.Type")
    public int getType() {
        return getWidgetInterface().getType(this.instance);
    }

    @HookName("Widget.LoopCycle")
    public int getLoopCycle() {
        return getWidgetInterface().getLoopCycle(this.instance);
    }

    @HookName("Widget.textColor")
    public int getTextColor() {
        return getWidgetInterface().getTextColor(this.instance);
    }

    @HookName("Widget.parent")
    public Widget getParentHook() {
        return getWidgetInterface().getParent(this.instance);
    }

    public int getRealIndex() {
        return this.getIndexes()[this.getIndexes().length - 1];
    }

    public int[] getIndexes() {
        int[] indexes = new int[]{this.index1, this.index2, this.index3};
        return indexes;
    }

    public int getScreenX() {
        Widget parent = this.getParent();
        int x = 0;
        if (parent != null) {
            x = parent.getScreenX();
        } else {
            int[] posx = getWidgetBoundsX();
            if (this.getIndex() != -1 && posx[this.getIndex()] > this.getX()) {
                return posx[this.getIndex()] + this.getX() + this.offsetX;
            }
        }

        return x + this.getX() + this.getScrollX() + this.offsetX;
    }

    public int getScreenY() {
        Widget parent = this.getParent();
        int y = 0;
        int y1;
        if (parent != null) {
            y = parent.getScreenY();
        } else {
            int[] posy = getWidgetBoundsY();
            if (this.getIndex() != -1 && posy[this.getIndex()] > this.getY()) {
                y1 = posy[this.getIndex()] + this.getY();
                return this.calculateOffset(y1) + y1 - this.getScrollY() + this.offsetY;
            }
        }

        y1 = y + this.getY();
        y1 = this.getScrollY();
        return y1 - y1 + this.offsetY;
    }

    public boolean isRendered() {
        return Client.getGameCycle() - this.getLoopCycle() < 40;
    }

    public boolean isVisible() {
        if (this.visible == -1) {
            if (this.isHidden()) {
                this.visible = 0;
                return false;
            } else {
                Widget parent = this.getParent();
                if (parent != null && !this.getParent().isVisible()) {
                    this.visible = 0;
                    return false;
                } else {
                    this.visible = 1;
                    return true;
                }
            }
        } else {
            return this.visible == 1;
        }
    }

    public Widget withOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
        return this;
    }

    public Point screenPoint() {
        if (this.screenPointCached != null) {
            return this.screenPointCached;
        } else {
            int i = this.getParentUid();
            if (i != -1) {
                Widget localObject1 = Widgets.get(i >> 16, i & '\uffff');
                Point localObject2 = localObject1.screenPoint();
                if (localObject2.x != -1 && localObject2.y != -1) {
                    boolean j = this.getScrollMax() == 0;
                    return this.screenPointCached = new Point(localObject2.x + this.getX() - (j ? localObject1.getScrollX() : 0), localObject2.y + this.getY() - (j ? localObject1.getScrollY() : 0));
                }
            }

            int[] localObject1 = getWidgetBoundsX();
            int[] localObject2 = getWidgetBoundsY();
            int j = this.getIndex();
            if (localObject1 != null && localObject2 != null && j >= 0 && j < localObject1.length && j < localObject2.length) {
                int x = localObject1[j];
                int y = localObject2[j];
                if ((new Rectangle(0, 0, 500, 300)).contains(new Point(x - this.getScrollX(), y - this.getScrollY()))) {
                    x += this.getX();
                    y += this.getY();
                }

                return this.screenPointCached = new Point(x - this.getScrollX(), y - this.getScrollY());
            } else {
                return this.screenPointCached = new Point(this.getX() - this.getScrollX(), this.getY() - this.getScrollY());
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(this.screenPoint().x, this.screenPoint().y, this.getWidth(), this.getHeight());
    }

    public Point getCenterPoint() {
        Rectangle bounds = this.getBounds();
        return new Point((int) bounds.getCenterX(), (int) bounds.getCenterY());
    }

    public int getParentCount() {
        Widget current = null;
        Widget last = this;

        for (int i = 0; i < 10; ++i) {
            if (current == null || ((Widget) current).getParentUid() == last.getParentUid()) {
                return i;
            }

            last = (Widget) current;
        }

        return 0;
    }

    public int getChildId() {
        return this.getId() & '\uffff';
    }

    public Widget getParent() {
        int rsParentId = this.getParentUid();
        return rsParentId != -1 ? Widgets.get(toGroup(rsParentId), toChild(rsParentId)) : null;
    }

    private int calculateOffset(int currY) {
        return (new Rectangle(0, 338, 519, 142)).contains(this.getScreenX(), currY + 23) ? 23 : 0;
    }

    public boolean interact(String action) {
        return Interact.interactHandler.widgetInteract(action, this);
    }

    private static class WidgetsCache {
        public Widget[][] widgets;
        public long lastTime;

        public WidgetsCache(Widget[][] widgets, long lastTime) {
            this.widgets = widgets;
            this.lastTime = lastTime;
        }
    }
}
