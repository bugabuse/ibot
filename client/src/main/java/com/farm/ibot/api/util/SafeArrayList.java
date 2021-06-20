package com.farm.ibot.api.util;

import java.util.ArrayList;

public class SafeArrayList<T> extends ArrayList<T> {
    public T get(int index) {
        int size = this.size();
        return size > index && index < size ? super.get(index) : null;
    }
}
