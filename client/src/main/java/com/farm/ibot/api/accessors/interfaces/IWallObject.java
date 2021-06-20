package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.Renderable;

public interface IWallObject {
    long getUid(Object var1);

    int getAnimableX(Object var1);

    int getAnimableY(Object var1);

    Renderable getRenderable(Object var1);

    int getOrientation(Object var1);

    int getRealId(Object var1);

    void setRenderable(Object var1, Object var2);
}
