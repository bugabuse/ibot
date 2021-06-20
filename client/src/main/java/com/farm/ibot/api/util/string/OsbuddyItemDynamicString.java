package com.farm.ibot.api.util.string;

import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchange;
import com.farm.ibot.api.util.web.osbuddyexchange.OsbuddyExchangeItem;

public class OsbuddyItemDynamicString extends DynamicString {
    private int itemId;
    private boolean areWeBuying;
    private int gePrice;
    private PaintTimer lastGeFetchTime = new PaintTimer(0L);

    public OsbuddyItemDynamicString(int itemId, boolean areWeBuying) {
        this.itemId = itemId;
        this.areWeBuying = areWeBuying;
    }

    public int intValue() {
        OsbuddyExchangeItem item = OsbuddyExchange.forId(this.itemId);
        int price = item != null ? (this.areWeBuying ? item.getSellAverage() : item.getBuyAverage()) : -1;
        return price;
    }

    public String toString() {
        return "" + this.intValue();
    }
}
