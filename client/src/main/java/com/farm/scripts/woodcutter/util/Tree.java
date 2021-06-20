package com.farm.scripts.woodcutter.util;

public class Tree {
    public int[] stumpIds;
    public int[] ids;
    public String name;

    public Tree(String name, int... stumpIds) {
        this.name = name;
        this.stumpIds = stumpIds;
    }

    public Tree ids(int... ids) {
        this.ids = ids;
        return this;
    }
}
