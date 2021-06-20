package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Node;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.ILinkedList;

public class DefaultLinkedList implements ILinkedList {
    public Node getCurrent(Object instance) {
        return (Node) Wrapper.get("LinkedList.Current", Node.class, instance);
    }

    public Node getTail(Object instance) {
        return (Node) Wrapper.get("LinkedList.Tail", Node.class, instance);
    }
}
