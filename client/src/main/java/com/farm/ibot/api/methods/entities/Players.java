package com.farm.ibot.api.methods.entities;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.util.Sorting;

import java.util.ArrayList;

public class Players {
    public static ArrayList<Player> getAll() {
        return getAll((p) -> {
            return true;
        });
    }

    public static Player get(String name) {
        return get((p) -> {
            return ("" + name).equalsIgnoreCase(p.getName());
        });
    }

    public static Player get(Filter<Player> filter) {
        return (Player) Sorting.getNearest(getAll(filter));
    }

    public static ArrayList<Player> getAll(Filter<Player> filter) {
        ArrayList<Player> temp = new ArrayList();
        Player[] var2 = Player.getPlayers();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Player player = var2[var4];
            if (player != null && !player.equals(Player.getLocal()) && filter.accept(player)) {
                temp.add(player);
            }
        }

        return temp;
    }

    public static Player forIndex(int index) {
        Player[] players = Player.getPlayers();
        return index > -1 && players.length > index ? players[index] : null;
    }
}
