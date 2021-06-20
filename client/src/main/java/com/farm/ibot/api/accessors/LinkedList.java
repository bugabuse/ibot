package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.ILinkedList;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class LinkedList extends Wrapper {
    public LinkedList(Object instance) {
        super(instance);
    }

    public static ILinkedList getLinkedListInterface() {
        return Bot.get().accessorInterface.linkedListInterface;
    }

    @HookName("LinkedList.Current")
    public Node getCurrent() {
        return getLinkedListInterface().getCurrent(this.instance);
    }

    @HookName("LinkedList.Tail")
    public Node getTail() {
        return getLinkedListInterface().getTail(this.instance);
    }
}
