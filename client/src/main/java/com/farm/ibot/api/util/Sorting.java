// Decompiled with: Procyon 0.5.36
package com.farm.ibot.api.util;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.Animable;
import com.farm.ibot.api.wrapper.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sorting {
    public static Tile getNearest(final Tile tile, final Tile... list) {
        int lastDistance = -1;
        Tile temp = null;
        for (final Tile t : list) {
            final int distance = t.distance(tile);
            if (distance <= lastDistance || lastDistance == -1) {
                lastDistance = distance;
                temp = t;
            }
        }
        return temp;
    }

    public static <T> T getNearest(final Tile tile, final T... list) {
        int lastDistance = -1;
        Animable temp = null;
        for (final T anim : list) {
            final Animable animable = (Animable) anim;
            final int distance = animable.getAnimablePosition().animableDistance(tile);
            if (distance <= lastDistance || lastDistance == -1) {
                lastDistance = distance;
                temp = animable;
            }
        }
        return (T) temp;
    }

    public static <T> T getNearest(Tile tile, final ArrayList<T> list) {
        int lastDistance = -1;
        Animable temp = null;
        tile = tile.toAnimable();
        for (final T anim : list) {
            final Animable animable = (Animable) anim;
            final int distance = animable.getAnimablePosition().distance(tile);
            if (distance <= lastDistance || lastDistance == -1) {
                lastDistance = distance;
                temp = animable;
            }
        }
        return (T) temp;
    }

    public static <T> T getNearest(final ArrayList<T> list) {
        return getNearest(Player.getLocal().getAnimablePosition(), list);
    }

    public static <T> ArrayList<T> sortReversed(final List<PriorityComparator> list) {
        Collections.sort(list, (a, b) -> b.priorityPoints - a.priorityPoints);
        final ArrayList<T> newList = new ArrayList<T>();
        for (final PriorityComparator element : list) {
            newList.add((T) element.object);
        }
        return newList;
    }

    public static <T> ArrayList<T> sort(final List<PriorityComparator> list) {
        Collections.sort(list, (a, b) -> a.priorityPoints - b.priorityPoints);
        final ArrayList<T> newList = new ArrayList<T>();
        for (final PriorityComparator element : list) {
            newList.add((T) element.object);
        }
        return newList;
    }

    public static <T> T getBest(final ArrayList<T> list) {
        return getBest(Player.getLocal().getPosition(), list);
    }

    public static <T> T getBest(Tile tile, final ArrayList<T> list) {
        int lastDistance = -1;
        Animable temp = null;
        tile = tile.toAnimable();
        for (final T anim : list) {
            final Animable animable = (Animable) anim;
            final int distance = tile.realDistance(animable.getPosition(), true) + tile.distance(animable.getPosition());
            if (distance <= lastDistance || lastDistance == -1) {
                lastDistance = distance;
                temp = animable;
            }
        }
        return (T) temp;
    }
}
