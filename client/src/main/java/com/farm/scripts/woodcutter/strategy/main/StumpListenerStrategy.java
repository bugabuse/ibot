package com.farm.scripts.woodcutter.strategy.main;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.woodcutter.ChopSettings;
import com.farm.scripts.woodcutter.util.Tree;
import com.google.common.primitives.Ints;

import java.util.ArrayList;

public class StumpListenerStrategy extends Strategy {
    public static ArrayList<GameObject> stumps = new ArrayList();

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Tree tree = ChopSettings.getTreeToChop();
        stumps.removeIf((o) -> {
            return !o.exists();
        });
        stumps.addAll(GameObjects.getAll((o) -> {
            return Ints.contains(tree.stumpIds, o.getId()) && !stumps.contains(o) && ChopSettings.getSpot().contains(o.getPosition());
        }));
        this.sleep(1200);
    }

    public boolean isBackground() {
        return true;
    }
}
