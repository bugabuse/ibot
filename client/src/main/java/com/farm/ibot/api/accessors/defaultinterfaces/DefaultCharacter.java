package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Iterable;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.ICharacter;

public class DefaultCharacter implements ICharacter {
    public Iterable getHealthBarIterable(Object instance) {
        return (Iterable) Wrapper.get("Character.HealthBars", Iterable.class, instance);
    }

    public int getFrame1(Object instance) {
        return (Integer) Wrapper.get("Character.Frame1", instance);
    }

    public int getFrame2(Object instance) {
        return (Integer) Wrapper.get("Character.Frame2", instance);
    }

    public int getInteractingIndex(Object instance) {
        return (Integer) Wrapper.get("Character.InteractingIndex", instance);
    }

    public int getWalkingQueueSize(Object instance) {
        return (Integer) Wrapper.get("Character.WalkingQueueSize", instance);
    }

    public int[] getWalkingQueueX(Object instance) {
        return (int[]) Wrapper.get("Character.WalkingQueueX", instance);
    }

    public int[] getWalkingQueueY(Object instance) {
        return (int[]) Wrapper.get("Character.WalkingQueueY", instance);
    }

    public int[] getHitCycles(Object instance) {
        return (int[]) Wrapper.get("Character.HitCycles", instance);
    }

    public int getAnimableX(Object instance) {
        return (Integer) Wrapper.get("Character.AnimableX", instance);
    }

    public int getAnimableY(Object instance) {
        return (Integer) Wrapper.get("Character.AnimableY", instance);
    }

    public int getModelHeight(Object instance) {
        return (Integer) Wrapper.get("Renderable.ModelHeight", instance);
    }

    public int getAnimation(Object instance) {
        return (Integer) Wrapper.get("Character.Animation", instance);
    }

    public int getOrientation(Object instance) {
        return (Integer) Wrapper.get("Character.Orientation", instance);
    }

    public int getNpcCycle(Object instance) {
        return (Integer) Wrapper.get("Character.npcCycle", instance);
    }
}
