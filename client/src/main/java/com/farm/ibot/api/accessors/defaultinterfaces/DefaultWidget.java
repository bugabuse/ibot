package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IWidget;

public class DefaultWidget implements IWidget {
    public Widget[][] hookGetWidgets(Object instance) {
        return (Widget[][]) Wrapper.getStatic("Widget.Widgets", Widget[][].class);
    }

    public Object[][] getWidgetsRaw(Object instance) {
        return (Object[][]) Wrapper.getStatic("Widget.Widgets");
    }

    public String getText(Object instance) {
        return (String) Wrapper.get("Widget.Text", instance);
    }

    public void setText(Object instance, Object text) {
        Wrapper.set("Widget.Text", instance, text);
    }

    public int getX(Object instance) {
        return (Integer) Wrapper.get("Widget.X", instance);
    }

    public int getY(Object instance) {
        return (Integer) Wrapper.get("Widget.Y", instance);
    }

    public int getScrollX(Object instance) {
        return (Integer) Wrapper.get("Widget.ScrollX", instance);
    }

    public int getScrollY(Object instance) {
        return (Integer) Wrapper.get("Widget.ScrollY", instance);
    }

    public String[] getActions(Object instance) {
        return (String[]) Wrapper.get("Widget.Actions", instance);
    }

    public int getTextureId(Object instance) {
        return (Integer) Wrapper.get("Widget.TextureId", instance);
    }

    public int[] getWidgetBoundsX(Object instance) {
        return (int[]) Wrapper.getStatic("Widget.WidgetBoundsX");
    }

    public int[] getWidgetBoundsY(Object instance) {
        return (int[]) Wrapper.getStatic("Widget.WidgetBoundsY");
    }

    public int getItemAmount(Object instance) {
        return (Integer) Wrapper.get("Widget.ItemAmount", instance);
    }

    public int getItemId(Object instance) {
        return (Integer) Wrapper.get("Widget.ItemId", instance);
    }

    public int[] getItemIds(Object instance) {
        return (int[]) Wrapper.get("Widget.ItemIds", instance);
    }

    public int[] getItemAmounts(Object instance) {
        return (int[]) Wrapper.get("Widget.ItemAmounts", instance);
    }

    public Widget[] getChildren(Object instance) {
        return (Widget[]) Wrapper.get("Widget.Children", Widget[].class, instance);
    }

    public int getId(Object instance) {
        return (Integer) Wrapper.get("Widget.Id", instance);
    }

    public int getParentUid(Object instance) {
        return (Integer) Wrapper.get("Widget.ParentUid", instance);
    }

    public int getIndex(Object instance) {
        return (Integer) Wrapper.get("Widget.Index", instance);
    }

    public boolean isHidden(Object instance) {
        return (Boolean) Wrapper.get("Widget.Hidden", instance);
    }

    public int getWidth(Object instance) {
        return (Integer) Wrapper.get("Widget.Width", instance);
    }

    public int getHeight(Object instance) {
        return (Integer) Wrapper.get("Widget.Height", instance);
    }

    public int getType(Object instance) {
        return (Integer) Wrapper.get("Widget.Type", instance);
    }

    public String getSelectedAction(Object instance) {
        return (String) Wrapper.get("Widget.SelectedAction", instance);
    }

    public int getLoopCycle(Object instance) {
        return (Integer) Wrapper.get("Widget.LoopCycle", instance);
    }

    public int getTextColor(Object instance) {
        return (Integer) Wrapper.get("Widget.textColor", instance);
    }

    public int getScrollMax(Object instance) {
        return (Integer) Wrapper.get("Widget.scrollMax", instance);
    }

    public Widget getParent(Object instance) {
        return (Widget) Wrapper.get("Widget.parent", instance);
    }
}
