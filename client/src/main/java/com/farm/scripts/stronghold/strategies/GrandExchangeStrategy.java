package com.farm.scripts.stronghold.strategies;

import com.farm.ibot.api.data.Locations;
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
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.stronghold.Strategies;
import com.farm.scripts.stronghold.StrongholdMoney;

public class GrandExchangeStrategy extends Strategy {
    public static Item[] itemsToBuy = new Item[]{new Item(1993, 15)};
    public static Item[] itemsToSell = new Item[]{new Item(590, 1), new Item(303, 1), new Item(1265, 1)};

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Walking.setRun(true);
        if (this.goToGe()) {
            Item[] var1 = itemsToSell;
            int var2 = var1.length;

            int var3;
            Item item;
            for (var3 = 0; var3 < var2; ++var3) {
                item = var1[var3];
                if (Bank.getCache().contains(item.getId())) {
                    Bank.openAndWithdraw(new Item[]{item});
                    return;
                }
            }

            if (GrandExchange.open()) {
                if (Inventory.getFreeSlots() < 15) {
                    if (Bank.openNearest()) {
                        Bank.depositAllExcept(new int[]{995});
                    }

                } else if (GrandExchange.collect()) {
                    var1 = itemsToSell;
                    var2 = var1.length;

                    for (var3 = 0; var3 < var2; ++var3) {
                        item = var1[var3];
                        if (Inventory.container().countNoted().contains(item.getId())) {
                            GrandExchangeOffer sellOffer = new GrandExchangeOffer(new Item(item.getId(), Inventory.container().countNoted().getCount(new int[]{item.getId()})), 1, true);
                            if (sellOffer.exists()) {
                                if (sellOffer.getStatus() == Status.COMPLETED) {
                                    sellOffer.abort();
                                    return;
                                }

                                if (!sellOffer.create()) {
                                    return;
                                }
                            }

                            sellOffer.create();
                            return;
                        }
                    }

                    var1 = itemsToBuy;
                    var2 = var1.length;

                    for (var3 = 0; var3 < var2; ++var3) {
                        item = var1[var3];
                        int ourAmount = Inventory.container().countNoted().getCount(new int[]{item.getId()}) + Bank.getCache().getCount(new int[]{item.getId()});
                        int amountNeeded = item.getAmount() - ourAmount;
                        if (amountNeeded >= 1) {
                            int price = 7;
                            int coinsNeeded = amountNeeded * price;
                            Debug.log("We need " + coinsNeeded + " coins. " + amountNeeded + " @ " + item.getDefinition().name);
                            if (coinsNeeded > Inventory.container().getCount(new int[]{995})) {
                                if (Bank.openNearest()) {
                                    if (Bank.getContainer().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995}) < coinsNeeded) {

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


                    StrongholdMoney.get().setCurrentlyExecutitng(Strategies.DEFAULT);
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
