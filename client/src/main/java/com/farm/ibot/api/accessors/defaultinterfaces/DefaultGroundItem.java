package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IGroundItem;

public class DefaultGroundItem implements IGroundItem {
    public int getId(Object instance) {
        return (Integer) Wrapper.get("GroundItem.Id", instance);
    }

    public int getAmount(Object instance) {
        return (Integer) Wrapper.get("GroundItem.Amount", instance);
    }
}
