package com.farm.ibot.api.methods.entities;

import com.farm.ibot.api.accessors.LinkedList;
import com.farm.ibot.api.accessors.Node;
import com.farm.ibot.api.accessors.Projectile;
import com.farm.ibot.api.interfaces.Filter;

import java.util.ArrayList;

public class Projectiles {
    public static Projectile[] getAll(Filter<Projectile> filter) {
        ArrayList<Projectile> temp = new ArrayList();
        LinkedList currentList = Projectile.getProjectileList();
        Node tail = currentList.getTail();

        for (Node current = tail.getNext(); current != null && !current.equals(tail); current = current.getNext()) {
            Projectile item = new Projectile(current.instance);
            if (filter.accept(item)) {
                temp.add(item);
            }
        }

        return (Projectile[]) temp.toArray(new Projectile[temp.size()]);
    }
}
