package com.farm.scripts.farmtrainer.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.pathfinding.impl.WebPathFinder;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmtrainer.FarmingTrainer;
import com.farm.scripts.farmtrainer.Strategies;

import java.util.Iterator;

public class FillWateringCan extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {

        if (!Inventory.container().contains(5332) && !Inventory.container().contains(5331) && !Bank.getCache().contains(5331)) {

            FarmingTrainer.get().setCurrentlyExecutitng(Strategies.DEFAULT);
        } else if (Locations.GRAND_EXCHANGE.distance() > 50 && (new WebPathFinder()).findPath(Player.getLocal().getPosition(), Locations.GRAND_EXCHANGE).size() == 0) {
            if (Inventory.get("Varrock teleport") == null) {

                Bank.openAndWithdraw(new Item[]{new Item(ItemDefinition.forName("Varrock teleport").id, 1)});
            } else {
                Inventory.get("Varrock teleport").interact("Break");
                Time.waitRegionChange();
            }

        } else if (!Inventory.container().contains(5331)) {
            if (Inventory.container().countNoted().contains(5331)) {
                if (Bank.openNearest()) {
                    Bank.depositAll();
                }

            } else {
                Bank.openAndWithdraw(new Item[]{new Item(5331, 20)});
            }
        } else if (Walking.walkTo(new Tile(3192, 3472, 0))) {
            Iterator var1 = Inventory.getAll("Watering can").iterator();

            while (var1.hasNext()) {
                Item item = (Item) var1.next();
                item.interactWith(GameObjects.get("Fountain"));
                Time.sleep(600, 800);
            }

        }
    }
}
