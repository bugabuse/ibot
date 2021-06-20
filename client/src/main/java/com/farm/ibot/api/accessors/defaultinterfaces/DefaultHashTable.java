package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IHashTable;

public class DefaultHashTable implements IHashTable {
    public int getSize(Object instance) {
        return (Integer) Wrapper.get("HashTable.Size", instance);
    }
}
