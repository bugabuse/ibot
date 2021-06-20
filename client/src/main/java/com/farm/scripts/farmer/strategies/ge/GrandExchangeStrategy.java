package com.farm.scripts.farmer.strategies.ge;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer.Status;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmer.DailyFarming;
import com.farm.scripts.farmer.Strategies;

public class GrandExchangeStrategy extends Strategy {
    public static Item[] itemsToBuy;

    static {
        itemsToBuy = new Item[]{new Item(8010, 72), new Item(8009, 72), new Item(Strategies.herbToFarm.seedId, 72), new Item(5341, 1), new Item(952, 1), new Item(5343, 1), new Item(6036, 20), new Item(21483, 72)};
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Walking.setRun(true);
        if (!WebWalking.canFindPath(Locations.GRAND_EXCHANGE)) {
            Widgets.closeTopInterface();
            if (this.goToGe()) {
                ;
            }
        } else if (GrandExchange.open()) {
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
                    int amountNeeded = item.getAmount() - ourAmount;
                    if (amountNeeded >= 1) {
                        int price = (int) ((double) OsbuddyExchange.forId(item.getId()).overallAverage * 1.2D);
                        if (amountNeeded < 2 && price < 1000) {
                            price += 3000;
                        }

                        if (item.getName().contains("Plant cure")) {
                            price += 600;
                        }

                        int coinsNeeded = amountNeeded * price;
                        if (coinsNeeded > Inventory.container().getCount(new int[]{995})) {
                            if (Bank.openNearest()) {
                                if (Bank.getContainer().getCount(new int[]{995}) < coinsNeeded) {

                                    if (!this.goToGe()) {
                                        return;
                                    }

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


                DailyFarming.get().setCurrentlyExecutitng(Strategies.DEFAULT);
            }
        }
    }

    private boolean goToGe() {
        if (!WebWalking.canFindPath(Locations.GRAND_EXCHANGE)) {
            if (Inventory.container().contains("Falador teleport")) {
                Widgets.closeTopInterface();
                Inventory.container().get("Falador teleport").interact("Break");
                Time.waitRegionChange();
            } else {
                if (!Inventory.container().contains("Varrock teleport")) {

                    Widgets.closeTopInterface();
                    Magic.LUMBRIDGE_HOME_TELEPORT.select();
                    Time.sleep(15000);
                    return false;
                }

                Widgets.closeTopInterface();
                Inventory.container().get("Varrock teleport").interact("Break");
                Time.waitRegionChange();
            }

            return false;
        } else {
            return Bank.openNearest();
        }
    }
}
