/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import com.google.gson.Gson;
import com.farm.server.content.stats.AccountStats;
import com.farm.server.content.trade.AccountData;
import com.farm.server.core.util.FileSave;
import com.farm.server.core.util.FileUtils;
import com.farm.server.core.util.RequestUtils;
import com.farm.server.core.util.StringUtils;
import com.farm.server.rest.AccountsController;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;

@RestController
public class StatsController {
    private static Logger logger = Logger.getLogger(StatsController.class.getName());
    private static ConcurrentHashMap<String, AccountStats> accounts = new ConcurrentHashMap();
    private static ConcurrentHashMap<String, Integer> oneHourBanCache = new ConcurrentHashMap();
    private static long oneHourBanCacheTime = 0L;
    private static long lastUpdate = 0L;

    public static Collection<AccountStats> getAccounts() {
        return accounts.values();
    }

    @PostConstruct
    public void onInitialize() throws IOException {
        StatsController.load();
    }

    public Stream<AccountStats> getBanStream(int hour) {
        return StatsController.getAccounts().stream().filter(o -> o.getOnlineTimeMs() > 0L && System.currentTimeMillis() - o.banTime < (long)(3600 * hour * 1000));
    }

    @RequestMapping(value={"/ip"})
    public String ipAddress(HttpServletRequest request) throws IOException {
        return RequestUtils.getIpAddress(request);
    }

    @RequestMapping(value={"/accounts/stats"})
    public String stats(@RequestParam(value="sort", required=false) String sort) throws IOException {
        StatsController.getAccounts().removeIf(Objects::isNull);
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("Bany - ostatnie 1h: ").append(this.getBanStream(1).count());
        sb.append(" (").append(this.getBanStream(1).filter(o -> o.flaggedAsStolen).count());
        sb.append(")");
        sb.append("<br>");
        sb.append("Bany - ostatnie 3h: ").append(this.getBanStream(3).count());
        sb.append(" (").append(this.getBanStream(3).filter(o -> o.flaggedAsStolen).count());
        sb.append(")");
        sb.append("<br>");
        sb.append("Bany - ostatnie 12h: ").append(this.getBanStream(12).count());
        sb.append(" (").append(this.getBanStream(12).filter(o -> o.flaggedAsStolen).count());
        sb.append(")");
        sb.append("<br>");
        sb.append("Bany - ostatnie 24h: ").append(this.getBanStream(24).count());
        sb.append(" (").append(this.getBanStream(24).filter(o -> o.flaggedAsStolen).count());
        sb.append(")");
        sb.append("<br>");
        sb.append("Bany - ostatnie 7 dni: ").append(this.getBanStream(168).count());
        sb.append(" (").append(this.getBanStream(168).filter(o -> o.flaggedAsStolen).count());
        sb.append(")");
        sb.append("<br>");
        sb.append("Bany - poprzedni tydzien: ").append(StatsController.getAccounts().stream().filter(o -> o.getOnlineTimeMs() > 0L && System.currentTimeMillis() - o.banTime > 604800000L && System.currentTimeMillis() - o.banTime < 1209600000L).count());
        sb.append("<br>");
        sb.append("Sredni czas zycia bota - ostatnie 24h: ").append(StringUtils.convertMillisToString((long)StatsController.getAccounts().stream().filter(o -> o.getOnlineTimeMs() > 0L && o.banTime > 0L && System.currentTimeMillis() - o.banTime < 86400000L).mapToLong(AccountStats::getOnlineTimeMs).average().orElse(0.0)));
        sb.append("<br>");
        sb.append("Sredni czas zycia bota - ostatnie 7 dni: ").append(StringUtils.convertMillisToString((long)StatsController.getAccounts().stream().filter(o -> o.getOnlineTimeMs() > 0L && o.banTime > 0L && System.currentTimeMillis() - o.banTime < 604800000L).mapToLong(AccountStats::getOnlineTimeMs).average().orElse(0.0)));
        sb.append("<br>");
        sb.append("Sredni czas zycia bota - poprzedni tydzien: ").append(StringUtils.convertMillisToString((long)StatsController.getAccounts().stream().filter(o -> o.getOnlineTimeMs() > 0L && o.banTime > 0L && System.currentTimeMillis() - o.banTime > 604800000L && System.currentTimeMillis() - o.banTime < 1209600000L).mapToLong(o -> o.getOnlineTimeMs()).average().orElse(0.0)));
        sb.append("<br>");
        sb.append("<br>");
        sb.append("Bany - top adresy IP<br>");
        sb.append("<style>table { border-collapse: collapse;} table, th, td { border: 1px solid black;}</style>");
        sb.append("<table>");
        sb.append("<th>Adres IP</th><th>Ilosc banow(24h)</th><th>Ilosc banow(1h)</th>");
        HashMap<String, Integer> bannedAddressMap = new HashMap<String, Integer>();
        for (AccountStats data : StatsController.getAccounts()) {
            if (data.getOnlineTimeMs() <= 0L || data.banTime <= 0L || System.currentTimeMillis() - data.banTime >= 86400000L) continue;
            bannedAddressMap.put(data.ipAddress, bannedAddressMap.getOrDefault(data.ipAddress, 0) + 1);
        }
        for (Map.Entry entry : bannedAddressMap.entrySet()) {
            try {
                sb.append("<tr>");
                sb.append("<th>").append(entry.getKey()).append("</th><th>").append(bannedAddressMap.get(entry.getKey())).append("</th><th>").append(oneHourBanCache.get(entry.getKey())).append("</th>");
                sb.append("</tr>");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("<br>");
        sb.append("<br>");
        sb.append("Sort by: ");
        sb.append("<a href = '/accounts/stats?sort=bantime'>Ban time</a>   ");
        sb.append("<a href = '/accounts/stats?sort=playduration'>Alive time</a>");
        sb.append("<br>");
        sb.append("<style>table { border-collapse: collapse;} table, th, td { border: 1px solid black;}</style>");
        sb.append("<table>");
        sb.append("<th>Username</th><th>Czas online</th><th>Typ bana</th><th>Kiedy</th>");
        ArrayList<AccountStats> accounts = new ArrayList<AccountStats>(StatsController.getAccounts());
        if (sort != null) {
            if (sort.contains("bantime")) {
                Collections.sort(accounts, Comparator.comparingLong(o -> o.banTime));
            }
            if (sort.contains("playduration")) {
                Collections.sort(accounts, Comparator.comparingLong(AccountStats::getOnlineTimeMs));
            }
        }
        for (AccountStats data : accounts) {
            if (!StatsController.isRecent(data) || data.getOnlineTimeMs() <= 0L) continue;
            sb.append("<tr>");
            String color = !data.isBanned() ? "green" : "red";
            sb.append("<th><font color='").append(color).append("'>").append(data.username).append("</font>").append("</th><th>").append(StringUtils.convertMillisToString(data.getOnlineTimeMs())).append("</th><th>").append(data.getScriptName()).append("<th>").append(data.flaggedAsStolen ? "Locked" : "Disabled").append("</th>").append("<th>").append(StringUtils.convertMillisToPastStringNicely(data.banTime)).append("</th>");
            sb.append("</tr>");
        }
        return sb.toString();
    }

    public static int getBansOnIpAddress(String ipAddress) {
        if (System.currentTimeMillis() - oneHourBanCacheTime > 15000L) {
            oneHourBanCache.clear();
            for (AccountStats data : StatsController.getAccounts()) {
                if (data.getOnlineTimeMs() > 0L && data.banTime > 0L && System.currentTimeMillis() - data.banTime < 3600000L) {
                    oneHourBanCache.put(data.ipAddress, oneHourBanCache.getOrDefault(data.ipAddress, 0) + 1);
                }
                oneHourBanCacheTime = System.currentTimeMillis();
            }
        }
        return oneHourBanCache.getOrDefault(ipAddress, 0);
    }

    @RequestMapping(value={"/accounts/stats/updateall"})
    public String update() {
        for (AccountData data : AccountsController.getAccounts()) {
            AccountStats stats = StatsController.forName(data.username);
            if (stats == null || stats.isBanned() || !data.isBanned) continue;
            stats.banTime = data.lastUpdate;
            stats.flaggedAsStolen = data.isFlaggedAsStolen;
        }
        return "Thanks";
    }

    @RequestMapping(value={"/accounts/stats/playtime"})
    public long getTime(String username, long lastHours, long lastMinutes) {
        AccountStats stats = StatsController.forName(username);
        return stats != null ? stats.getOnlineTimeMs(lastHours * 3600L * 1000L + lastMinutes * 60L * 1000L) : 0L;
    }

    public static void update(AccountData data) {
        if (data.username.contains("world-lock")) {
            return;
        }
        AccountStats stats = StatsController.forName(data.username);
        if (stats == null) {
            stats = new AccountStats(data.username);
            accounts.put(data.username, stats);
        }
        stats.setScriptName(data.currentScript);
        if (data.isBanned != stats.isBanned()) {
            stats.banTime = System.currentTimeMillis();
            stats.flaggedAsStolen = data.isFlaggedAsStolen;
            stats.ipAddress = data.hostName;
        } else if (data.isLoggedIn) {
            stats.updateRecord();
        }
        try {
            StatsController.save();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AccountStats forName(String name) {
        return accounts.get(name);
    }

    private static void load() throws IOException {
        File file;
        if (StatsController.getAccounts().size() == 0 && (file = FileUtils.getFile("files/AccountStats.json")).exists()) {
            
            FileReader reader = new FileReader(file);
            AccountStats[] accounts = (AccountStats[])new Gson().fromJson((Reader)reader, AccountStats[].class);
            reader.close();
            if (accounts != null) {
                for (AccountStats stats : accounts) {
                    StatsController.accounts.put(stats.username, stats);
                }
            }

            logger.info(StatsController.accounts.size() + " Loaded account stats.");
        }
    }

    private static boolean isRecent(AccountStats stats) {
        return System.currentTimeMillis() - stats.getLastRecordTime() < 259200000L;
    }

    private static void save() {
        if (System.currentTimeMillis() - lastUpdate > 300000L) {
            lastUpdate = System.currentTimeMillis();
            try {
                if (StatsController.getAccounts().size() > 0) {
                    File file = FileUtils.getFile("files/AccountStats.json");
                    FileSave.serialize(file, StatsController.getAccounts());
                    lastUpdate = System.currentTimeMillis();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

