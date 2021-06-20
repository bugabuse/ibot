package com.farm.scripts.fletcher.api;

import com.farm.scripts.fisher.util.RequiredItem;

public class RequiredItemContainer {
    private RequiredItem[] items;
    private String name;

    public RequiredItemContainer(String name, RequiredItem... items) {
        this.items = items;
        this.name = name;
    }

    public RequiredItem[] getItems() {
        return this.items;
    }

    public String getName() {
        return this.name;
    }
}
