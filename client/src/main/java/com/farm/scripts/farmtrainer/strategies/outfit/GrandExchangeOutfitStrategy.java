package com.farm.scripts.farmtrainer.strategies.outfit;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer.Status;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.web.account.AccountConfig;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.scripts.farmtrainer.FarmingTrainer;
import com.farm.scripts.farmtrainer.Strategies;

public class GrandExchangeOutfitStrategy extends Strategy {
    private MuleManager muleManager;
    private AccountConfig config;

    public GrandExchangeOutfitStrategy(MuleManager muleManager, AccountConfig config) {
        this.muleManager = muleManager;
        this.config = config;
    }

    public GrandExchangeOutfitStrategy() {
        this.muleManager = Strategies.muleManager;
        this.config = FarmingTrainer.config;
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
                int[] ourOutfit = OutfitWearStrategy.OUTFITS[this.config.getInt("Outfit")];
                int[] var2 = ourOutfit;
                int var3 = ourOutfit.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    int itemId = var2[var4];
                    int price = MathUtils.clamp(OsbuddyExchange.forId(itemId).overallAverage + 30000, 160000, 300000);
                    if (!Inventory.container().countNoted().contains(itemId) && !Bank.getCache().contains(itemId) && !Equipment.isEquipped(itemId)) {
                        if (Inventory.container().getCount(new int[]{995}) < price) {
                            if (Bank.openNearest()) {
                                if (Bank.getContainer().getCount(new int[]{995}) < price) {

                                    if (!this.goToGe()) {
                                        return;
                                    }

                                    this.muleManager.activateResupplyState();
                                    return;
                                }

                                Bank.withdraw(995, 500000);
                            }

                            return;
                        }

                        GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(itemId, 1), price, false);
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


                MultipleStrategyScript.get().setCurrentlyExecutitng(MultipleStrategyScript.get().getDefaultStrategies());
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
                    Time.waitRegionChange();
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
