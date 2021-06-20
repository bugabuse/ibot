package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IHashTable;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class HashTable extends Wrapper {
    public HashTable(Object instance) {
        super(instance);
    }

    public static IHashTable getHashTableInterface() {
        return Bot.get().accessorInterface.hashTableInterface;
    }

    @HookName("HashTable.Size")
    public int getSize() {
        return getHashTableInterface().getSize(this.instance);
    }
}
