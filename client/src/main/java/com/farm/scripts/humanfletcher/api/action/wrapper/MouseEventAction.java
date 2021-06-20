package com.farm.scripts.humanfletcher.api.action.wrapper;

public class MouseEventAction extends Action {
    public int x;
    public int y;
    public int button;
    public boolean mouseMove;

    public MouseEventAction(long time, int x, int y, int button, boolean mouseMove) {
        super("", time);
        this.x = x;
        this.y = y;
        this.button = button;
        this.mouseMove = mouseMove;
    }
}
