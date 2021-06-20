package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.LinkedList;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IGroundItems;

public class DefaultGroundItems implements IGroundItems {
    public LinkedList[][][] getList(Object instance) {
        return (LinkedList[][][]) Wrapper.getStatic("GroundItems.List", LinkedList[][][].class);
    }
}
