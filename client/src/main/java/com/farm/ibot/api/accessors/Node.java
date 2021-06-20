package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.INode;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Node extends Wrapper {
    public Node(Object instance) {
        super(instance);
    }

    public static INode getNodeInterface() {
        return Bot.get().accessorInterface.nodeInterface;
    }

    @HookName("Node.Uid")
    public long getUid() {
        return getNodeInterface().getUid(this.instance);
    }

    @HookName("Node.Next")
    public Node getNext() {
        return getNodeInterface().getNext(this.instance);
    }

    @HookName("Node.Previous")
    public Node getPrevious() {
        return getNodeInterface().getPrevious(this.instance);
    }
}
