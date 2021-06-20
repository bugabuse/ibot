package com.farm.scripts.humanfletcher.api.action.wrapper;

import java.awt.*;

public class RectangleAction extends Action {
    public int x;
    public int y;
    public int width;
    public int height;

    public RectangleAction(String menuOption, long time, int x, int y, int width, int height) {
        super(menuOption, time);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getRectangle() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }
}
