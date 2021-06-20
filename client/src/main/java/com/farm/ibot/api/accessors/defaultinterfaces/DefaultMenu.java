package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IMenu;

public class DefaultMenu implements IMenu {
    public String[] getActionNames(Object instance) {
        return (String[]) Wrapper.getStatic("Menu.ActionNames");
    }

    public String[] getActions(Object instance) {
        return (String[]) Wrapper.getStatic("Menu.Actions");
    }

    public int getCount(Object instance) {
        return (Integer) Wrapper.getStatic("Menu.Count");
    }

    public void setCount(Object instance, Object value) {
        Wrapper.setStatic("Menu.Count", value);
    }

    public int getHeight(Object instance) {
        return (Integer) Wrapper.getStatic("Menu.Height");
    }

    public int getWidth(Object instance) {
        return (Integer) Wrapper.getStatic("Menu.Width");
    }

    public int[] getOpcodes(Object instance) {
        return (int[]) Wrapper.getStatic("Menu.Opcodes");
    }

    public String[] getOptions(Object instance) {
        return (String[]) Wrapper.getStatic("Menu.Options");
    }

    public int[] getVariable(Object instance) {
        return (int[]) Wrapper.getStatic("Menu.Variable");
    }

    public boolean isVisible(Object instance) {
        return (Boolean) Wrapper.getStatic("Menu.Visible");
    }

    public void setVisible(Object instance, Object visible) {
        Wrapper.setStatic("Menu.Visible", visible);
    }

    public int getX(Object instance) {
        return (Integer) Wrapper.getStatic("Menu.X");
    }

    public int getY(Object instance) {
        return (Integer) Wrapper.getStatic("Menu.Y");
    }

    public int[] getXInteractions(Object instance) {
        return (int[]) Wrapper.getStatic("Menu.XInteractions");
    }

    public int[] getYInteractions(Object instance) {
        return (int[]) Wrapper.getStatic("Menu.YInteractions");
    }
}
