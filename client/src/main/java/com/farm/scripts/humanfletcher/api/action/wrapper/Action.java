package com.farm.scripts.humanfletcher.api.action.wrapper;

public class Action {
    public long time;
    public String menuOption;

    public Action(String menuOption, long time) {
        this.menuOption = menuOption;
        this.time = time;
    }
}
