package com.farm.scripts.woodcutter.strategy.main;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.*;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.woodcutter.ChopSettings;
import com.farm.scripts.woodcutter.Strategies;
import com.farm.scripts.woodcutter.util.Tree;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ChopStrategy extends Strategy {
    GameObject tree;
    ChopStrategyOak oaks = new ChopStrategyOak();

    public boolean active() {
        return !Inventory.isFull() && Strategies.BANK_STRATEGY.hasAxe() && !Strategies.BANK_STRATEGY.active();
    }

    public void onAction() {
        if (!ChopSettings.powerChopping && ChopSettings.getTreeToChop().equals(ChopSettings.TREE_OAK)) {
            this.oaks.onAction();
        } else {
            this.sleep(300, 800);
            if (Random.next(0, 30) <= 1) {
                this.sleep(1000, 6000);
            }

            if (!ChopSettings.getSpot().contains(Player.getLocal().getPosition())) {
                WebWalking.walkTo(ChopSettings.getSpot().centerTile(), 5, new Tile[0]);
            } else if (Player.getLocal().getAnimation() == -1 || this.tree == null || !this.tree.exists()) {
                this.tree = this.getBestTree();
                if (this.tree != null) {
                    Walking.setRun(true);
                    this.tree.interact("Chop down");
                    Time.sleep(1000, 1200);
                    Time.sleep(() -> {
                        return Player.getLocal().getAnimation() != -1 || !this.tree.exists();
                    });
                } else {
                    System.out.println("tree not found");
                    Walking.setRun(false);
                    if (StumpListenerStrategy.stumps.size() > 0 && StumpListenerStrategy.stumps.size() >= ChopSettings.getSpot().treesCount) {
                        Tile tile = ((GameObject) StumpListenerStrategy.stumps.get(0)).getNearestFrontTile();
                        if (tile != null) {
                            WebWalking.walkTo(tile, 0, new Tile[0]);
                        }
                    } else {
                        WebWalking.walkTo(ChopSettings.getSpot().centerTile(), 5, new Tile[0]);
                    }
                }

            }
        }
    }

    public GameObject getBestTree() {
        ArrayList<PriorityComparator> list = new ArrayList();
        Tree treeToChop = ChopSettings.getTreeToChop();
        System.out.println(Arrays.toString(treeToChop.ids));
        Iterator var3 = GameObjects.getAll((f) -> {
            return (f.getName().equalsIgnoreCase(treeToChop.name) || treeToChop.ids != null && Ints.contains(treeToChop.ids, f.getId())) && ChopSettings.getSpot().contains(f.getPosition()) && f.isReachable();
        }).iterator();

        while (var3.hasNext()) {
            GameObject obj = (GameObject) var3.next();
            PriorityComparator comp = new PriorityComparator(obj);
            comp.addPoints(obj.getPosition().distance());
            list.add(comp);
        }

        ArrayList<GameObject> newList = Sorting.sortReversed(list);
        return newList.size() > 0 ? (GameObject) newList.get(MathUtils.clamp(Random.next(0, newList.size() * 2), newList.size() - 1)) : null;
    }
}
