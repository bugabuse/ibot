package com.farm.scripts.fisher.strategies;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fisher.FishSettings;

public class FishStrategy extends Strategy {
    Npc fishingSpot;

    public boolean active() {
        return !Inventory.isFull() && BankStrategy.hasFishingEquipment();
    }

    public void onAction() {
        if (WebWalking.walkTo(FishSettings.getConfig().getSpotLocation(), FishSettings.getConfig().getFishingDistance(), new Tile[0])) {
            if (this.fishingSpot == null || !this.fishingSpot.exists() || Player.getLocal().getAnimation() == -1) {
                this.fishingSpot = this.getBestSpot();
                if (this.fishingSpot != null) {
                    this.fishingSpot.interact(FishSettings.getConfig().getFishingMethod());
                    Time.sleep(() -> {
                        return Player.getLocal().getAnimation() != -1 || !this.fishingSpot.exists();
                    });
                } else {
                    WebWalking.walkTo(FishSettings.getConfig().getSpotLocation(), 5, new Tile[0]);
                }

            }
        }
    }

    public Npc getBestSpot() {
        return Npcs.get((n) -> {
            return this.isReachable(n) && n.getDefinition().containsAction(FishSettings.getConfig().getFishingMethod()) && n.getName().contains("Fishing spot");
        });
    }

    private boolean isReachable(Npc npc) {
        return npc.getPosition().add(0, -1).isReachable() || npc.getPosition().add(-1, 0).isReachable() || npc.getPosition().add(0, 1).isReachable() || npc.getPosition().add(1, 0).isReachable();
    }
}
