package com.farm.scripts.fisher.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.shop.Shop;
import com.farm.ibot.api.methods.walking.PathNotFoundException;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.fisher.FishSettings;
import com.farm.scripts.fisher.Fisher;
import com.farm.scripts.fisher.FishingConfig;
import com.farm.scripts.fisher.Strategies;
import com.farm.scripts.fisher.util.RequiredItem;

import java.util.ArrayList;
import java.util.Iterator;

public class GrandExchangeStrategy extends Strategy {
    public static final Tile SHOP_TILE = new Tile(3013, 3223, 0);

    public static boolean unpackItems() {
        if (!Inventory.container().contains((i) -> {
            return i.getName().contains(" pack");
        })) {
            Item item = Bank.getCache().get((i) -> {
                return i.getName().contains(" pack");
            });
            if (item != null) {

                Debug.log(item.getId() + " " + item.getDefinition().name + " " + item.getAmount());
                if (Inventory.container().hasSpace(new Item(item.getId(), 10))) {
                    Bank.openAndWithdraw(new Item[]{new Item(item.getId(), 10)});
                    Time.waitInventoryChange();
                } else {

                    Bank.depositAll();
                    Time.waitInventoryChange();
                }

                return false;
            } else {
                return true;
            }
        } else {
            Widgets.closeTopInterface();
            Iterator var0 = Inventory.container().getAll((i) -> {
                return i.getName().contains(" pack");
            }).iterator();

            while (var0.hasNext()) {
                Item item = (Item) var0.next();
                item.interact("Open");
                Time.waitInventoryChange();
            }

            return false;
        }
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Walking.setRun(true);
        ArrayList<RequiredItem> itemsToBuy = new ArrayList();
        boolean isNeeded = false;
        FishingConfig[] var3 = FishSettings.ALL_CONFIGS;
        int var4 = var3.length;

        int ourAmount;
        for (ourAmount = 0; ourAmount < var4; ++ourAmount) {
            FishingConfig config = var3[ourAmount];
            if (config == FishSettings.getConfig()) {
                isNeeded = true;
            }

            if (isNeeded) {
                RequiredItem[] var7 = config.getFishingEquipment();
                int var8 = var7.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    RequiredItem item = var7[var9];
                    if (itemsToBuy.stream().noneMatch((ix) -> {
                        return ix.getDefinition().getUnnotedId() == item.getDefinition().getUnnotedId();
                    })) {
                        itemsToBuy.add(item);
                    }
                }
            }
        }

        if (Inventory.getFreeSlots() < 2) {
            if (Bank.openNearest()) {

                Bank.depositAllExcept(new int[]{995});
            }

        } else if (unpackItems()) {
            Iterator var11 = itemsToBuy.iterator();

            while (var11.hasNext()) {
                RequiredItem item = (RequiredItem) var11.next();
                ourAmount = Inventory.container().countNoted().getCount(new int[]{item.getId()}) + Bank.getCache().getCount(new int[]{item.getId()});
                int amountNeeded = item.getAmountToBuyAtGrandExchange() - ourAmount;
                Debug.log(item.getName() + " [1] AMT " + ourAmount);
                if (amountNeeded > 0) {
                    if (item.getName().contains("bait")) {
                        item = new RequiredItem("Bait pack", 0, 4, 0);
                    } else if (item.getName().equalsIgnoreCase("Feather")) {
                        item = new RequiredItem("Feather pack", 0, 4, 0);
                    }

                    ourAmount = Inventory.container().countNoted().getCount(new int[]{item.getId()}) + Bank.getCache().getCount(new int[]{item.getId()});
                    amountNeeded = item.getAmountToBuyAtGrandExchange() - ourAmount;
                    Debug.log(item.getName() + " [2] AMT " + ourAmount);
                    if (amountNeeded >= 1) {
                        if (Inventory.container().getCount(new int[]{995}) < 1000) {

                            if (Bank.openNearest()) {
                                if (Bank.getContainer().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995}) < 1000) {

                                    Strategies.muleManager.activateResupplyState();
                                    return;
                                }

                                Bank.withdraw(995, 500000);
                            }

                            return;
                        }

                        Debug.log("BUY " + item.getName() + " " + amountNeeded);
                        if (!this.goToShop()) {
                            return;
                        }

                        if (!Shop.open("Gerrant")) {
                            return;
                        }

                        if (amountNeeded >= 10) {
                            Shop.buy(item.getId(), 10);
                        } else if (amountNeeded >= 5) {
                            Shop.buy(item.getId(), 5);
                        } else {
                            for (int i = 0; i < amountNeeded; ++i) {
                                Shop.buy(item.getId(), 1);
                                Time.waitInventoryChange();
                            }
                        }

                        Time.waitInventoryChange();
                        Time.sleep(2000);
                        return;
                    }
                }
            }

            if (unpackItems()) {

                Fisher.get().setCurrentlyExecutitng(Strategies.DEFAULT);
            }
        }
    }

    private boolean goToShop() {
        try {
            return WebWalking.walkToEnsurePath(SHOP_TILE, 6, new Tile[0]);
        } catch (PathNotFoundException var2) {
            Magic.LUMBRIDGE_HOME_TELEPORT.select();
            Time.waitRegionChange();
            return false;
        }
    }
}
