package com.farm.ibot.api.accessors.interfaces;

public interface ICamera {
    int getX(Object var1);

    int getY(Object var1);

    int getZ(Object var1);

    int getPitch(Object var1);

    int getYaw(Object var1);

    void setX(Object var1, Object var2);

    void setY(Object var1, Object var2);

    void setZ(Object var1, Object var2);

    void setPitch(Object var1, Object var2);

    void setYaw(Object var1, Object var2);
}
