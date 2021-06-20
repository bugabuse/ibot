package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Node;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IIterable;

public class DefaultIterable implements IIterable {
    public Node getNode(Object instance) {
        return (Node) Wrapper.get("Iterable.node", Node.class, instance);
    }
}
