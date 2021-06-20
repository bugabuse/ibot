package com.farm.ibot.api.wrapper.item.container;

import com.farm.ibot.api.wrapper.item.Item;

public class WithdrawItem extends Item {
    private PriceHandler priceHandler;
    private int price;

    public WithdrawItem(Item item) {
        this(item != null ? item.getId() : -1, item != null ? item.getAmount() : 0);
        if (item != null) {
            this.slot = item.getSlot();
        }

    }

    public WithdrawItem(int id, int amount) {
        this(id, amount, 0);
    }

    public WithdrawItem(int id, int amount, int price) {
        super(id, amount);
        this.priceHandler = new PriceHandler(new WithdrawItem[0]);
        this.price = 0;
        this.price = price;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setPriceHandler(PriceHandler priceHandler) {
        this.priceHandler = priceHandler;
    }

    public int getUnitPrice() {
        return this.price == 0 ? this.priceHandler.getPrice(this.getId()) : this.price;
    }

    public int getTotalPrice() {
        return this.getUnitPrice() * this.getAmount();
    }
}
