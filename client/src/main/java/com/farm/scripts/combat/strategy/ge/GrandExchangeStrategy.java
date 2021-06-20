package com.farm.scripts.combat.strategy.ge;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer.Status;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.combat.Combat;
import com.farm.scripts.combat.EquipmentItem;
import com.farm.scripts.combat.Settings;
import com.farm.scripts.combat.Strategies;

import java.util.ArrayList;
import java.util.Arrays;

public class GrandExchangeStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        Walking.setRun(true);
        ArrayList<Item> itemList = new ArrayList();
        itemList.add(new Item(Settings.FOOD_ID, 300));
        if (Settings.usingMagic()) {
            itemList.addAll(Arrays.asList(Settings.RUNES));
        }

        EquipmentItem[] var2 = Settings.getEquipmentItems();
        int var3 = var2.length;

        int var4;
        for (var4 = 0; var4 < var3; ++var4) {
            EquipmentItem item = var2[var4];
            if (!Equipment.isEquipped(item.slot, item.itemName)) {
                itemList.add(new Item(ItemDefinition.forName(item.itemName).getUnnotedId(), 1));
            }
        }

        Item[] itemsToBuy;
        if (Settings.usingMagic()) {
            itemsToBuy = Settings.RUNES;
            var3 = itemsToBuy.length;

            for (var4 = 0; var4 < var3; ++var4) {
                Item rune = itemsToBuy[var4];
                itemList.add(rune);
            }
        }

        itemsToBuy = (Item[]) itemList.toArray(new Item[0]);

        if (this.goToGe()) {
            if (GrandExchange.open()) {
                if (Inventory.getFreeSlots() < 2) {
                    if (Bank.openNearest()) {
                        Bank.depositAllExcept(new int[]{995});
                    }

                } else if (GrandExchange.collect()) {
                    Item[] var13 = itemsToBuy;
                    var4 = itemsToBuy.length;

                    for (int var15 = 0; var15 < var4; ++var15) {
                        Item item = var13[var15];
                        int ourAmount = Inventory.container().countNoted().getCount(new int[]{item.getId()}) + Bank.getCache().getCount(new int[]{item.getId()});
                        int amountNeeded = item.getAmount() - ourAmount;
                        if (amountNeeded >= 1) {
                            int price = (int) ((double) OsbuddyExchange.forId(item.getId()).overallAverage * 2.0D);
                            int coinsNeeded = amountNeeded * price;
                            if (amountNeeded == 1 && price < 2000) {
                                price += 2000;
                            }

                            Debug.log("We need " + coinsNeeded + " coins. " + amountNeeded + " @ " + item.getDefinition().name);
                            if (coinsNeeded > Inventory.container().getCount(new int[]{995})) {
                                if (Bank.openNearest()) {
                                    if (Bank.getContainer().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995}) < coinsNeeded) {

                                        Strategies.muleManager.activateResupplyState();
                                        return;
                                    }

                                    Bank.withdraw(995, 500000);
                                }

                                return;
                            }

                            GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(item.getId(), amountNeeded), price, false);
                            if (buyOffer.exists()) {
                                if (buyOffer.getStatus() == Status.COMPLETED) {
                                    buyOffer.abort();
                                    return;
                                }

                                if (!buyOffer.create()) {
                                    return;
                                }
                            }

                            buyOffer.create();
                            return;
                        }
                    }


                    Combat.get().setCurrentlyExecutitng(Strategies.DEFAULT);
                }
            }
        }
    }

    private boolean goToGe() {
        if (!WebWalking.canFindPath(Locations.GRAND_EXCHANGE)) {

            Magic.LUMBRIDGE_HOME_TELEPORT.select();
            Time.waitRegionChange();
            return false;
        } else {
            return WebWalking.walkTo(Locations.GRAND_EXCHANGE, new Tile[0]);
        }
    }
}
