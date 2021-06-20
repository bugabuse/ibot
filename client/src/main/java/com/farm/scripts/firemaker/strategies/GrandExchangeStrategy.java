package com.farm.scripts.firemaker.strategies;

import com.farm.ibot.api.accessors.GrandExchangeItem;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer.Status;
import com.farm.ibot.api.methods.walking.PathNotFoundException;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.string.WebConfigAsyncDynamicString;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.firemaker.Firemaker;
import com.farm.scripts.firemaker.Strategies;

public class GrandExchangeStrategy extends Strategy {
    public static Item[] itemsToBuy = new Item[]{new Item(1511, 210), new Item(590, 1)};
    WebConfigDynamicString geMultipler = new WebConfigAsyncDynamicString("fm_ge_price_multipler", 60000L);

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
                        int amountNeeded = item.getAmount() - ourAmount;
                        if (amountNeeded >= 1) {
                            int price = (int) ((double) OsbuddyExchange.forId(item.getId()).overallAverage * this.geMultipler.doubleValue());
                            int coinsNeeded = amountNeeded * price;
                            if (!GrandExchange.ensurePrice(item.getId(), price)) {
                                return;
                            }

                            Debug.log("We need " + coinsNeeded + " coins. " + amountNeeded + " @ " + item.getDefinition().name);
                            if (coinsNeeded > Inventory.container().getCount(new int[]{995})) {
                                if (Bank.openNearest()) {
                                    if (Bank.getContainer().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995}) < coinsNeeded) {

                                        if (GrandExchangeItem.getItems().length == 0) {
                                            Strategies.muleManager.activateResupplyState();
                                        }

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


                    Firemaker.get().setCurrentlyExecutitng(Strategies.DEFAULT);
                }
            }
        }
    }

    private boolean goToGe() {
        try {
            return WebWalking.walkToEnsurePath(Locations.GRAND_EXCHANGE, 6, new Tile[0]);
        } catch (PathNotFoundException var2) {

            Magic.LUMBRIDGE_HOME_TELEPORT.select();
            Time.waitRegionChange();
            return false;
        }
    }
}
