package com.farm.scripts.farmtrainer.strategies;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.shop.Shop;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.farmtrainer.FarmingTrainer;
import com.farm.scripts.farmtrainer.Strategies;

public class BuyPlants extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (this.goToShop()) {
            GrandExchangeOffer offer = new GrandExchangeOffer(new Item(8431, 1), 1, false);
            if (offer.exists()) {
                if (GrandExchange.open()) {
                    GrandExchange.collect();
                    offer.abort();
                }

            } else if (Inventory.isFull()) {
                if (Bank.openNearest()) {
                    Bank.depositAllExcept(new int[]{995});
                }
            } else if (Inventory.getCount(995) < 3000) {
                if (!Bank.hasCache()) {
                    Bank.openNearest();
                } else {
                    if (Bank.getCache().contains(995, 10000)) {
                        Bank.openAndWithdraw(new Item[]{new Item(995, 500000)});
                    } else {
                        Strategies.muleManager.activateResupplyState();
                    }

                }
            } else if (Shop.isOpen() || WebWalking.walkTo(new Tile(3017, 3373), 3, new Tile[0])) {
                if (Shop.open("Garden supplier")) {
                    int amountRequired = 10 + Skill.FARMING.getExperienceToLevel(38) / 31;

                    if (Bank.getCache().getCount(new int[]{8431}) + Inventory.container().countNoted().getCount(new int[]{8431}) > MathUtils.clamp(amountRequired, 1, 2000)) {
                        FarmingTrainer.get().setCurrentlyExecutitng(Strategies.DEFAULT);
                    } else {
                        Debug.log(Shop.getContainer().getCount(new int[]{8431}));
                        if (Shop.getContainer().getCount(new int[]{8431}) > 0) {
                            Shop.buy(8431, 50);
                            Time.waitInventoryChange();
                        } else {
                            WorldHopping.hop(WorldHopping.getRandomP2p());
                        }

                    }
                }
            }
        }
    }

    private boolean goToShop() {
        if (!WebWalking.canFindPath(new Tile(3017, 3373))) {
            if (Inventory.container().contains("Varrock teleport")) {
                Widgets.closeTopInterface();
                Inventory.container().get("Varrock teleport").interact("Break");
                Time.waitRegionChange();
            } else {
                if (!Inventory.container().contains("Falador teleport")) {
                    Widgets.closeTopInterface();

                    Magic.LUMBRIDGE_HOME_TELEPORT.select();
                    Time.waitRegionChange();
                    return false;
                }

                Widgets.closeTopInterface();
                Inventory.container().get("Falador teleport").interact("Break");
                Time.waitRegionChange();
            }

            return false;
        } else {
            return true;
        }
    }
}
