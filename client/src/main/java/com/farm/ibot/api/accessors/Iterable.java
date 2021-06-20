package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IIterable;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Iterable extends Wrapper {
    public Iterable(Object instance) {
        super(instance);
    }

    public static IIterable getIterableInterface() {
        return Bot.get().accessorInterface.iterableInterface;
    }

    @HookName("Iterable.node")
    public Node getNode() {
        return getIterableInterface().getNode(this.instance);
    }
}
