package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.interact.action.Action;

public class ClickAction extends Action {
    public ClickAction(int arg1, int arg2, int arg3, int arg4, String sArg1, String sArg2, int arg5, int arg6) {
        super(arg1, arg2, arg3, arg4, sArg1, sArg2, arg5, arg6);
    }

    public static Action create(int x, int y) {
        return new ClickAction(0, 0, 1006, 0, "", "", x, y);
    }
}
