package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.LinkedList;

public interface IProjectile {
    LinkedList getProjectileList(Object var1);

    double hookGetAnimableX(Object var1);

    double hookGetAnimableY(Object var1);

    double hookGetModelHeight(Object var1);

    int getId(Object var1);
}
