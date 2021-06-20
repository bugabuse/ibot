package com.farm.ibot.api.methods.entities;

import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.wrapper.Tile;

public interface EntitySearcher<T> {
    T get(int var1, Tile var2);

    T get(int var1);

    T get(String var1, Tile var2);

    T get(String var1);

    T get(Filter<T> var1);
}
