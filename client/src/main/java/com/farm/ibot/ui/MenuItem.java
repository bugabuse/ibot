package com.farm.ibot.ui;

public class MenuItem {
    public String text;
    public MouseAction actionEvent;

    public MenuItem(String text, MouseAction actionEvent) {
        this.text = text;
        this.actionEvent = actionEvent;
    }
}
