package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IWidgetNode;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class WidgetNode extends Wrapper {
    public WidgetNode(Object instance) {
        super(instance);
    }

    public static IWidgetNode getWidgetNodeInterface() {
        return Bot.get().accessorInterface.widgetNodeInterface;
    }

    @HookName("WidgetNode.Cache")
    public static HashTable getCache() {
        return getWidgetNodeInterface().getCache(null);
    }

    @HookName("WidgetNode.Uid")
    public int getUid() {
        return getWidgetNodeInterface().getUid(this.instance);
    }
}
