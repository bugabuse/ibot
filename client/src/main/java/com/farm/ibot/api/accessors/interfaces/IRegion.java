package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.Region;
import com.farm.ibot.api.accessors.RegionTile;

public interface IRegion {
    int getFocusedX(Object var1);

    void setFocusedX(Object var1, Object var2);

    int getFocusedY(Object var1);

    void setFocusedY(Object var1, Object var2);

    Region getRegion(Object var1);

    RegionTile[][][] getTiles(Object var1);

    void setMinLevel(Object var1, Object var2);

    int getMinLevel(Object var1);

    Object[][][] getTilesObject(Object var1);

    boolean isCheckClick(Object var1);

    void setCheckClick(Object var1, boolean var2);
}
