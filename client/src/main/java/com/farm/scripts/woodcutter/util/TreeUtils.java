package com.farm.scripts.woodcutter.util;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.scripts.woodcutter.ChopSettings;

import java.util.ArrayList;
import java.util.List;

public class TreeUtils {
    public static List<Player> getPlayersChopping(GameObject tree) {
        return tree != null ? Players.getAll((p) -> {
            return !p.equals(Player.getLocal()) && p.getPosition().distance(tree.getPosition()) <= 2;
        }) : new ArrayList();
    }

    public static List<Player> getPlayersChoppingNearest() {
        List<Player> players = new ArrayList();
        if (ChopSettings.getTreeToChop() != null) {
            GameObjects.getAll(ChopSettings.getTreeToChop().name).forEach((o) -> {
                players.addAll(getPlayersChopping(o));
            });
        }

        GameObjects.getAll((o) -> {
            return o.getName().toLowerCase().contains("stump");
        }).forEach((o) -> {
            players.addAll(getPlayersChopping(o));
        });
        return players;
    }
}
