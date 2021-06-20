/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import com.farm.server.content.trade.AccountData;
import com.farm.server.core.WorldHopping;
import com.farm.server.rest.AccountsController;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntConsumer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotAssigner {
    @RequestMapping(value={"/accounts/worldAssign"})
    public int add(String username, int spotCount) throws IOException {
        AccountData currentAccount = AccountsController.forName(username);
        HashMap<Integer, Integer> worldCount = new HashMap<Integer, Integer>();
        Arrays.stream(WorldHopping.F2P_WORLDS).forEach(w -> worldCount.put(w, 0));
        if (currentAccount == null) {
            return WorldHopping.getRandomF2p();
        }
        for (AccountData data : AccountsController.getOnlineAccounts()) {
            if (data.getUniqueScriptId() % spotCount != currentAccount.getUniqueScriptId() % spotCount || !Objects.equals(data.autostartScript, currentAccount.autostartScript) || !worldCount.containsKey(WorldHopping.toRegularWorldNumber(data.world))) continue;
            worldCount.put(WorldHopping.toRegularWorldNumber(data.world), worldCount.getOrDefault(WorldHopping.toRegularWorldNumber(data.world), 0) + 1);
        }
        int best = -1;
        int bestWorld = -1;
        for (Map.Entry entry : worldCount.entrySet()) {
            if (best != -1 && (Integer)entry.getValue() >= best) continue;
            bestWorld = (Integer)entry.getKey();
            best = (Integer)entry.getValue();
        }
        currentAccount.world = WorldHopping.toExpandedWorldNumber(bestWorld);
        currentAccount.preferredWorld = WorldHopping.toExpandedWorldNumber(bestWorld);
        currentAccount.lastUpdate = System.currentTimeMillis();
        if (bestWorld == -1) {
            bestWorld = WorldHopping.getRandomF2p();
        }
        return bestWorld;
    }
}

