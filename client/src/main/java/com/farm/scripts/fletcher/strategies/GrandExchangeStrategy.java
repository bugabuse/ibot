package com.farm.scripts.fletcher.strategies;

import com.farm.ibot.api.accessors.Client;
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
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fisher.util.RequiredItem;
import com.farm.scripts.fletcher.Constants;
import com.farm.scripts.fletcher.Fletching;
import com.farm.scripts.fletcher.Strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GrandExchangeStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        Walking.setRun(true);
        if (WorldHopping.isF2p(Client.getCurrentWorld())) {
            (new WorldEnsure()).onAction();
        } else if (this.goToGe()) {
            if (GrandExchange.open()) {
                if (Inventory.getFreeSlots() < 2) {
                    if (Bank.openNearest()) {
                        Bank.depositAllExcept(new int[]{995});
                    }

                } else if (GrandExchange.collect()) {
                    ArrayList<RequiredItem> required = new ArrayList(Arrays.asList(Constants.getRequiredItems().getItems()));
                    Iterator var2 = required.iterator();

                    RequiredItem item;
                    int amountNeeded;
                    do {
                        if (!var2.hasNext()) {

                            Fletching.get().setCurrentlyExecutitng(Strategies.DEFAULT);
                            return;
                        }

                        item = (RequiredItem) var2.next();
                        int ourAmount = Inventory.container().countNoted().getCount(new int[]{item.getId()}) + Bank.getCache().getCount(new int[]{item.getId()});
                        amountNeeded = item.getAmountToBuyAtGrandExchange() - ourAmount;
                    } while (amountNeeded < 1);

                    int price = (int) ((double) OsbuddyExchange.forId(item.getId()).getBuyAverage() * 1.25D);
                    if (price < 30) {
                        price = 50;
                    }

                    if (item.getId() == 66) {
                        price = 325;
                    }

                    if (item.getId() == 946) {
                        price = 1000;
                    }

                    int coinsNeeded = amountNeeded * price;
                    Debug.log("We need " + coinsNeeded + " coins. " + amountNeeded + " @ " + item.getDefinition().name);
                    GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(item.getId(), amountNeeded), price, false);
                    if (!buyOffer.exists() || buyOffer.getCurrentPrice() == price && buyOffer.getStatus() != Status.COMPLETED) {
                        if (coinsNeeded > Inventory.container().getCount(new int[]{995})) {
                            if (Bank.openNearest()) {
                                if (Bank.getContainer().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995}) < coinsNeeded) {
                                    if (!this.sellBows()) {

                                        Strategies.muleManager.activateResupplyState();
                                        return;
                                    }
                                } else {
                                    Bank.withdraw(995, 500000);
                                }
                            }

                        } else {
                            buyOffer.create();
                        }
                    } else {
                        buyOffer.abort();
                    }
                }
            }
        }
    }

    private boolean sellBows() {
        return false;
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
