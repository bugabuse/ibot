package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.wrapper.MenuNode;

public class MenuNodeAction extends Action {
    public MenuNodeAction(int arg1, int arg2, int arg3, int arg4, String arg5, String arg6, int arg7, int arg8) {
        super(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    public static Action create(MenuNode node) {
        return (Action) (node != null ? new MenuNodeAction(node.getXInteraction(), node.getYInteraction(), node.getOpcodes(), node.getVariable(), node.getAction(), node.getOption(), Mouse.getLocation().x, Mouse.getLocation().y) : new Action("MenuNodeAction"));
    }
}
