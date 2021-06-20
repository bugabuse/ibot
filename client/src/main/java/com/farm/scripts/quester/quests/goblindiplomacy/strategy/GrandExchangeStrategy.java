package com.farm.scripts.quester.quests.goblindiplomacy.strategy;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.string.WebConfigAsyncDynamicString;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.goblindiplomacy.GoblinDiplomacy;

public class GrandExchangeStrategy extends Strategy {
    public static Item[] itemsToBuy = new Item[]{new Item(288, 3), new Item(1767, 1), new Item(1769, 1)};
    WebConfigDynamicString priceItems = new WebConfigAsyncDynamicString("price_dyes", 60000L);

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
                            int price = this.priceItems.intValue();
                            int coinsNeeded = amountNeeded * price;
                            GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(item.getId(), amountNeeded), price, false);
                            if (buyOffer.exists()) {
                                if (buyOffer.getCurrentPrice() != price) {
                                    buyOffer.abort();
                                }

                                return;
                            }

                            Debug.log("We need " + coinsNeeded + " coins. " + amountNeeded + " @ " + item.getDefinition().name);
                            if (coinsNeeded > Inventory.container().getCount(new int[]{995})) {
                                if (Bank.openNearest()) {
                                    if (Bank.getContainer().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995}) < coinsNeeded) {

                                        GoblinDiplomacy.muleManager.activateResupplyState();
                                        return;
                                    }

                                    Bank.withdraw(995, 6000000);
                                }

                                return;
                            }

                            buyOffer.create();
                            return;
                        }
                    }


                    GoblinDiplomacy.instance.setCurrentlyExecutitng(GoblinDiplomacy.instance.getDefaultStrategies());
                }
            }
        }
    }

    private boolean goToGe() {
        return WebWalking.walkTo(Locations.GRAND_EXCHANGE, new Tile[0]);
    }
}
