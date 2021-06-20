package com.farm.ibot.api.methods.entities;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Widgets {
    public static Widget get(Filter<Widget> filter) {
        ArrayList<Widget> temp = getAll(filter, true);
        return temp.size() > 0 ? (Widget) temp.get(0) : null;
    }

    public static ArrayList<Widget> getAll(Filter<Widget> filter) {
        return getAll(filter, false);
    }

    private static ArrayList<Widget> getAll(Filter<Widget> filter, boolean limitToOne) {
        ArrayList<Widget> temp = new ArrayList();
        Widget[][] widgets1 = Widget.getWidgets();
        if (widgets1 == null) {
            return temp;
        } else {
            for (int x = 0; x < widgets1.length; ++x) {
                Widget[] widgets = widgets1[x];
                if (widgets != null) {
                    for (int y = 0; y < widgets.length; ++y) {
                        Widget widget = widgets[y];
                        widget.index1 = x;
                        widget.index2 = y;
                        if (widget != null) {
                            if (filter.accept(widget)) {
                                temp.add(widget);
                                if (limitToOne) {
                                    return temp;
                                }
                            }

                            Widget[] children = widget.getChildren();
                            if (children != null) {
                                for (int z = 0; z < children.length; ++z) {
                                    Widget child = children[z];
                                    if (child != null) {
                                        child.index1 = x;
                                        child.index2 = y;
                                        child.index3 = z;
                                    }

                                    if (child != null && filter.accept(child)) {
                                        temp.add(child);
                                        if (limitToOne) {
                                            return temp;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return temp;
        }
    }

    public static Widget get(int parentId, int childId, int childId2) {
        Widget[] children = getChildren(parentId, childId);
        if (children != null && children.length > childId2 && children[childId2] != null) {
            children[childId2].index1 = parentId;
            children[childId2].index2 = childId;
            children[childId2].index3 = childId2;
            return children[childId2];
        } else {
            return null;
        }
    }

    public static Widget[] getChildren(int parentId, int childId) {
        Widget widget = get(parentId, childId);
        if (widget != null) {
            Widget[] children = widget.getChildren();
            if (children != null) {
                for (int i = 0; i < children.length; children[i].index3 = i++) {
                    children[i].index1 = parentId;
                    children[i].index2 = childId;
                }

                return children;
            }
        }

        return new Widget[0];
    }

    public static Widget get(int parentId, int childId) {
        Object[][] widgets = Widget.getWidgetsRaw();
        if (widgets != null && parentId > -1 && childId > -1 && widgets.length > parentId && widgets[parentId] != null && widgets[parentId].length > childId) {
            if (widgets[parentId][childId] != null) {
                Widget newWidget = new Widget(widgets[parentId][childId]);
                newWidget.index1 = parentId;
                newWidget.index2 = childId;
                return newWidget;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Widget forId(int uid) {
        return get((w) -> {
            return w.getId() == uid;
        });
    }

    public static Widget getCloseButton() {
        return get((w) -> {
            return (w.getTextureId() == 535 || w.getTextureId() == 536 || w.getTextureId() == 429) && w.isVisible() && Screen.GAME_SCREEN.contains(w.getBounds());
        });
    }

    public static boolean closeTopInterface() {
        Keyboard.press(27);
        Time.sleep(200, 500);
        return true;
    }

    public static boolean isValid(int id, int childId) {
        Widget widget = get(id, childId);
        return widget != null && widget.isVisible();
    }

    public static boolean isValid(int id) {
        Widget widget = get(id, 0);
        return widget != null && widget.isVisible();
    }

    public static Widget getTopAt(Point point, Filter<Widget> filter) {
        Widget topWidget = null;
        Iterator var3 = getAll((w) -> {
            return w.isRendered() && (w.getParent() == null || w.getParent().getBounds().contains(w.getCenterPoint()));
        }).iterator();

        Widget widget;
        do {
            if (!var3.hasNext()) {
                return (Widget) topWidget;
            }

            widget = (Widget) var3.next();
        } while (!filter.accept(widget) || !widget.isVisible() || !widget.getBounds().contains(point));

        return widget;
    }
}
