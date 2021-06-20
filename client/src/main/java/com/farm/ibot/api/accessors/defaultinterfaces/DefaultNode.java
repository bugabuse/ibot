package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Node;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.INode;

public class DefaultNode implements INode {
    public long getUid(Object instance) {
        return (Long) Wrapper.get("Node.Uid", instance);
    }

    public Node getNext(Object instance) {
        return (Node) Wrapper.get("Node.Next", Node.class, instance);
    }

    public Node getPrevious(Object instance) {
        return (Node) Wrapper.get("Node.Previous", Node.class, instance);
    }
}
