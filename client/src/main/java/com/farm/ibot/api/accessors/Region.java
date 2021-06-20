package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IRegion;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Region extends Wrapper {
    public Region(Object instance) {
        super(instance);
    }

    public static IRegion getRegionInterface() {
        return Bot.get().accessorInterface.regionInterface;
    }

    @HookName("Region.FocusedX")
    public static int getFocusedX() {
        return getRegionInterface().getFocusedX(null);
    }

    @HookName("Region.FocusedX")
    public static void setFocusedX(int x) {
        getRegionInterface().setFocusedX(null, x);
    }

    @HookName("Region.FocusedY")
    public static int getFocusedY() {
        return getRegionInterface().getFocusedY(null);
    }

    @HookName("Region.FocusedY")
    public static void setFocusedY(int y) {
        getRegionInterface().setFocusedY(null, y);
    }

    @HookName("Region.Region")
    public static Region getRegion() {
        return getRegionInterface().getRegion(null);
    }

    @HookName("Region.Tiles")
    public RegionTile[][][] getTiles() {
        return getRegionInterface().getTiles(this.instance);
    }

    @HookName("Region.minLevel")
    public int getMinLevel() {
        return getRegionInterface().getMinLevel(this.instance);
    }

    @HookName("Region.minLevel")
    public void setMinLevel(int minLevel) {
        getRegionInterface().setMinLevel(this.instance, minLevel);
    }

    @HookName("Region.checkClick")
    public boolean isCheckClick() {
        return getRegionInterface().isCheckClick(this.instance);
    }

    @HookName("Region.checkClick")
    public void setCheckClick(boolean value) {
        getRegionInterface().setCheckClick(this.instance, value);
    }

    public Object[][][] getTilesObject() {
        return getRegionInterface().getTilesObject(this.instance);
    }
}
