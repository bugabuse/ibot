package com.farm.scripts.runecrafter.strategies.slave;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Trade;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.runecrafter.Constants;

public class DeliverStrategy extends Strategy {
    public boolean active() {
        return Trade.isOpen() || Inventory.getCount(7936) >= 20;
    }

    protected void onAction() {
        if (Equipment.isEquipped(5527) || Inventory.contains(1438)) {
            this.enterRuins();
            if (WebWalking.walkTo(Constants.ALTAR_AIR, new Tile[0])) {
                Player crafter = Players.get(Constants.CRAFTER_NAME.toString());
                if (!Trade.isOpen()) {
                    if (crafter != null) {
                        crafter.interact("Trade");
                        Time.sleep(8000, Trade::isOpen);
                        return;
                    }
                } else if (!Trade.getOponentName().equalsIgnoreCase(Constants.CRAFTER_NAME.toString())) {
                    Trade.decline();
                } else if (Trade.getOpenedScreen() == 1 && Inventory.getCount(7936) > 0 && !Trade.getMyItems().contains(7936, 20)) {
                    Trade.add(7936, MathUtils.clamp(27, 0, Inventory.getCount(7936)));
                } else {
                    Trade.accept();
                }

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
