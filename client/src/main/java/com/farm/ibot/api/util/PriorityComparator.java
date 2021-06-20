package com.farm.ibot.api.util;

public class PriorityComparator {
    public Object object;
    public int priorityPoints;

    public PriorityComparator(Object object) {
        this(object, 0);
    }

    public PriorityComparator(Object object, int priorityPoints) {
        this.priorityPoints = 0;
        this.object = object;
        this.priorityPoints = priorityPoints;
    }

    public void addPoints(int points) {
        this.priorityPoints += points;
    }
}
