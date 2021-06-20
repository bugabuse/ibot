package com.farm.scripts.fisher.util;

import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.wrapper.item.Item;

public class RequiredItem extends Item {
    private int amountToWithdrawFromBank;
    private int amountToBuyAtGrandExchange;
    private int amountMinimum;
    private float maxCoinsPercentToSpend;

    public RequiredItem(String name, int amountToWithdrawFromBank, int amountToBuyAtGrandExchange, int amountMinimum) {
        this(ItemDefinition.forName(name).getUnnotedId(), amountToWithdrawFromBank, amountToBuyAtGrandExchange, amountMinimum);
    }

    public RequiredItem(int id, int amountToWithdrawFromBank, int amountToBuyAtGrandExchange, int amountMinimum) {
        super(id, amountMinimum);
        this.amountToWithdrawFromBank = amountToWithdrawFromBank;
        this.amountToBuyAtGrandExchange = amountToBuyAtGrandExchange;
        this.amountMinimum = amountMinimum;
    }

    public int getAmountToWithdrawFromBank() {
        return this.amountToWithdrawFromBank;
    }

    public void setAmountToWithdrawFromBank(int amountToWithdrawFromBank) {
        this.amountToWithdrawFromBank = amountToWithdrawFromBank;
    }

    public int getAmountToBuyAtGrandExchange() {
        return this.amountToBuyAtGrandExchange;
    }

    public void setAmountToBuyAtGrandExchange(int amountToBuyAtGrandExchange) {
        this.amountToBuyAtGrandExchange = amountToBuyAtGrandExchange;
    }

    public int getAmountMinimum() {
        return this.amountMinimum;
    }

    public void setAmountMinimum(int amountMinimum) {
        this.amountMinimum = amountMinimum;
    }

    public float getMaxCoinsPercentToSpend() {
        return this.maxCoinsPercentToSpend;
    }

    public void setMaxCoinsPercentToSpend(int maxCoinsPercentToSpend) {
        this.maxCoinsPercentToSpend = (float) maxCoinsPercentToSpend;
    }

    public RequiredItem withMaxToSpend(float maxCoinsPercentToSpend) {
        this.maxCoinsPercentToSpend = maxCoinsPercentToSpend;
        return this;
    }
}
