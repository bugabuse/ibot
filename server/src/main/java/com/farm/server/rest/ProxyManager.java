package com.farm.server.rest;

import com.farm.server.content.trade.AccountData;
import com.farm.server.core.ProxyRecord;
import com.farm.server.core.util.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@RestController
public class ProxyManager {
    private static Logger logger = Logger.getLogger(ProxyManager.class.getName());
    public static ConcurrentHashMap<String, Long> flaggedProxies = new ConcurrentHashMap();
    public static CopyOnWriteArrayList<String> proxies = new CopyOnWriteArrayList();
    public static CopyOnWriteArrayList<ProxyRecord> proxyRecords = new CopyOnWriteArrayList();

    @PostConstruct
    public void onInitialize() throws IOException {
        this.load();
    }

    @RequestMapping(value={"proxy/update"}, method={RequestMethod.POST})
    public String addRecord(String jsonObject) {
        ProxyRecord[] records;
        for (ProxyRecord record : records = (ProxyRecord[])AccountsController.GSON.fromJson(jsonObject, ProxyRecord[].class)) {
            ProxyRecord oldRecord = ProxyManager.findProxyRecord(record.sessionId);
            if (oldRecord != null) {
                oldRecord.lastUpdate = System.currentTimeMillis();
                continue;
            }
            proxyRecords.add(record);
            record.lastUpdate = System.currentTimeMillis();
        }
        return "OK";
    }

    @RequestMapping(value={"proxy/assign"})
    public ProxyRecord assignProxy() {
        this.update();
        String bestProxy = null;
        if (proxies.size() > 500) {
            bestProxy = proxies.get(new Random().nextInt(proxies.size()));
            return ProxyManager.addRecord(bestProxy, new Random().nextInt(Integer.MAX_VALUE));
        }
        int bestAmount = 10000;
        for (String proxy : proxies) {
            int amount;
            if (ProxyManager.isFlagged(proxy) || StatsController.getBansOnIpAddress(proxy) > 2 || (amount = ProxyManager.findProxyRecords(proxy).size()) > WebConfigController.getInt("max_accounts_per_proxy") || amount >= bestAmount) continue;
            bestProxy = proxy;
            bestAmount = amount;
        }
        if (bestProxy != null) {
            return ProxyManager.addRecord(bestProxy, new Random().nextInt(Integer.MAX_VALUE));
        }
        return null;
    }

    @RequestMapping(value={"proxy/list"})
    public String listProxies() throws IOException {
        this.update();
        String str = "";
        for (String proxy : proxies) {
            int banAmount = StatsController.getBansOnIpAddress(proxy);
            str = str + proxy + " | " + ProxyManager.findProxyRecords(proxy).size() + (ProxyManager.isFlagged(proxy) ? " | FLAGGED(" + ProxyManager.getFlaggedMinutesLeft(proxy) + ")" : "") + (banAmount > 2 ? " | BANED(1hr): " + banAmount : "") + "<br>";
        }
        return str;
    }

    @RequestMapping(value={"proxy/flagged"})
    public boolean isProxyFlagged(String proxy) {
        return ProxyManager.isFlagged(proxy);
    }

    @RequestMapping(value={"proxy/records"})
    public List<ProxyRecord> listRecords() {
        this.update();
        return proxyRecords;
    }

    @RequestMapping(value={"proxy/flag"})
    public String doProxyFlag(String proxy) {
        ProxyManager.flagProxy(proxy, 20);
        return "OK";
    }

    @RequestMapping(value={"proxy/flagshort"})
    public String doProxyFlagShort(String proxy) {
        ProxyManager.flagProxy(proxy, 3);
        return "OK";
    }

    public static ProxyRecord findProxyRecord(int sessionId) {
        return proxyRecords.stream().filter(r -> r.sessionId == sessionId).findAny().orElse(null);
    }

    public static ArrayList<AccountData> getOnlineAccountsWithProxy(String proxy) {
        ArrayList<AccountData> temp = new ArrayList<AccountData>();
        for (AccountData record : new ArrayList<AccountData>(AccountsController.getOnlineAccounts())) {
            if (!record.proxy.equals(proxy)) continue;
            temp.add(record);
        }
        return temp;
    }

    public static ArrayList<ProxyRecord> findProxyRecords(String proxy) {
        ArrayList<ProxyRecord> temp = new ArrayList<ProxyRecord>();
        for (ProxyRecord record : new ArrayList<ProxyRecord>(proxyRecords)) {
            if (!record.proxy.equals(proxy)) continue;
            temp.add(record);
        }
        return temp;
    }

    public static String proxyForIp(String ip) {
        for (String proxy : proxies) {
            if (!proxy.contains(ip + ":")) continue;
            return proxy;
        }
        return null;
    }

    public static String findProxy(String ip) {
        for (String proxy : proxies) {
            if (!proxy.contains(ip)) continue;
            return proxy;
        }
        return null;
    }

    private void load() throws IOException {
        if (proxies.size() == 0) {
            try{
                File file = FileUtils.getFile("files/proxies.txt");
                for (String proxy : Files.readAllLines(file.toPath())) {
                    proxies.add(proxy);
                }
                logger.info(proxies.size() + " Proxies loaded.");
            }
            catch(Exception e){
                logger.warning("error loading proxies");
            }

        }
    }

    public static ProxyRecord addRecord(String proxy, int id) {
        if (ProxyManager.findProxyRecord(id) == null) {
            ProxyRecord record = new ProxyRecord();
            record.sessionId = id;
            record.proxy = proxy;
            record.lastUpdate = System.currentTimeMillis();
            proxyRecords.add(record);
            return record;
        }
        return null;
    }

    public void update() {
        for (ProxyRecord record : new ArrayList<ProxyRecord>(proxyRecords)) {
            if (record.isOnline()) continue;
            proxyRecords.remove(record);
        }
    }

    public static void flagProxy(String proxy, int minutes) {
        flaggedProxies.put(proxy, System.currentTimeMillis() + (long)(minutes * 60 * 1000));
    }

    public static boolean isFlagged(String proxy) {
        return System.currentTimeMillis() < flaggedProxies.getOrDefault(proxy, 0L);
    }

    public static int getFlaggedMinutesLeft(String proxy) {
        if (System.currentTimeMillis() < flaggedProxies.getOrDefault(proxy, 0L)) {
            return (int)((flaggedProxies.getOrDefault(proxy, 0L) - System.currentTimeMillis()) / 60000L);
        }
        return 0;
    }
}

