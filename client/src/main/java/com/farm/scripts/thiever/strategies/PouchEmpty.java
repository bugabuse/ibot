package com.farm.scripts.thiever.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.google.common.primitives.Ints;

import java.util.Iterator;

public class PouchEmpty extends Strategy {
    private int[] notToDrop = new int[]{995, 333, 1993, 5295, 5300, 5304};
    private String[] notToDropStrings = new String[]{"Coin pouch"};

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Inventory.container().getCount(new String[]{"Coin pouch"}) >= 28) {
            Inventory.get("Coin pouch").interact(ItemMethod.EAT);
        } else {
            if (Player.getLocal().getModelHeight() == 1000) {
                int dropped = 0;
                Iterator var2 = Inventory.container().getAll((i) -> {
                    return !Ints.contains(this.notToDrop, i.getId()) && !StringUtils.containsAny(i.getName(), this.notToDropStrings);
                }).iterator();

                while (var2.hasNext()) {
                    Item item = (Item) var2.next();
                    if (item.getId() == 22521) {
                        item.interact(ItemMethod.EAT);
                    } else {
                        ++dropped;
                        item.interact("Drop");
                    }

                    if (dropped > 3) {
                        break;
                    }

                    Time.sleep(10, 120);
                }
            }

            if (Inventory.isFull()) {
                for (Iterator var4 = Inventory.container().getAll((i) -> {
                    return !Ints.contains(this.notToDrop, i.getId()) && !StringUtils.containsAny(i.getName(), this.notToDropStrings);
                }).iterator(); var4.hasNext(); Time.sleep(10, 120)) {
                    Item item = (Item) var4.next();
                    if (item.getId() == 22521) {
                        item.interact(ItemMethod.EAT);
                    } else {
                        item.interact("Drop");
                    }
                }
            }

            this.sleep(2000);
        }
    }
}
