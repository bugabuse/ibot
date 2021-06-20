package com.farm.ibot.api.wrapper.item.container;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class PriceHandler {
    public ArrayList<Entry<Integer, Integer>> priceList = new ArrayList();
    public ArrayList<Entry<Integer, Integer>> amountList = new ArrayList();

    public PriceHandler(WithdrawItem... items) {
        this.addItems(items);
    }

    public void addItems(WithdrawItem... items) {
        WithdrawItem[] var2 = items;
        int var3 = items.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            WithdrawItem item = var2[var4];
            this.priceList.add(new SimpleEntry(item.getId(), item.getUnitPrice()));
            this.amountList.add(new SimpleEntry(item.getId(), item.getAmount()));
        }

    }

    public int getPrice(int id) {
        Iterator var2 = this.priceList.iterator();

        Entry entry;
        do {
            if (!var2.hasNext()) {
                return 0;
            }

            entry = (Entry) var2.next();
        } while ((Integer) entry.getKey() != id);

        return (Integer) entry.getValue();
    }

    public int getAmount(int id) {
        Iterator var2 = this.amountList.iterator();

        Entry entry;
        do {
            if (!var2.hasNext()) {
                return 0;
            }

            entry = (Entry) var2.next();
        } while ((Integer) entry.getKey() != id);

        return (Integer) entry.getValue();
    }

    public void setPrice(int id, int price) {
        Iterator var3 = this.priceList.iterator();

        while (var3.hasNext()) {
            Entry<Integer, Integer> entry = (Entry) var3.next();
            if ((Integer) entry.getKey() == id) {
                entry.setValue(price);
            }
        }

    }
}
