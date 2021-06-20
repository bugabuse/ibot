/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.util;

public class PriorityComparator {
    public Object object;
    public int priorityPoints = 0;

    public PriorityComparator(Object object) {
        this(object, 0);
    }

    public PriorityComparator(Object object, int priorityPoints) {
        this.object = object;
        this.priorityPoints = priorityPoints;
    }

    public void addPoints(int points) {
        this.priorityPoints += points;
    }
}

