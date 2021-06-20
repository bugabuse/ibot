package com.farm.scripts.humanfletcher.api.action.wrapper;

public class WidgetAction extends RectangleAction {
    public int widgetId;

    public WidgetAction(String menuOption, long time, int widgetId, int x, int y, int width, int height) {
        super(menuOption, time, x, y, width, height);
        this.widgetId = widgetId;
    }
}
