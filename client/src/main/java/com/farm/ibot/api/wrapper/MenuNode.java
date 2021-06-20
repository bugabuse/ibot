package com.farm.ibot.api.wrapper;

import com.farm.ibot.api.accessors.Menu;

import java.awt.*;

public class MenuNode {
    private final int index;
    private final int onScreenIndex;

    public MenuNode(int realIndex, int index) {
        this.index = index;
        this.onScreenIndex = realIndex;
    }

    public static boolean interact() {
        return false;
    }

    public String getActionName() {
        return Menu.getActionNames()[this.index];
    }

    public String getAction() {
        return Menu.getActions()[this.index];
    }

    public int getOpcodes() {
        return Menu.getOpcodes()[this.index];
    }

    public String getOption() {
        return Menu.getOptions()[this.index];
    }

    public int getId() {
        return Menu.getVariable()[this.index];
    }

    public Rectangle getBounds() {
        return Menu.getChildBounds(this.onScreenIndex);
    }

    public int getIndex() {
        return this.index;
    }

    public int getRealIndex() {
        return this.onScreenIndex;
    }

    public int getXInteraction() {
        return Menu.getXInteractions()[this.index];
    }

    public int getYInteraction() {
        return Menu.getYInteractions()[this.index];
    }

    public int getVariable() {
        return Menu.getVariable()[this.index];
    }
}
