package com.farm.scripts.farmtrainer.strategies;

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
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmtrainer.FarmingTrainer;
import com.farm.scripts.farmtrainer.Strategies;

public class GrandExchangeStrategy extends Strategy {
    public static Item[] itemsToBuy = new Item[]{new Item(5331, 40), new Item(8013, 100), new Item(2552, 10), new Item(8007, 30)};

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Walking.setRun(true);
        if (this.goToGe()) {
            if (GrandExchange.open()) {
                if (Inventory.getFreeSlots() < 2) {
                    if (Bank.openNearest()) {
                        Bank.depositAllExcept(new int[]{995});
                    }

                } else if (GrandExchange.collect()) {
                    Item[] var1 = itemsToBuy;
                    int var2 = var1.length;

                    for (int var3 = 0; var3 < var2; ++var3) {
                        Item item = var1[var3];
                        int ourAmount = Inventory.container().countNoted().getCount(new int[]{item.getId()}) + Bank.getCache().getCount(new int[]{item.getId()});
                        if (item.getName().contains("Watering can")) {
                            ourAmount = Inventory.container().countNoted().getCount((i) -> {
                                return i.getName().contains("Watering can");
                            }) + Bank.getCache().getCount((i) -> {
                                return i.getName().contains("Watering can");
                            });
                        }

                        int amountNeeded = item.getAmount() - ourAmount;
                        if (amountNeeded >= 1) {
                            int price = (int) ((double) OsbuddyExchange.forId(item.getId()).overallAverage * 1.2D);
                            if (amountNeeded < 2 && price < 1000) {
                                price += 3000;
                            }

                            if (item.getName().contains("Bagged plant")) {
                                price += 2500;
                            }

                            if (item.getName().contains("Watering can")) {
                                price += 600;
                            }

                            int coinsNeeded = amountNeeded * price;
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


                    FarmingTrainer.get().setCurrentlyExecutitng(Strategies.DEFAULT);
                }
            }
        }
    }

    private boolean goToGe() {
        if (!WebWalking.canFindPath(Locations.GRAND_EXCHANGE)) {
            if (Inventory.container().contains("Varrock teleport")) {
                Inventory.container().get("Varrock teleport").interact("Break");
                Time.waitRegionChange();
            } else {
                if (!Inventory.container().contains("Falador teleport")) {

                    Magic.LUMBRIDGE_HOME_TELEPORT.select();
                    Time.waitRegionChange();
                    return false;
                }

                Inventory.container().get("Falador teleport").interact("Break");
                Time.waitRegionChange();
            }

            return false;
        } else {
            return WebWalking.walkTo(Locations.GRAND_EXCHANGE, new Tile[0]);
        }
    }
}
