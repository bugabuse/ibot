package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Region;
import com.farm.ibot.api.accessors.RegionTile;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IRegion;

public class DefaultRegion implements IRegion {
    public int getFocusedX(Object instance) {
        return (Integer) Wrapper.getStatic("Region.FocusedX");
    }

    public void setFocusedX(Object instance, Object x) {
        Wrapper.setStatic("Region.FocusedX", x);
    }

    public int getFocusedY(Object instance) {
        return (Integer) Wrapper.getStatic("Region.FocusedY");
    }

    public void setFocusedY(Object instance, Object y) {
        Wrapper.setStatic("Region.FocusedY", y);
    }

    public Region getRegion(Object instance) {
        return (Region) Wrapper.getStatic("Region.Region", Region.class);
    }

    public RegionTile[][][] getTiles(Object instance) {
        return (RegionTile[][][]) Wrapper.get("Region.Tiles", RegionTile[][][].class, instance);
    }

    public void setMinLevel(Object instance, Object minLevel) {
        Wrapper.set("Region.minLevel", instance, minLevel);
    }

    public int getMinLevel(Object instance) {
        return (Integer) Wrapper.get("Region.minLevel", instance);
    }

    public Object[][][] getTilesObject(Object instance) {
        return (Object[][][]) Wrapper.get("Region.Tiles", instance);
    }

    public boolean isCheckClick(Object instance) {
        return (Boolean) Wrapper.get("Region.checkClick", instance);
    }

    public void setCheckClick(Object instance, boolean value) {
        Wrapper.set("Region.checkClick", instance, value);
    }
}
