package com.farm.scripts.woodcutter.strategy.main;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.woodcutter.ChopSettings;
import com.google.common.primitives.Ints;

import java.util.ArrayList;

public class ChopStrategyOak extends Strategy {
    private GameObject tree;
    private Tile treeTile;
    private Tile treeTileStayable;

    public boolean active() {
        return true;
    }

    public void onAction() {
        if (!ChopSettings.getSpot().contains(Player.getLocal().getPosition())) {
            WebWalking.walkTo(ChopSettings.getSpot().centerTile(), 5, new Tile[0]);
        } else if (Player.getLocal().getAnimation() == -1 || this.tree == null || !this.tree.exists()) {
            this.tree = this.getBestTree();
            if (this.tree != null) {
                if (this.treeTileStayable == null) {

                    this.treeTileStayable = this.tree.getNearestFrontTile();
                }

                Walking.setRun(true);
                this.tree.interact("Chop down");
                Time.sleep(1000, 1200);
                Time.sleep(() -> {
                    return Player.getLocal().getAnimation() != -1 || !this.tree.exists();
                });
            } else {
                Walking.setRun(false);
                if (this.treeTileStayable != null) {
                    WebWalking.walkTo(this.treeTileStayable, 2, new Tile[0]);
                } else {
                    WebWalking.walkTo(ChopSettings.getSpot().centerTile(), 5, new Tile[0]);
                }
            }

        }
    }

    public GameObject getBestTree() {
        if (this.treeTile == null) {
            ArrayList<GameObject> list = GameObjects.getAll((object) -> {
                return (object.getName().equalsIgnoreCase(ChopSettings.getTreeToChop().name) || Ints.contains(ChopSettings.getTreeToChop().stumpIds, object.getId())) && ChopSettings.getSpot().contains(object.getPosition()) && object.isReachable();
            });
            this.treeTile = list.size() > 0 ? ((GameObject) list.get(Random.next(0, list.size()))).getPosition() : null;
        }

        return this.treeTile != null ? GameObjects.get((object) -> {
            return object.getName().equalsIgnoreCase(ChopSettings.getTreeToChop().name) && ChopSettings.getSpot().contains(object.getPosition()) && object.getPosition().distance(this.treeTile) <= 1 && object.isReachable();
        }) : null;
    }
}
