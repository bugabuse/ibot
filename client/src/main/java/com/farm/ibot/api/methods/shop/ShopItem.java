package com.farm.ibot.api.methods.shop;

import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.wrapper.item.Item;

public class ShopItem {
    public String name;
    public PaintTimer lastUpdate = new PaintTimer();
    public long updateInterval = -1L;
    private int lastCount = -1;

    public ShopItem(String name) {
        this.name = name;
    }

    public void listen() {
        Item shopItem = Shop.getContainer().get(this.name);
        if (shopItem != null) {
            if (shopItem.getAmount() > this.lastCount) {
                if (this.lastUpdate.getTime() != -1L && this.updateInterval == -1L && this.lastUpdate.getElapsed() < 15000L) {
                    this.updateInterval = System.currentTimeMillis() - this.lastUpdate.getTime();
                }

                this.lastUpdate.reset();
            }

            this.lastCount = shopItem.getAmount();
        }

    }

    public long getNextUpdateTime() {
        long time = this.updateInterval - this.lastUpdate.getElapsed();
        return time < -600L ? 1000L : time;
    }
}
