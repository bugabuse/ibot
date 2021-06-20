package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.Iterable;

public interface ICharacter {
    Iterable getHealthBarIterable(Object var1);

    int getFrame1(Object var1);

    int getFrame2(Object var1);

    int getInteractingIndex(Object var1);

    int getWalkingQueueSize(Object var1);

    int[] getWalkingQueueX(Object var1);

    int[] getWalkingQueueY(Object var1);

    int[] getHitCycles(Object var1);

    int getAnimableX(Object var1);

    int getAnimableY(Object var1);

    int getModelHeight(Object var1);

    int getAnimation(Object var1);

    int getOrientation(Object var1);

    int getNpcCycle(Object var1);
}
