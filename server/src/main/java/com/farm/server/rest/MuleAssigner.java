/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.primitives.Ints
 *  com.google.gson.Gson
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import com.farm.server.content.trade.AccountData;
import com.farm.server.content.trade.TradeData;
import com.farm.server.rest.AccountsController;
import com.farm.server.rest.MuleController2;
import com.farm.server.rest.WebConfigController;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MuleAssigner {
    @RequestMapping(value={"/muleassign/assign"})
    public TradeData assign(String muleName, String jsonObject) {
        MuleController2.update();
        ArrayList<TradeData> toSort = new ArrayList<TradeData>();
        int[] availablePackages = (int[])AccountsController.GSON.fromJson(jsonObject, int[].class);
        AccountData ourMule = AccountsController.forName(muleName);
        int index = 0;
        if (ourMule == null) {
            return null;
        }
        for (TradeData data : new ArrayList<TradeData>(MuleController2.accounts)) {
            if (data == null || data.username.equalsIgnoreCase(muleName) || data.username.startsWith("#Player") || !Ints.contains((int[])availablePackages, (int)data.id)) continue;
            if (data.muleHandlerName != null && data.muleHandlerName.equals(ourMule.getGameUsername())) {
                return data;
            }
            if (data.muleHandlerName != null && data.muleHandlerName.length() > 0 && this.isOnline(data.muleHandlerName) || !data.requireMuleHandlerAssigned && data.distanceToTile > 10) continue;
            if (index > WebConfigController.getInt("walk_to_mulespot_mules_count")) break;
            if (data.world == ourMule.world) {
                // empty if block
            }
            ++index;
            toSort.add(data);
        }
        if (toSort.size() > 0) {
            TradeData best = (TradeData)toSort.get(0);
            for (TradeData data : toSort) {
                if (data.world != ourMule.world) continue;
                best = data;
                break;
            }
            best.muleHandlerName = ourMule.getGameUsername();
            return best;
        }
        return null;
    }

    private boolean isOnline(String muleHandlerName) {
        AccountData data = AccountsController.forGameUsername(muleHandlerName);
        return data != null && data.isOnline();
    }
}

