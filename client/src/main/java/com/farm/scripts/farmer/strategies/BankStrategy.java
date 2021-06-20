package com.farm.scripts.farmer.strategies;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmer.DailyFarming;
import com.farm.scripts.farmer.Strategies;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class BankStrategy extends Strategy {
    public static Item[] requiredItems;

    static {
        requiredItems = new Item[]{new Item(8010, 30), new Item(8009, 30), new Item(Strategies.herbToFarm.seedId, 200), new Item(5341, 1), new Item(952, 1), new Item(5343, 1), new Item(6036, 2), new Item(21483, 10)};
    }

    int[] junk = new int[]{229, 1925, 6055};

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Iterator var1 = Inventory.container().getAll((i) -> {
            return Ints.contains(this.junk, i.getId());
        }).iterator();

        while (var1.hasNext()) {
            Item item = (Item) var1.next();
            item.interact("Drop");
            Time.sleep(300, 600);
        }

        ArrayList<Item> required = new ArrayList(Arrays.asList(requiredItems));
        required.removeIf((i) -> {
            return i.getId() == 21483 && MaintainPatch.usedCompost;
        });
        if (!Inventory.container().containsAllOne((Item[]) required.toArray(new Item[0]))) {
            if (!this.goToBank()) {

                DailyFarming.get().getScriptHandler().loginRandom.active = true;
                ScriptUtils.interruptCurrentLoop();
                return;
            }

            Bank.depositAllExcept(requiredItems);
            Item[] var7 = requiredItems;
            int var3 = var7.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Item item = var7[var4];
                if (!Bank.getContainer().contains(item.getId()) && !Inventory.container().countNoted().contains(item.getId())) {
                    Debug.log("Missing " + item.getId());

                    DailyFarming.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                    return;
                }

                if (Inventory.getCount(item.getId()) > item.getAmount()) {
                    Bank.depositAll();
                    return;
                }

                Bank.withdraw(item.getId(), item.getAmount() - Inventory.getCount(item.getId()));
                Time.sleep(300, 600);
            }

            ScriptUtils.interruptCurrentLoop();
        }

    }

    private boolean goToBank() {
        ScriptUtils.interruptCurrentLoop();
        if (!WebWalking.canFindPath(Locations.getClosestBank())) {
            if (Inventory.container().contains((i) -> {
                return i.getName().contains("Ring of dueling");
            })) {
                Widgets.closeTopInterface();
                Inventory.container().get((i) -> {
                    return i.getName().contains("Ring of dueling");
                }).interact("Rub");
                if (Time.sleep(() -> {
                    return Widgets.get((w) -> {
                        return w.getText().contains("Castle Wars Arena.");
                    }) != null;
                })) {
                    Dialogue.selectOptionThatContains("Castle Wars Arena.");
                }

                return false;
            } else {
                Magic.LUMBRIDGE_HOME_TELEPORT.select();
                Time.sleep(15000);
                return false;
            }
        } else {
            return Bank.openNearest();
        }
    }
}
