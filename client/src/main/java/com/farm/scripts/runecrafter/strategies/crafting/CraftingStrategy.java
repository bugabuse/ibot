package com.farm.scripts.runecrafter.strategies.crafting;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.runecrafter.Constants;

public class CraftingStrategy extends Strategy {
    public boolean active() {
        return Inventory.getCount(7936) > 0 && (Equipment.isEquipped(5527) || Inventory.contains(1438));
    }

    protected void onAction() {
        System.out.println("Walk");
        this.enterRuins();
        if (WebWalking.walkTo(Constants.ALTAR_AIR, 8, new Tile[0])) {
            GameObject altar = GameObjects.get("Altar");
            if (altar != null && altar.interact("Craft-rune")) {
                Time.sleep(() -> {
                    return Player.getLocal().getAnimation() != -1;
                });
                Time.sleep(() -> {
                    return Player.getLocal().getAnimation() == -1;
                });
            }

        }
    }

    private void enterRuins() {
        GameObject mysterious_ruins = GameObjects.get(34813);
        if (mysterious_ruins != null && mysterious_ruins.getPosition().distance() <= 8) {
            System.out.println("ruins click");
            if (Inventory.contains(1438)) {
                Inventory.get(1438).interactWith(mysterious_ruins);
                Time.sleep(() -> {
                    return GameObjects.get(34813) == null;
                });
            }
        }

    }
}
