package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Random;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class WidgetAction extends Action {
    public static HashMap<Integer, Integer[]> realWidgetsCache = new HashMap();

    public WidgetAction(int arg1, int arg2, int arg3, int arg4, String sArg1, String sArg2, int arg5, int arg6) {
        super(arg1, arg2, arg3, arg4, sArg1, sArg2, arg5, arg6);
    }

    public static Action create(Widget widget) {
        if (widget != null) {
            int index = widget.getParentCount() > 1 ? widget.getIndex() : -1;
            Point p = Random.human(widget.getBounds());
            p.translate(Random.next(-15, 15), Random.next(-15, 15));
            return new WidgetAction(index, widget.getId(), 57, 1, "", "", p.x, p.y);
        } else {
            return new Action("WidgetAction");
        }
    }

    public static Action create(ItemMethod action, Widget widget) {
        return null;
    }

    public static Action create(String action, Widget widget) {
        return create(action, widget, false);
    }

    public static Action create(String action, Widget widget, boolean searchRealWidget) {
        if (widget == null) {
            return new Action("WidgetAction");
        } else {
            if (action.length() <= 0 && widget.getActions() != null && widget.getActions().length > 0) {
                action = widget.getActions()[0];
            }

            Point widgetPoint = new Point((int) widget.getBounds().getCenterX(), (int) widget.getBounds().getCenterY());
            Widget realWidget = Widgets.getTopAt(widgetPoint, (w) -> {
                return w.getActions() != null && w.getActions().length > 0;
            });
            if (realWidget != null) {
                Debug.log("realWidget: " + realWidget.getId() + "   " + Arrays.toString(widget.getActions()) + "   " + Arrays.toString(widget.getIndexes()));
                Debug.log("Widget: " + widget.getId() + "   " + Arrays.toString(widget.getActions()) + "   " + Arrays.toString(widget.getActions()) + "   " + Arrays.toString(widget.getIndexes()));
                widget = realWidget;
            }

            Debug.log("Parents " + widget.getParentCount());
            Debug.log("Index " + widget.getRealIndex());
            int index = widget.getIndexes().length > 2 ? widget.getRealIndex() : -1;
            Point p = Random.human(widget.getBounds());
            p.translate(Random.next(-15, 15), Random.next(-15, 15));
            int idx = ItemMethod.forName(action) != null ? ItemMethod.forName(action).value : 57;
            int actionIndex = 1;
            String[] actions = widget.getActions();

            for (int i = 0; actions != null && i < actions.length; ++i) {
                if (actions[i].equalsIgnoreCase(action)) {
                    actionIndex = 1 + i;
                    break;
                }
            }

            return new WidgetAction(index, widget.getId(), idx, actionIndex, action, "", p.x, p.y);
        }
    }

    private static int widgetHashcode(Widget widget) {
        return Arrays.hashCode(widget.getIndexes());
    }

    public static Action create(int widgetId) {
        Widget widget = Widgets.get((f) -> {
            return f.getId() == widgetId;
        });
        Point p = new Point(-1, -1);
        if (widget != null) {
            return create(widget);
        } else {
            p.translate(Random.next(-15, 15), Random.next(-15, 15));
            return new WidgetAction(widgetId >> 16, widget.getId(), 57, 1, "", "", p.x, p.y);
        }
    }

    public static Action create(int widgetId, int index) {
        Widget widget = Widgets.get((f) -> {
            return f.getId() == widgetId;
        });
        Point p = new Point(-1, -1);
        if (widget != null) {
            p = Random.human(widget.getBounds());
            p.translate(Random.next(-15, 15), Random.next(-15, 15));
        }

        return new WidgetAction(index, widgetId, 57, 1, "", "", p.x, p.y);
    }
}
