package com.farm.ibot.api.methods.entities;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.util.Sorting;
import com.farm.ibot.api.wrapper.Tile;

import java.util.ArrayList;

public class Npcs {
    public static EntitySearcher<Npc> best = new EntitySearcher<Npc>() {
        public Npc get(int id, Tile nearestTo) {
            return (Npc) Sorting.getBest(nearestTo, Npcs.getAll((f) -> {
                return f.getId() == id;
            }));
        }

        public Npc get(int id) {
            return this.get(id, Player.getLocal().getPosition());
        }

        public Npc get(String name, Tile nearestTo) {
            return (Npc) Sorting.getBest(nearestTo, Npcs.getAll((f) -> {
                return f.getName().equalsIgnoreCase(name);
            }));
        }

        public Npc get(String name) {
            return this.get(name, Player.getLocal().getPosition());
        }

        public Npc get(Filter<Npc> filter) {
            return (Npc) Sorting.getBest(Npcs.getAll(filter));
        }
    };

    public static ArrayList<Npc> getAll() {
        return getAll((p) -> {
            return true;
        });
    }

    public static Npc get(String name) {
        return get((n) -> {
            return n.getName().equalsIgnoreCase(name);
        });
    }

    public static Npc get(Filter<Npc> filter) {
        return (Npc) Sorting.getNearest(getAll(filter));
    }

    public static ArrayList<Npc> getAll(Filter<Npc> filter) {
        ArrayList<Npc> temp = new ArrayList();
        Npc[] var2 = Npc.getLocalNpcs();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Npc npc = var2[var4];
            if (npc != null && Client.getGameCycle() - npc.getNpcCycle() < 200 && filter.accept(npc)) {
                temp.add(npc);
            }
        }

        return temp;
    }
}
