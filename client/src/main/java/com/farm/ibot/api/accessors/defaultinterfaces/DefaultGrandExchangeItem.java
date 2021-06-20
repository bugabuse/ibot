package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IGrandExchangeItem;

public class DefaultGrandExchangeItem implements IGrandExchangeItem {
    public int getId(Object instance) {
        return (Integer) Wrapper.get("GrandExchangeItem.Id", instance);
    }

    public int getAmount(Object instance) {
        return (Integer) Wrapper.get("GrandExchangeItem.Amount", instance);
    }

    public int getPrice(Object instance) {
        return (Integer) Wrapper.get("GrandExchangeItem.Price", instance);
    }

    public byte getState(Object instance) {
        return (Byte) Wrapper.get("GrandExchangeItem.State", instance);
    }
}
