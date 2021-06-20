package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IGameConfig;

public class DefaultGameConfig implements IGameConfig {
    public boolean isRoofsEnabled(Object instance, Object value) {
        return (Boolean) Wrapper.get("GameConfig.RoofsEnabled", instance);
    }

    public int getResizableMode(Object instance) {
        return (Integer) Wrapper.get("GameConfig.ResizableMode", instance);
    }

    public void setRoofsEnabled(Object instance, Object value) {
        Wrapper.set("GameConfig.RoofsEnabled", instance, value);
    }
}
