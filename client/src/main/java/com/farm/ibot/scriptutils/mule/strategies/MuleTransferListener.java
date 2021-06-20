package com.farm.ibot.scriptutils.mule.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.GrandExchangeItem;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.string.DynamicString;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.api.wrapper.item.container.WithdrawContainer;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.scriptutils.mule.MuleManager;

import java.util.Arrays;

public class MuleTransferListener extends Strategy {
    public final MuleManager manager;
    public DynamicString muleQueueCountStart = new WebConfigDynamicString("walk_to_mulespot_mules_count");

    public MuleTransferListener(MuleManager manager) {
        this.manager = manager;
    }

    public boolean active() {
        return this.manager.getEnabledCondition().active();
    }

    public void onAction() {

        if (this.manager.getCurrentState() != 0) {
            if (this.manager.script.getCurrentlyExecuting() != this.manager.muleStrategies) {

                this.manager.setState(0);
            }

        } else {

            if (Bot.get().getFpsData().isOverloaded()) {

                this.manager.setState(0);
            } else {

                if (Bank.hasCache() || !this.manager.determineBankCache) {
                    WithdrawContainer items = (new WithdrawContainer(this.manager.priceHandler, Bank.getCache().getItems())).merge(new WithdrawContainer(this.manager.priceHandler, Inventory.getAll()));
                    int wealth = items.calculateWealth();
                    this.manager.tradeAction = 0;
                    if (wealth > 5000) {
                        this.manager.toWithdraw = items.subtractForPrice(this.manager.wealthToKeep);
                        Debug.log("Items 1" + Arrays.toString(this.manager.toWithdraw.getItemsArray()));
                        if (this.manager.itemsToKeep != null) {

                            this.manager.toWithdraw = this.manager.toWithdraw.subtract(new WithdrawContainer(this.manager.itemsToKeep));
                        }

                        Debug.log("Items 2" + Arrays.toString(this.manager.toWithdraw.getItemsArray()));
                        if (this.manager.giveToMuleActionId != -1 && this.manager.toWithdraw.calculateWealth() > this.manager.wealthToGive) {
                            this.manager.tradeAction = this.manager.giveToMuleActionId;
                            if (this.manager.requiredItems != null) {
                                this.manager.toWithdraw = this.manager.toWithdraw.subtractForPriceAndIgnore(this.manager.wealthToKeep, this.manager.requiredItems);
                            }
                        } else if (this.manager.requiredItems != null && !items.containsAll(new WithdrawContainer(this.manager.requiredItems))) {

                            this.manager.tradeAction = this.manager.supplyFromMuleActionId;
                        }
                    } else if (this.manager.requiredItems != null && !items.containsAll(new WithdrawContainer(this.manager.requiredItems))) {
                        this.manager.tradeAction = this.manager.supplyFromMuleActionId;
                    } else if (this.manager.getGeStrategies() != null) {
                        wealth = (new WithdrawContainer(this.manager.priceHandler, GrandExchangeItem.getItems())).calculateWealth();
                        if (wealth >= this.manager.wealthToKeep) {
                            this.manager.setState(0);
                            if (this.manager.getGeStrategies() != null) {

                                this.manager.script.setCurrentlyExecutitng(this.manager.getGeStrategies());
                            }
                        } else if (this.manager.supplyFromMuleActionId != -1) {
                            this.manager.tradeAction = this.manager.supplyFromMuleActionId;
                        }
                    }
                }


                if (this.manager.supplyFromMuleActionId != -1 && this.manager.tradeAction == this.manager.supplyFromMuleActionId && (!this.manager.requireMuleHandlerAssigned && this.manager.notifierStrategy.queueIndex < this.muleQueueCountStart.intValue() || this.manager.getMuleName() != null)) {

                    this.manager.setState(1);
                }


                if (this.manager.giveToMuleActionId != -1 && this.manager.tradeAction == this.manager.giveToMuleActionId && (!this.manager.requireMuleHandlerAssigned && this.manager.notifierStrategy.queueIndex < this.muleQueueCountStart.intValue() || this.manager.getMuleName() != null)) {

                    if (!this.isTradeRestricted()) {
                        this.manager.setState(2);
                    }
                }

            }
        }
    }

    public boolean isTradeRestricted() {
        if (Varbit.MEMBERSHIP_DAYS.intValue() > 0) {
            return false;
        } else {
            int hours = this.manager.playTime.intValue() / 1000 / 60 / 60;

            return hours < 20 || Client.getTotalLevel() < 100;
        }
    }
}
