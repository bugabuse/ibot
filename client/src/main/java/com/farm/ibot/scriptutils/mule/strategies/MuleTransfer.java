package com.farm.ibot.scriptutils.mule.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.Trade;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.methods.walking.PathNotFoundException;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.WithdrawContainer;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.MuleUtils;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;

import java.util.Iterator;

public class MuleTransfer extends Strategy implements MessageListener {
    private final MuleManager manager;
    int tradeOnWorld = -1;

    public MuleTransfer(MuleManager manager, BotScript script) {
        script.addEventHandler(new MessageEventHandler(this));
        this.manager = manager;
    }

    public boolean active() {
        return this.manager.getCurrentState() == 2 || this.manager.getCurrentState() == 1;
    }

    public void onAction() {
        Bot.get().getScriptHandler().loginRandom.active = true;
        Bot.get().getScriptHandler().antiKick.active = true;
        if (this.manager.tradeOnWorldGive != -1 && this.manager.tradeAction == this.manager.giveToMuleActionId) {
            if (this.manager.muleTransferListener.isTradeRestricted()) {
                this.returnToNormalState();
                return;
            }

            this.tradeOnWorld = this.manager.tradeOnWorldGive;
        }

        if (this.manager.tradeOnWorldResupply != -1 && this.manager.tradeAction == this.manager.supplyFromMuleActionId) {
            this.tradeOnWorld = this.manager.tradeOnWorldResupply;
        }

        if (!this.manager.getEnabledCondition().active()) {

            this.returnToNormalState();
        } else {
            OnDemandMuleDynamicString muleName = this.manager.getMuleDynamicString();
            if (muleName != null && muleName.tradeData != null && muleName.tradeData.id != this.manager.tradeAction) {

                this.returnToNormalState();
            } else if (this.manager.tradeAction == 0) {

                this.returnToNormalState();
            } else if (this.manager.requireMuleHandlerAssigned && this.manager.getMuleName() == null) {

                this.returnToNormalState();
            } else if (Bot.get().getFpsData().getFps(60) < 5L) {

                this.returnToNormalState();
            } else if (this.tradeOnWorld == -1 && !WorldHopping.isF2p(Client.getCurrentWorld())) {
                WorldHopping.hop(WorldHopping.getRandomF2p());
            } else {

                if (this.manager.getCurrentState() == 1) {
                    try {
                        if (WebWalking.walkToEnsurePath(this.manager.resupplyMuleTile, 1)) {
                            this.hopToTradingWorld();
                            if (this.manager.getMuleName() != null && Players.get(this.manager.getMuleName()) != null) {
                                this.manager.setState(2);
                            }
                        }

                    } catch (PathNotFoundException var9) {
                        Magic.LUMBRIDGE_HOME_TELEPORT.select();
                        Time.sleep(20000);
                    }
                } else {

                    if (this.walkToTradeArea(8)) {
                        this.hopToTradingWorld();

                        this.hopToTradingWorld();

                        WithdrawContainer toWithdraw = this.manager.toWithdraw;
                        if (this.manager.tradeAction != this.manager.giveToMuleActionId || toWithdraw != null && toWithdraw.getPriceableItemArray().length != 0) {

                            if (this.manager.tradeAction == this.manager.supplyFromMuleActionId && Inventory.getFreeSlots() < 5) {

                                if (Bank.openNearest()) {
                                    Bank.depositAll();
                                }

                            } else {

                                if (Trade.isOpen()) {
                                    this.doTrade();
                                } else {

                                    if (this.manager.tradeAction == this.manager.giveToMuleActionId) {
                                        Iterator var3 = toWithdraw.getItems().iterator();

                                        while (var3.hasNext()) {
                                            WithdrawItem item = (WithdrawItem) var3.next();
                                            if (!item.getDefinition().isStackable() && Inventory.contains(item.getId())) {
                                                Debug.log("Open bank 1, non stackable: " + item.getId());
                                                if (Bank.openNearest()) {
                                                    Bank.depositAll();
                                                    return;
                                                }
                                            }
                                        }

                                        WithdrawContainer inventoryItems = new WithdrawContainer(this.manager.priceHandler, Inventory.getAll());
                                        if (!inventoryItems.contains(toWithdraw)) {

                                            if (!Bank.openNearest()) {
                                                return;
                                            }

                                            Bank.depositAll((i) -> {
                                                return Inventory.isFull();
                                            });
                                            inventoryItems = inventoryItems.merge(new WithdrawContainer(Bank.getItemsArray()));
                                            if (!inventoryItems.contains(toWithdraw)) {
                                                Debug.log("Return: inventoryItems.contains(toWithdraw)");
                                                this.returnToNormalState();
                                                return;
                                            }

                                            inventoryItems = new WithdrawContainer(this.manager.priceHandler, Inventory.getAll());
                                            WithdrawContainer bankItems = toWithdraw.subtract(inventoryItems);
                                            Bank.depositAll((i) -> {
                                                return Inventory.isFull() || toWithdraw.contains(i.getId()) && !i.getDefinition().isStackable();
                                            });
                                            WithdrawItem[] var5 = bankItems.getPriceableItemArray();
                                            int var6 = var5.length;

                                            for (int var7 = 0; var7 < var6; ++var7) {
                                                WithdrawItem item = var5[var7];
                                                Debug.log("Withdraw: " + item.getDefinition().getUnnotedId() + " : " + item.getAmount());
                                                Bank.withdraw(true, item.getDefinition().getUnnotedId(), item.getAmount());
                                                Time.sleep(680, 1200);
                                            }

                                            return;
                                        }
                                    }

                                    if (this.walkToTradeArea(1)) {

                                        if (Players.get(this.manager.getMuleName()) == null && !MuleUtils.isOnline(this.manager.getMuleName())) {
                                            Debug.log("Online: " + MuleUtils.isOnline(this.manager.getMuleName()));
                                            Debug.log("In game:: " + Players.get(this.manager.getMuleName()));
                                        } else {

                                            this.doTrade();
                                        }
                                    }
                                }
                            }
                        } else {
                            Debug.log("Return: manager.tradeAction == manager.giveToMuleActionId && (toWithdraw == null || toWithdraw.getPriceableItemArray().length == 0");
                            this.returnToNormalState();
                        }
                    }
                }
            }
        }
    }

    private void hopToTradingWorld() {
        if (this.tradeOnWorld != -1) {
            if (this.manager.notifierStrategy.queueIndex >= this.manager.muleTransferListener.muleQueueCountStart.intValue() && this.manager.getMuleName() == null) {

                if (WorldHopping.toRegularWorldNumber(Client.getCurrentWorld()) == WorldHopping.toRegularWorldNumber(this.tradeOnWorld)) {
                    WorldHopping.hop(WorldHopping.getRandomF2p());
                    return;
                }
            } else {
                Debug.log("WORLD " + this.manager.notifierStrategy.queueIndex + " MAX INDEX " + this.manager.muleTransferListener.muleQueueCountStart.intValue());
                if (!WorldHopping.hop(this.tradeOnWorld)) {
                    return;
                }
            }
        }

    }

    private boolean walkToTradeArea(int distance) {
        if (this.manager.tradeAction == this.manager.giveToMuleActionId) {
            if (!Trade.isOpen() && !this.manager.walkToSpotStrategy.walkToSpot(this.manager.receivingMuleTile, distance)) {
                return false;
            }
        } else if (!Trade.isOpen() && !this.manager.walkToSpotStrategy.walkToSpot(this.manager.resupplyMuleTile, distance)) {
            return false;
        }

        return true;
    }

    private void returnToNormalState() {

        this.manager.setState(0);
        this.manager.tradeAction = 0;
        this.manager.toWithdraw = null;
        WebUtils.sendRequest("mule/remove/?username=" + Player.getLocal().getName());
    }

    private void doTrade() {
        Player mule = Players.get(this.manager.getMuleName());
        if (mule != null) {
            if (Inventory.isFull()) {

                if (Bank.openNearest()) {
                    Bank.depositAll();
                }

            } else if (!Trade.isOpen()) {
                mule.interact("Trade");
                if (Time.sleep(10000, Trade::isOpen)) {
                    Time.sleep(600, 800);
                }

            } else if (!Trade.getOponentName().equalsIgnoreCase(this.manager.getMuleName())) {
                Trade.decline();
            } else {
                if (this.manager.tradeAction == this.manager.giveToMuleActionId) {
                    if (Trade.getOpenedScreen() == 1) {
                        WithdrawItem[] var2 = this.manager.toWithdraw.getPriceableItemArray();
                        int var3 = var2.length;

                        for (int var4 = 0; var4 < var3; ++var4) {
                            WithdrawItem item = var2[var4];
                            if (item.getAmount() > 0 && Inventory.get(item.getName()) != null) {
                                Item invItem = Inventory.container().get((it) -> {
                                    return it.getDefinition().isStackable() && it.getName().equalsIgnoreCase(item.getName());
                                });
                                Debug.log("GIVE " + invItem.getId() + " " + MathUtils.clamp(item.getAmount(), invItem.getAmount()));
                                if (invItem != null && !Trade.add(invItem.getId(), MathUtils.clamp(item.getAmount(), invItem.getAmount()))) {
                                    return;
                                }
                            }
                        }
                    }

                    Trade.accept();
                } else {
                    int screen = Trade.getOpenedScreen();
                    if (Trade.hasOtherAccepted() || screen == 2) {
                        Trade.accept();
                        if (screen == 2 && Time.sleep(() -> {
                            return !Trade.isOpen();
                        })) {
                            this.returnToNormalState();
                        }
                    }
                }

            }
        }
    }

    public void onMessage(String message) {
        if (message.contains("Accepted trade.")) {
            this.returnToNormalState();
        }

    }
}
