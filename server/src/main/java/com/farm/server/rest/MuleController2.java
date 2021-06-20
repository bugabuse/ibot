/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.gson.Gson
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestMethod
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.farm.server.content.trade.AccountData;
import com.farm.server.content.trade.TradeData;
import com.farm.server.core.util.Debug;
import com.farm.server.rest.AccountsController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MuleController2 {
    public static ArrayList<TradeData> accounts = new ArrayList();
    public static final Gson GSON = new Gson();

    @RequestMapping(value={"/mule/remove"})
    public String remove(String username) {
        for (TradeData data : new ArrayList<TradeData>(accounts)) {
            if (!data.username.equalsIgnoreCase(username)) continue;
            accounts.remove(data);
            break;
        }
        return "Thanks";
    }

    @RequestMapping(value={"/mule/online"})
    public ArrayList<TradeData> online() {
        try {
            MuleController2.update();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (accounts == null) {
            Debug.log("Accounts == null. WTF???");
        }
        return new ArrayList<TradeData>(accounts);
    }

    @RequestMapping(value={"/mule/handle"})
    public String handle(String slaveName, String muleName) {
        TradeData account = this.forName(slaveName);
        if (account != null) {
            AccountData acc;
            if (!Strings.isNullOrEmpty((String)account.muleHandlerName) && !account.muleHandlerName.equalsIgnoreCase(muleName) && (acc = AccountsController.forGameUsername(account.muleHandlerName)) != null && acc.isOnline()) {
                return "false, mule already defined.";
            }
            account.muleHandlerName = muleName;
            return "true";
        }
        return "false, account not found";
    }

    @RequestMapping(value={"/mule/add"}, method={RequestMethod.POST})
    public int add(String jsonObject) {
        TradeData account = (TradeData)GSON.fromJson(jsonObject, TradeData.class);
        if (account == null) {
            return -1;
        }
        TradeData currentAccount = this.forName(account.username);
        account.lastUpdate = System.currentTimeMillis();
        if (currentAccount != null) {
            currentAccount.updateWith(account);
        } else {
            accounts.add(account);
        }
        return this.getIndex(account.username);
    }

    @RequestMapping(value={"/mule/get"})
    public TradeData get(String slaveName) {
        return this.forName(slaveName);
    }

    public static void update() {
        for (TradeData account : new ArrayList<TradeData>(accounts)) {
            if (account != null && System.currentTimeMillis() - account.lastUpdate <= 60000L && !"null".equalsIgnoreCase(account.username)) continue;
            accounts.remove(account);
        }
        ArrayList<TradeData> accs = new ArrayList<TradeData>(accounts);
        accounts = accs;
    }

    public TradeData forName(String name) {
        return new ArrayList<TradeData>(accounts).stream().filter(a -> Objects.equals(a.username, name)).findAny().orElse(null);
    }

    public int getIndex(String username) {
        int index = 0;
        TradeData d = this.forName(username);
        if (d != null) {
            for (TradeData data : new ArrayList<TradeData>(accounts)) {
                if (data.username.equalsIgnoreCase(username)) {
                    if (data.muleHandlerName != null && data.muleHandlerName.length() > 0) {
                        return 0;
                    }
                    return index;
                }
                if (data.id != d.id && (d.tradeOnWorld == -1 || data.tradeOnWorld != d.tradeOnWorld)) continue;
                ++index;
            }
        }
        return Integer.MAX_VALUE;
    }
}

