package com.farm.scripts.miner.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.container.WithdrawContainer;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.miner.MiningSettings;
import com.farm.scripts.miner.MiningUtils;
import com.farm.scripts.miner.Strategies;
import com.google.common.primitives.Ints;

public class MiningStrategy extends Strategy {
    private GameObject rocks = null;
    private boolean alreadyMining = false;

    public boolean active() {
        return !Inventory.isFull() && MiningUtils.hasPickaxe() && (new WithdrawContainer(Strategies.PRICES, Inventory.getAll())).calculateWealth() <= 6500;
    }

    public void onAction() {
        if (MiningSettings.getSpot().distance() <= 3 || WebWalking.walkTo(MiningSettings.getSpot(), 2, new Tile[0])) {
            if (this.rocks == null || !this.rocks.exists()) {
                Time.sleep(2000, () -> {
                    this.rocks = (GameObject) GameObjects.getAt(MiningSettings.getSpot()).stream().filter((go) -> {
                        return Ints.contains(MiningSettings.rocksIds, go.getId());
                    }).findAny().orElse(null);
                    return this.rocks != null;
                });
                this.alreadyMining = false;
            }

            Debug.log((!this.alreadyMining || Player.getLocal().getAnimation() == -1) + "   " + (this.rocks != null));
            if ((!this.alreadyMining || Player.getLocal().getAnimation() == -1) && this.rocks != null && this.rocks.interact("Mine") && Time.sleep(() -> {
                return Player.getLocal().getAnimation() != -1 || this.rocks == null || !this.rocks.exists();
            })) {
                this.alreadyMining = true;
            }

        }
    }
}
