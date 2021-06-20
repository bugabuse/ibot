package com.farm.scripts.humanfletcher.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.GrandExchangeItem;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer.Status;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fisher.util.RequiredItem;
import com.farm.scripts.humanfletcher.Constants;
import com.farm.scripts.humanfletcher.HumanFletcher;
import com.farm.scripts.humanfletcher.Strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GrandExchangeStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        System.out.println("GE GE");
        Walking.setRun(true);

        if (WorldHopping.isF2p(Client.getCurrentWorld())) {
            (new WorldEnsure()).onAction();
        } else {
            System.out.println(Arrays.toString(Bank.getContainer().getItems()));
            System.out.println(Bank.isOpen());
            if (this.goToGe()) {

                if (GrandExchangeItem.getItems().length > 0 || GrandExchange.canCollect()) {
                    Debug.log(GrandExchangeItem.getItems()[0].getId());
                    if (GrandExchange.open()) {
                        GrandExchange.collect();
                    }
                }


                if (Bank.getCache().contains(995)) {
                    System.out.println("WITDDRAW");
                    Bank.openAndWithdraw(new Item[]{new Item(995, Integer.MAX_VALUE)});
                } else {

                    if (Inventory.getFreeSlots() < 2) {
                        if (Bank.openNearest()) {
                            Bank.depositAllExcept(new int[]{995});
                        }

                    } else {

                        ArrayList<RequiredItem> required = new ArrayList(Arrays.asList(Constants.getRequiredItems().getItems()));
                        Iterator var2 = required.iterator();

                        RequiredItem item;
                        int amountNeeded;
                        do {
                            if (!var2.hasNext()) {
                                for (; !Inventory.isEmpty(); Time.sleep(1000)) {
                                    if (Bank.openNearest()) {
                                        Bank.depositAll();
                                        Time.waitInventoryChange();
                                        Time.sleep(3000);
                                    }
                                }


                                HumanFletcher.get().setCurrentlyExecutitng(Strategies.DEFAULT);
                                return;
                            }

                            item = (RequiredItem) var2.next();
                            int ourAmount = Inventory.container().countNoted().getCount(new int[]{item.getId()}) + Bank.getCache().getCount(new int[]{item.getId()});
                            amountNeeded = item.getAmountToBuyAtGrandExchange() - ourAmount;
                        } while (amountNeeded < 1);

                        int price = (int) ((double) OsbuddyExchange.forId(item.getId()).getBuyAverage() * 1.1D);
                        int ourCoins = Bank.getContainer().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995});
                        if (item.getName().toLowerCase().contains("log")) {
                            if (amountNeeded < 2000) {
                                if (price < 30) {
                                    price += 15;
                                } else {
                                    price += 10;
                                }
                            } else {
                                price += 5;
                            }

                            if (item.getName().toLowerCase().contains("yew")) {
                                price += 20;
                            }
                        } else if (price < 30) {
                            price += 30;
                        }

                        if (item.getId() == 946) {
                            price = 1000;
                        }

                        if (item.getMaxCoinsPercentToSpend() > 0.0F) {
                            amountNeeded = MathUtils.clamp(ourCoins / price, amountNeeded, (int) ((float) ourCoins * item.getMaxCoinsPercentToSpend()) / price);
                        }

                        int coinsNeeded = amountNeeded * price;
                        Debug.log("We need " + coinsNeeded + " coins. " + amountNeeded + " @ " + item.getDefinition().name);
                        GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(item.getId(), amountNeeded), price, false);
                        if (buyOffer.exists() && (buyOffer.getCurrentPrice() != price || buyOffer.getStatus() == Status.COMPLETED)) {
                            buyOffer.abort();
                        } else if (coinsNeeded > Inventory.container().getCount(new int[]{995})) {

                            if (Bank.openNearest()) {
                                if (Bank.getCache().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995}) < coinsNeeded) {
                                    if (!this.sellBows(855) && !this.sellBows(851)) {
                                        if (GrandExchangeItem.getItems().length > 0) {
                                            return;
                                        }

                                        Strategies.muleManager.activateResupplyState();
                                    }
                                } else {
                                    Bank.withdraw(995, 6000000);
                                }
                            }

                        } else {

                            buyOffer.create();
                        }
                    }
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

    private boolean sellBows(int itemId) {
        if (Bank.getCache().contains(itemId)) {

            Bank.openAndWithdraw(true, new Item[]{new Item(itemId, 500000000)});
            return true;
        } else if (Inventory.container().countNoted().contains(itemId)) {

            int price = (int) ((double) OsbuddyExchange.forId(itemId).overallAverage * 0.9D);
            GrandExchangeOffer sellOffer = new GrandExchangeOffer(new Item(itemId, MathUtils.clamp(Inventory.container().countNoted().getCount(new int[]{itemId}), 0, 100000)), price, true);
            if (sellOffer.exists()) {
                if (sellOffer.getCurrentPrice() != price) {
                    sellOffer.abort();
                    return true;
                } else {
                    return true;
                }
            } else {
                sellOffer.create();
                return true;
            }
        } else {
            GrandExchangeOffer sellOffer = new GrandExchangeOffer(new Item(itemId, Inventory.container().countNoted().getCount(new int[]{itemId})), 0, true);
            return sellOffer.exists();
        }
    }
}
