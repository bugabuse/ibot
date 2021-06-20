package com.farm.ibot.api.accessors.interfaces;

public interface IMenu {
    String[] getActionNames(Object var1);

    String[] getActions(Object var1);

    int getCount(Object var1);

    void setCount(Object var1, Object var2);

    int getHeight(Object var1);

    int getWidth(Object var1);

    int[] getOpcodes(Object var1);

    String[] getOptions(Object var1);

    int[] getVariable(Object var1);

    boolean isVisible(Object var1);

    void setVisible(Object var1, Object var2);

    int getX(Object var1);

    int getY(Object var1);

    int[] getXInteractions(Object var1);

    int[] getYInteractions(Object var1);
}
