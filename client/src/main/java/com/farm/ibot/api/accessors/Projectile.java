package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IProjectile;
import com.farm.ibot.api.interfaces.Animable;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Projectile extends Node implements Animable {
    public Projectile(Object instance) {
        super(instance);
    }

    public static IProjectile getProjectileInterface() {
        return Bot.get().accessorInterface.projectileInterface;
    }

    @HookName("Projectile.ProjectileList")
    public static LinkedList getProjectileList() {
        return getProjectileInterface().getProjectileList(null);
    }

    @HookName("Projectile.X")
    private double hookGetAnimableX() {
        return getProjectileInterface().hookGetAnimableX(this.instance);
    }

    @HookName("Projectile.Y")
    private double hookGetAnimableY() {
        return getProjectileInterface().hookGetAnimableY(this.instance);
    }

    @HookName("Projectile.ModelHeight")
    private double hookGetModelHeight() {
        return getProjectileInterface().hookGetModelHeight(this.instance);
    }

    @HookName("Projectile.id")
    public int getId() {
        return getProjectileInterface().getId(this.instance);
    }

    public int getAnimableX() {
        return (int) this.hookGetAnimableX();
    }

    public int getAnimableY() {
        return (int) this.hookGetAnimableY();
    }

    public int getModelHeight() {
        return (int) this.hookGetModelHeight();
    }

    public Character getTargetEntity() {
        return null;
    }
}
