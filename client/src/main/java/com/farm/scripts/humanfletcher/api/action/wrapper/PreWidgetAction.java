package com.farm.scripts.humanfletcher.api.action.wrapper;

public class PreWidgetAction extends RectangleAction {
    public int widgetId;

    public PreWidgetAction(String menuOption, long time, int widgetId, int x, int y, int width, int height) {
        super(menuOption, time, x, y, width, height);
        this.widgetId = widgetId;
    }
}
