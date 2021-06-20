package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.Widget;

public interface IWidget {
    Widget[][] hookGetWidgets(Object var1);

    Object[][] getWidgetsRaw(Object var1);

    String getText(Object var1);

    void setText(Object var1, Object var2);

    int getX(Object var1);

    int getY(Object var1);

    int getScrollX(Object var1);

    int getScrollY(Object var1);

    String[] getActions(Object var1);

    int getTextureId(Object var1);

    int[] getWidgetBoundsX(Object var1);

    int[] getWidgetBoundsY(Object var1);

    int getItemAmount(Object var1);

    int getItemId(Object var1);

    int[] getItemIds(Object var1);

    int[] getItemAmounts(Object var1);

    Widget[] getChildren(Object var1);

    int getId(Object var1);

    int getParentUid(Object var1);

    int getIndex(Object var1);

    boolean isHidden(Object var1);

    int getWidth(Object var1);

    int getHeight(Object var1);

    int getType(Object var1);

    String getSelectedAction(Object var1);

    int getLoopCycle(Object var1);

    int getTextColor(Object var1);

    int getScrollMax(Object var1);

    Widget getParent(Object var1);
}
