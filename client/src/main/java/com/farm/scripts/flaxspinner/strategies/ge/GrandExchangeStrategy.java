package com.farm.scripts.flaxspinner.strategies.ge;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer.Status;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.string.DynamicString;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.scripts.fisher.util.RequiredItem;

import java.util.Iterator;
import java.util.List;

public class GrandExchangeStrategy extends Strategy {
    public static final DynamicString leatherPrice = new WebConfigDynamicString("leather_price", 60000L);
    public static final DynamicString flaxPrice = new WebConfigDynamicString("flax_price", 60000L);
    public static final DynamicString opalPrice = new WebConfigDynamicString("opal_price", 60000L);
    private List<RequiredItem> requiredItems;
    private MuleManager muleManager;
    private MultipleStrategyScript script;
    private boolean membersOnly;

    public GrandExchangeStrategy(MultipleStrategyScript script, MuleManager muleManager, List<RequiredItem> requiredItems) {
        this.requiredItems = requiredItems;
        this.muleManager = muleManager;
        this.script = script;
    }

    public GrandExchangeStrategy membersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
        return this;
    }

    protected void onAction() {
        Walking.setRun(true);
        if (this.membersOnly && WorldHopping.isF2p(Client.getCurrentWorld())) {
            this.hopToMembersWorld();
        } else if (this.goToGe()) {
            if (GrandExchange.open()) {
                if (Inventory.getFreeSlots() < 2) {
                    if (Bank.openNearest()) {
                        Bank.depositAllExcept(new int[]{995});
                    }

                } else if (GrandExchange.collect()) {
                    if (this.requiredItems.stream().anyMatch((i) -> {
                        return i.getId() == 1741;
                    }) && (new GrandExchangeOffer(new Item(1625, 1), 1, false)).exists()) {
                        (new GrandExchangeOffer(new Item(1625, 1), 1, false)).abort();
                    } else if (this.requiredItems.stream().anyMatch((i) -> {
                        return i.getId() == 1625;
                    }) && (new GrandExchangeOffer(new Item(1741, 1), 1, false)).exists()) {
                        (new GrandExchangeOffer(new Item(1741, 1), 1, false)).abort();
                    } else {
                        Iterator var1 = this.requiredItems.iterator();

                        int ourAmount;
                        do {
                            RequiredItem item;
                            int amountNeeded;
                            do {
                                if (!var1.hasNext()) {
                                    while (!Inventory.isEmpty()) {
                                        if (Bank.openNearest()) {
                                            Bank.depositAll();
                                            this.sleep(3000);
                                        }
                                    }


                                    this.script.setCurrentlyExecutitng(this.script.getDefaultStrategies());
                                    return;
                                }

                                item = (RequiredItem) var1.next();
                                ourAmount = Inventory.container().countNoted().getCount(new int[]{item.getId()}) + Bank.getCache().getCount(new int[]{item.getId()});
                                amountNeeded = item.getAmountToBuyAtGrandExchange() - ourAmount;
                            } while (amountNeeded < 1);

                            int price = (int) ((double) OsbuddyExchange.forId(item.getId()).getBuyAverage() * 1.35D);
                            if (item.getId() == 1625) {
                                price = 300;
                            }

                            if (item.getId() == 1755) {
                                price = 1000;
                            }

                            if (item.getId() != 1779) {
                                if (price < 30) {
                                    price = 50;
                                }
                            } else {
                                price = flaxPrice.intValue();
                            }

                            if (item.getId() == 1625) {
                                price = opalPrice.intValue();
                            }

                            if (item.getId() == 1733) {
                                price = 1000;
                            }

                            if (item.getId() == 1741) {
                                price = leatherPrice.intValue();
                            }

                            int coinsNeeded = amountNeeded * price;
                            Debug.log("We need " + coinsNeeded + " coins. " + amountNeeded + " @ " + item.getDefinition().name);
                            GrandExchangeOffer buyOffer = new GrandExchangeOffer(new Item(item.getId(), amountNeeded), price, false);
                            if (!buyOffer.exists()) {
                                if (coinsNeeded > Inventory.container().getCount(new int[]{995})) {
                                    if (Bank.openNearest()) {
                                        if (Bank.getContainer().getCount(new int[]{995}) + Inventory.container().getCount(new int[]{995}) < coinsNeeded) {

                                            this.muleManager.activateResupplyState();
                                            return;
                                        }

                                        Bank.withdraw(995, 500000);
                                    }

                                    return;
                                }

                                buyOffer.create();
                                return;
                            }

                            if (buyOffer.getCurrentPrice() != price || buyOffer.getStatus() == Status.COMPLETED) {
                                buyOffer.abort();
                                return;
                            }
                        } while (ourAmount > 700);

                    }
                }
            }
        }
    }

    private void hopToMembersWorld() {
        if (WorldHopping.isF2p(Client.getCurrentWorld())) {
            ScriptUtils.interruptCurrentLoop();
            if (Dialogue.isInDialouge()) {
                WebWalking.walkTo(Player.getLocal().getPosition(), -1, new Tile[0]);
                return;
            }

            Widgets.closeTopInterface();
            WorldHopping.hop(WorldHopping.getRandomP2p());
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
}
