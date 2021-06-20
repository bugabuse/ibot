package com.farm.scripts.claysoftener.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.claysoftener.Constants;

public class BankStrategy extends Strategy {
    public boolean active() {
        return Constants.currentState == 0 && !SoftenClayStrategy.isSoftening() && (Inventory.container().getCount(new int[]{1925, 1929}) < 14 || Inventory.get(1925) == null && Inventory.get(434) == null || Locations.getClosestBank().distance() <= Constants.TILE_FOUNTAIN.distance() && Inventory.get(434) == null);
    }

    public void onAction() {
        Walking.setRun(true);
        if (WebWalking.walkTo(Locations.GRAND_EXCHANGE, 6, new Tile[0])) {
            if (!Bank.open()) {
                return;
            }

            if (!Bank.depositAllExcept(new int[]{1929, 1925, 434})) {
                return;
            }

            if (Inventory.container().getCount(new int[]{1929, 1925}) > 14 || Inventory.container().getCount(new int[]{434}) > 14) {
                Bank.depositAll();
                return;
            }

            if (14 - Inventory.container().getCount(new int[]{1925, 1929}) > 0) {
                Bank.withdraw(1925, 14 - Inventory.container().getCount(new int[]{1925, 1929}));
                Time.sleep(3000);
                Bank.withdraw(1929, 14 - Inventory.container().getCount(new int[]{1925, 1929}));
            }

            Bank.withdraw(434, 14 - Inventory.container().getCount(new int[]{434}));
        }

    }
}
