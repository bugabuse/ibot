/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.gson.Gson
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestMethod
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.farm.server.content.trade.AccountData;
import com.farm.server.core.util.FileSave;
import com.farm.server.core.util.FileUtils;
import com.farm.server.core.util.RequestUtils;
import com.farm.server.core.util.StringUtils;
import com.farm.server.rest.ProxyManager;
import com.farm.server.rest.StatsController;
import com.farm.server.rest.WebConfigController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

@RestController
public class AccountsController {
	private static Logger logger = Logger.getLogger(AccountsController.class.getName());
    private static final String[] ITEMS_TO_COUNT = new String[]{"Logs", "Mined", "Jugs", "Bones", "Fishes", "Strings", "Planks", "MyBots"};
    private static ConcurrentHashMap<String, AccountData> accounts = new ConcurrentHashMap();
    public static final Gson GSON = new Gson();
    private long lastUpdate = 0L;

    public static List<AccountData> getOnlineAccounts() {
        return AccountsController.getAccounts().stream().filter(AccountData::isOnline).collect(Collectors.toList());
    }

    public static Collection<AccountData> getAccounts() {
        return accounts.values();
    }

    @PostConstruct
    public void onInitialize() throws IOException {
        this.load();
    }

    @RequestMapping(value={"/account/terminate"})
    public String add(@RequestParam(value="accounts") String account, int minutes) {
        AccountData accountData = AccountsController.forName(account);
        if (accountData != null) {
            accountData.terminatedUntil = System.currentTimeMillis() + 60000L * (long)minutes;
            return "Terminated until " + accountData.terminatedUntil;
        }
        return "Acc not found.";
    }

    @RequestMapping(value={"/accounts/add"})
    public String add(@RequestParam(value="accounts") String accounts, @RequestParam(value="emailConfirmed", required=false) boolean emailConfirmed, @RequestParam(value="script", required=false) String script, @RequestParam(value="password", required=false) String password, @RequestParam(value="hostname", required=false) String hostname, @RequestParam(value="proxy", required=false) String proxy, @RequestParam(value="twoFactorSecretKey", required=false) String twoFactorSecretKey, HttpServletRequest request) throws IOException {
        String ipAddress = RequestUtils.getIpAddress(request);
        this.addAccounts(accounts, emailConfirmed, script, password, ipAddress, hostname, proxy, twoFactorSecretKey, false);
        try {
            this.save();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "Thanks";
    }

    @RequestMapping(value={"/account/set2fa"})
    public String setTwoFactor(String email, String twoFactorSecretKey) throws IOException {
        AccountData data = AccountsController.forName(email);
        if (data != null) {
            data.twoFactorSecretKey = twoFactorSecretKey;
        }
        try {
            this.save();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "Thanks";
    }

    @RequestMapping(value={"/account/set"})
    public String put(@RequestParam(value="accounts") String accounts, @RequestParam(value="emailConfirmed", required=false) boolean emailConfirmed, @RequestParam(value="script", required=false) String script, @RequestParam(value="password", required=false) String password, @RequestParam(value="hostname", required=false) String hostname, @RequestParam(value="proxy", required=false) String proxy, @RequestParam(value="twoFactorSecretKey", required=false) String twoFactorSecretKey, HttpServletRequest request) throws IOException {
        String ipAddress = RequestUtils.getIpAddress(request);
        this.addAccounts(accounts, emailConfirmed, script, password, ipAddress, hostname, proxy, twoFactorSecretKey, true);
        try {
            this.save();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "Thanks";
    }

    @RequestMapping(value={"/accounts/bind"})
    public AccountData bind(String username, String script) throws IOException {
        AccountData data = AccountsController.forName(username);
        if (data != null) {
            data.autostartScript = script;
            data.uniqueScriptId = (int)AccountsController.getAccounts().stream().filter(a -> Objects.equals(a.autostartScript, data.autostartScript)).count();
            this.save();
            return data;
        }
        return null;
    }

    @RequestMapping(value={"/accounts/reset"})
    public String reset() throws IOException {
        for (AccountData data : AccountsController.getAccounts()) {
            if (data.autostartScript == null || !data.autostartScript.contains("@gmail")) continue;
            data.autostartScript = "FreeAgent";
        }
        this.save();
        return "Thanks";
    }

    @RequestMapping(value={"/accounts/onlinejson"})
    public ArrayList<AccountData> onlineJson() throws IOException {
        ArrayList<AccountData> temp = new ArrayList<AccountData>();
        for (AccountData data : new ArrayList<AccountData>(AccountsController.getAccounts())) {
            if (!data.isOnline()) continue;
            temp.add(data);
        }
        return temp;
    }

    @RequestMapping(value={"/accounts/accounts"})
    public Collection<AccountData> allAccounts() throws IOException {
        return AccountsController.getAccounts();
    }

    @RequestMapping(value={"/accounts/update"}, method={RequestMethod.POST})
    public String allAccounts(String jsonObject) throws IOException {
        AccountData[] accountsLocal;
        long start = System.currentTimeMillis();
        for (AccountData account : accountsLocal = (AccountData[])GSON.fromJson(jsonObject, AccountData[].class)) {
            if (account == null || account.username == null) continue;
            AccountData current = AccountsController.forName(account.username);
            account.lastUpdate = System.currentTimeMillis();
            if (current != null) {
                current.setAs(account);
            } else {
                accounts.put(account.username, account);
            }
            StatsController.update(account);
        }
        try {
            this.save();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    @RequestMapping(value={"/accounts/setoffline"}, method={RequestMethod.POST})
    public String setOffline(String jsonObject) throws IOException {
        AccountData[] accountsLocal;
        long start = System.currentTimeMillis();
        for (AccountData account : accountsLocal = (AccountData[])GSON.fromJson(jsonObject, AccountData[].class)) {
            if (account == null || account.username == null) continue;
            AccountData current = AccountsController.forName(account.username);
            account.lastUpdate = 0L;
            if (current != null) {
                current.setAs(account);
            } else {
                accounts.put(account.username, account);
            }
            StatsController.update(account);
        }
        try {
            this.save();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    @RequestMapping(value={"/accounts/available"})
    public AccountData availableAccount(String script, @RequestParam(value="useDynamicProxy", required=false) boolean useDynamicProxy, @RequestParam(value="useAccountDynamicProxy", required=false) boolean useAccountDynamicProxy) throws IOException {
        AccountData[] acc = new AccountData[]{null};
        ArrayList<AccountData> accs = new ArrayList<AccountData>(AccountsController.getAccounts());
        for (AccountData account : accs) {
            if (account == null || account.username == null) continue;
            if (account.isTerminated()) {
                logger.info(String.format("%s is terminated.", account.username));
                continue;
            }
            if (account.isBanned) {
                logger.info(String.format("%s is banned.", account.username));
                continue;
            }
            if (account.autostartScript == null || !account.autostartScript.equalsIgnoreCase(script) || account.isOnline()) continue;
            if (useDynamicProxy) {
                if (Strings.isNullOrEmpty((String)account.proxy) || account.proxy.contains("null")) {
                    logger.info(String.format("%s, proxy is null.", account.username));
                    continue;
                }
                if (!useAccountDynamicProxy && ProxyManager.findProxyRecords(account.proxy).size() > WebConfigController.getInt("max_accounts_per_proxy")) {
                    logger.info(String.format("%s, proxy limit hit.", account.username));
                    continue;
                }
            }
            acc[0] = account;
            break;
        }
        if (acc[0] != null) {
            acc[0].lastUpdate = System.currentTimeMillis() + 10000L;
            if (useDynamicProxy) {
                ProxyManager.addRecord(acc[0].proxy, acc[0].id);
            }
        }
        try {
            this.save();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return acc[0];
    }

    @RequestMapping(value={"/accounts/unban"})
    public String onlineAccounts(String email, String newPassword) throws IOException {
        AccountData data = AccountsController.forName(email);
        if (data != null) {
            data.isBanned = false;
            data.isFlaggedAsStolen = false;
            data.password = newPassword;
            return "Unbanned.";
        }
        return "Cannot find account.";
    }

    @RequestMapping(value={"/accounts/online"})
    public String onlineAccounts() throws IOException {
        long start = System.currentTimeMillis();
        this.update();
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");
        sb.append("<style>table { border-collapse: collapse;} table, th, td { border: 1px solid black;}</style>");
        sb.append("<a href='../index.html'>OSRSBot Panel</a><br>");
        sb.append("<a href='../webconfig/?get=all'>WebConfig</a><br><br>");
        sb.append("<pre>");
        int online = 0;
        for (AccountData data : new ArrayList<AccountData>(AccountsController.getAccounts())) {
            if (!data.isOnline()) continue;
            ++online;
        }
        sb.append("Online: <font color='green'><b>" + online + "</b></font><br><br>");
        sb.append("@@@Stats@@@");
        sb.append("<table>");
        ArrayList<AccountData> accs = new ArrayList<AccountData>(AccountsController.getAccounts());
        Collections.sort(accs, Comparator.comparing(a -> Strings.nullToEmpty((String)a.currentScript)));
        for (AccountData data : accs) {
            if (!data.isOnline()) continue;
            sb.append("<tr>");
            String color = data.isLoggedIn ? "green" : "red";
            sb.append("<th><font color='" + color + "'>" + data.getGameUsername() + "</font></th><th>w" + data.world + "</th><th>" + data.description + "</th>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("</pre>");
        sb.append("</body>");
        sb.append("</html>");
        String onlineData = sb.toString();
        String statsString = "";
        for (String item : ITEMS_TO_COUNT) {
            String str = AccountsController.getTotalItemData(item, onlineData);
            if (str == null || str.equals("null")) continue;
            statsString = statsString + str + "<br>";
        }
        onlineData = onlineData.replace("@@@Stats@@@", statsString);
        return onlineData;
    }

    private void addAccounts(String accounts, boolean emailConfirmed, String script, String password, String ipAddress, String hostName, String proxy, String twoFactorSecretKey, boolean forceUpdate) {
        for (String accountName : accounts.split("\\|")) {
            AccountData data = new AccountData(accountName, 301, "");
            data.emailConfirmed = emailConfirmed;
            data.autostartScript = script;
            data.registeredIpAddress = ipAddress;
            data.accountCreationTime = System.currentTimeMillis();
            data.lastIpAddress = ipAddress;
            data.hostName = hostName;
            data.proxy = proxy;
            data.twoFactorSecretKey = twoFactorSecretKey;
            data.uniqueScriptId = (int)AccountsController.getAccounts().stream().filter(a -> Objects.equals(a.autostartScript, data.autostartScript)).count();
            data.id = AccountsController.accounts.size();
            if (accountName.length() <= 1 || !forceUpdate && AccountsController.forName(accountName) != null) continue;
            data.isBanned = false;
            data.password = password;
            data.preferredWorld = 301;
            AccountsController.accounts.put(data.username, data);
        }
    }

    public static AccountData forName(String name) {
        return accounts.get(name);
    }

    public static AccountData forGameUsername(String name) {
        return AccountsController.getAccounts().stream().filter(a -> a.gameUsername != null && a.gameUsername.equalsIgnoreCase(name)).findAny().orElse(null);
    }

    void update() {
        for (AccountData account : new ArrayList<AccountData>(AccountsController.getAccounts())) {
            if (account.isOnline()) continue;
            account.description = "";
        }
    }

    private void load() throws IOException {
        File file;
        if (accounts.size() == 0 && (file = FileUtils.getFile("files/Accounts.json")).exists()) {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            AccountData[] accounts = (AccountData[])GSON.fromJson((Reader)reader, AccountData[].class);
            reader.close();

            for (AccountData newAccount : accounts) {
                if (AccountsController.accounts.containsKey(newAccount.username)) continue;
                newAccount.clearData();
                AccountsController.accounts.put(newAccount.username, newAccount);
            }

            logger.info(String.format("Loaded %s accounts",AccountsController.accounts.size()));
        }
        ArrayList<AccountData> temp = new ArrayList<AccountData>();
        int i = 0;
        for (AccountData acc : AccountsController.getAccounts()) {
            acc.id = i++;
            acc.uniqueScriptId = new Random().nextInt(Integer.MAX_VALUE);
            temp.add(acc);
        }
    }

    private void save() throws IOException {
        if (System.currentTimeMillis() - this.lastUpdate > 90000L) {
            this.lastUpdate = System.currentTimeMillis();
            File file = FileUtils.getFile("files/Accounts.json");
            ArrayList<AccountData> accs = new ArrayList<AccountData>();
            for (AccountData account : new ArrayList<AccountData>(AccountsController.getAccounts())) {
                AccountData newAcc = new AccountData("", 0, "");
                newAcc.setAs(account);
                newAcc.description = "";
                if (accs.contains(newAcc) || newAcc.isBanned) continue;
                accs.add(newAcc);
            }
            FileSave.serialize(file, accs);
            this.lastUpdate = System.currentTimeMillis();
        }
    }

    public static String getTotalItemData(String itemName, String input) {
        input = input.replace(" ", "").replace("</b>", "").replace("<b>", "");
        String[] strings = StringUtils.substringsBetween(input, itemName + ":", ")");
        int i1 = 0;
        int i2 = 0;
        if (strings != null) {
            for (String str : strings) {
                str = str.replaceAll(" ", "");
                i1 += Integer.parseInt(str.split("\\(")[0]);
                i2 += Integer.parseInt(str.split("\\(")[1]);
            }
        } else {
            return null;
        }
        return itemName + ":" + i1 + "(" + i2 + ") avg. " + i1 / strings.length + "(" + i2 / strings.length + ")";
    }
}

