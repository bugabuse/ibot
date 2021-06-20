package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.LinkedList;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IProjectile;

public class DefaultProjectile implements IProjectile {
    public LinkedList getProjectileList(Object instance) {
        return (LinkedList) Wrapper.getStatic("Projectile.ProjectileList", LinkedList.class);
    }

    public double hookGetAnimableX(Object instance) {
        return (Double) Wrapper.get("Projectile.X", instance);
    }

    public double hookGetAnimableY(Object instance) {
        return (Double) Wrapper.get("Projectile.Y", instance);
    }

    public double hookGetModelHeight(Object instance) {
        return (Double) Wrapper.get("Projectile.ModelHeight", instance);
    }

    public int getId(Object instance) {
        return (Integer) Wrapper.get("Projectile.id", instance);
    }
}
