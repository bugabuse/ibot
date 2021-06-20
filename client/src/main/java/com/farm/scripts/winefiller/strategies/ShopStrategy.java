package com.farm.scripts.winefiller.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.shop.Shop;
import com.farm.ibot.api.methods.walking.PathNotFoundException;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.winefiller.Strategies;
import com.farm.scripts.winefiller.WineFiller;

public class ShopStrategy extends Strategy {
    public static Tile SHOP_TILE = new Tile(3219, 3413, 0);

    public static boolean unpackItems() {
        if (Shop.isOpen() && !Inventory.isFull()) {
            return true;
        } else {
            Item item;
            if (Inventory.container().contains((i) -> {
                return i.getName().contains(" pack");
            })) {
                Widgets.closeTopInterface();
                item = Inventory.container().get((i) -> {
                    return i.getName().contains(" pack");
                });
                item.interact("Open");
                Time.sleep(() -> {
                    return !Inventory.container().contains((i) -> {
                        return i.getName().contains(" pack");
                    });
                });
                return false;
            } else {
                item = Bank.getCache().get((i) -> {
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
            }
        }
    }

    public boolean active() {
        return WineFiller.currentState == WineFiller.GRAND_EXCHANGE;
    }

    public void onAction() {
        if (Strategies.getSpot().tile.getNote().contains("Falador")) {
            SHOP_TILE = new Tile(2958, 3388, 0);
        } else {
            SHOP_TILE = new Tile(3219, 3413, 0);
        }

        if (Inventory.getFreeSlots() < 2) {
            if (Bank.openNearest()) {

                Bank.depositAllExcept(new int[]{995});
            }

        } else if (unpackItems()) {
            int ourAmount = Inventory.container().countNoted().getCount(new int[]{Strategies.JUG_EMPTY}) + Bank.getCache().getCount(new int[]{Strategies.JUG_EMPTY});
            ourAmount += Inventory.container().getCount((i) -> {
                return i.getName().contains(" pack");
            }) * 100;
            int amountNeeded = 5000 - ourAmount;
            if (amountNeeded <= 0) {
                WineFiller.currentState = WineFiller.NORMAL;
            } else if (Inventory.container().getCount(new int[]{995}) < 250) {

                if (ourAmount >= 500) {
                    WineFiller.currentState = WineFiller.NORMAL;
                } else {
                    if (Bank.openNearest()) {
                        if (Bank.getContainer().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995}) < 300) {

                            WineFiller.currentState = WineFiller.NORMAL;
                            Strategies.muleManager.activateResupplyState();
                            return;
                        }

                        Bank.withdraw(995, 500000);
                    }

                }
            } else if (this.goToShop()) {
                if (Shop.open("Shop keeper")) {
                    Shop.buy(20742, 5);
                    Time.waitInventoryChange();
                }
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
