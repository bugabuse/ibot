package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.MouseTracker;

public interface IMouseTracker {
    int[] getXCoordinates(Object var1);

    int[] getYCoordinates(Object var1);

    long[] getTimes(Object var1);

    int getCurrentIndex(Object var1);

    MouseTracker get();
}
