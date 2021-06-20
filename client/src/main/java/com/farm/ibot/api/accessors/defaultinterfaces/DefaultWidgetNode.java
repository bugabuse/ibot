package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.HashTable;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IWidgetNode;

public class DefaultWidgetNode implements IWidgetNode {
    public int getUid(Object instance) {
        return (Integer) Wrapper.get("WidgetNode.Uid", instance);
    }

    public HashTable getCache(Object instance) {
        return (HashTable) Wrapper.getStatic("WidgetNode.Cache", HashTable.class);
    }
}
