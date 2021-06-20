package com.farm.ibot.api.util.web.osbuddyexchange;

import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.util.web.WebClient;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Optional;

public class OsbuddyExchange {
    private static HashMap<String, OsbuddyExchangeItem> itemsCache = new HashMap();
    private static HashMap<Integer, OsbuddyExchangeItem> itemsCacheOsrs = new HashMap();
    private static PaintTimer lastUpdate = new PaintTimer(0L);

    private static boolean fetch() {
        if (itemsCache.isEmpty() || lastUpdate.getElapsedSeconds() > 300L) {
            HashMap<String, OsbuddyExchangeItem> temp = (HashMap) WebUtils.downloadObjectFromUrl((new TypeToken<HashMap<String, OsbuddyExchangeItem>>() {
            }).getType(), "https://rsbuddy.com/exchange/summary.json");
            if (temp != null && !temp.isEmpty()) {
                itemsCache = temp;
            }
        }

        if (!itemsCache.isEmpty()) {
            lastUpdate.reset();
        }

        return !itemsCache.isEmpty();
    }

    public static OsbuddyExchangeItem forId(int id) {
        //System.out.println("LOOK FOR ID " + id + ", " + (String) Optional.of(ItemDefinition.forId(id)).map((i) -> {
        //    return i.name;
        //}).orElse("Null"));

        if (fetch()) {
            OsbuddyExchangeItem item = (OsbuddyExchangeItem) itemsCache.get("" + id);
            return item.getSellAverage() != 0 && item.getBuyAverage() != 0 ? item : forIdOsrsGePrice(id);
        } else {
            return forIdOsrsGePrice(id);
        }
    }

    private static OsbuddyExchangeItem forIdOsrsGePrice(int id) {
        if (itemsCacheOsrs.get(id) == null || System.currentTimeMillis() - ((OsbuddyExchangeItem) itemsCacheOsrs.get(id)).fetchTime > 60000L) {
            OsbuddyExchangeItem osbuddyExchangeItem = new OsbuddyExchangeItem();
            osbuddyExchangeItem.sellAverage = fetchPriceOsrs(id);
            osbuddyExchangeItem.buyAverage = osbuddyExchangeItem.sellAverage;
            osbuddyExchangeItem.overallAverage = osbuddyExchangeItem.sellAverage;
            itemsCacheOsrs.put(id, osbuddyExchangeItem);

        }

        return (OsbuddyExchangeItem) itemsCacheOsrs.getOrDefault(id, null);
    }

    private static int fetchPriceOsrs(int itemId) {
        try {
            String raw = (new WebClient()).downloadString("http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=" + itemId).split("price\":")[1].split("}")[0];
            return !raw.endsWith("m") && !raw.endsWith("k") ? Integer.parseInt(raw) : (int) (Double.parseDouble(raw.substring(0, raw.length() - 1)) * (double) (raw.endsWith("m") ? 1000000 : 1000));
        } catch (Exception var2) {
            var2.printStackTrace();
            Time.sleep(5000);
            return -1;
        }
    }
}
