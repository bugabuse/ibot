package com.farm.ibot.proxy;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.Terminal;
import com.farm.ibot.api.util.web.WebClient;
import com.farm.ibot.core.Bot;
import com.farm.ibot.init.ConsoleParams;
import com.farm.ibot.init.Settings;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ProxyManager {
    private static String cachedProxyList = "";
    private static int cachedProxyAmount = -1;
    private static HashMap<String, Integer> proxies = new HashMap();
    public final Bot bot;

    public ProxyManager(Bot bot) {
        this.bot = bot;
        if (proxies.size() == 0) {
            try {
                int index = ConsoleParams.getIntValue("randomproxy");
                if (index > -1) {

                    Iterator var3 = Files.readAllLines(Paths.get(Settings.BOT_DATA_PATH + File.separator + "proxies" + File.separator + "proxies" + index + ".txt")).iterator();

                    while (var3.hasNext()) {
                        String proxy = (String) var3.next();
                        proxies.put(proxy, 0);
                    }
                }
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

    }

    public static boolean containsProxyPort(int port) {
        return cachedProxyList.contains("127.0.0.1:" + port);
    }

    public static int getAvailableTorProxyCount() {
        if (cachedProxyAmount == -1) {
            cachedProxyList = Terminal.execute("docker container ls");
            cachedProxyAmount = StringUtils.countMatches(cachedProxyList, "tor-socks-proxy");
            if (cachedProxyAmount == -1) {
                cachedProxyAmount = 0;
            }
        }

        return cachedProxyAmount;
    }

    public static void restartProxy(Proxy proxy) {
        (new Thread(new ThreadGroup("Restart proxy threadGroup"), () -> {
            WebClient wc = new WebClient();
            wc.setProxy((String) null);
            wc.downloadString("http://" + proxy.host + ":6666/?port=" + proxy.port);
        })).start();
    }

    public void setProxy() {
        if (ConsoleParams.contains("dynamicProxy")) {
            if (this.bot.getSession().getAccount() != null && this.bot.getSession().getAccount().proxy != null && this.bot.getSession().getAccount().proxy.split(":").length > 1) {

                this.setProxy(this.bot.getSession().getAccount().proxy);
                this.bot.proxy.sessionId = this.bot.getSession().getAccount().id;
            } else {
                this.setProxy((String) null);
                this.bot.proxy = null;
            }
        } else if (ConsoleParams.contains("accountDynamicProxy")) {
            if (this.bot.getSession().getAccount() != null && this.bot.getSession().getAccount().proxy.split(":").length > 1) {

                this.setProxy(this.bot.getSession().getAccount().proxy);
                this.bot.proxy.sessionId = this.bot.getSession().getAccount().id;
            }
        } else {
            String port;
            String address;
            if (ConsoleParams.contains("randomproxy")) {
                if (proxies != null && proxies.size() > 0) {
                    if (this.bot.proxy != null) {
                        return;
                    }

                    String bestProxy = "";
                    int best = Integer.MAX_VALUE;
                    Iterator var3 = proxies.entrySet().iterator();

                    while (var3.hasNext()) {
                        Entry<String, Integer> entry = (Entry) var3.next();
                        if ((Integer) entry.getValue() < best) {
                            best = (Integer) entry.getValue();
                            bestProxy = (String) entry.getKey();
                        }
                    }

                    if (bestProxy.length() > 0) {
                        proxies.put(bestProxy, best + 1);
                    }

                    String[] proxyData = bestProxy.split(":");
                    address = proxyData[0];
                    port = proxyData[1];
                    String username = proxyData.length > 2 ? proxyData[2] : "";
                    String password = proxyData.length > 3 ? proxyData[3] : "";


                    this.setProxy(address, Integer.parseInt(port), username, password);
                }
            } else if (ConsoleParams.contains("proxy")) {
                String[] proxyData = ConsoleParams.getValue("proxy").split(":");
                address = proxyData[0];
                port = proxyData[1];
                address = proxyData.length > 2 ? proxyData[2] : "";
                port = proxyData.length > 3 ? proxyData[3] : "";


                this.setProxy(address, Integer.parseInt(port), address, port);
            }
        }

        if (ConsoleParams.contains("torproxy")) {
            if (this.bot.getSession().getAccount() != null && this.bot.getSession().getAccount().proxy.length() > 0) {

                this.setProxy(this.bot.getSession().getAccount().proxy);
                this.bot.proxy.sessionId = this.bot.getSession().getAccount().id;
            } else {
                this.setTorProxy();
            }
        }

    }

    public void setTorProxy() {
        int index = Settings.processPerTab ? Main.getBotProcessIndex() : Main.bots.size();
        int port = 9150;
        if (getAvailableTorProxyCount() > 0) {
            for (port = 9149 + index % (getAvailableTorProxyCount() + 1); !containsProxyPort(port); port = 9149 + index % (getAvailableTorProxyCount() + 1)) {
                ++index;
            }
        }

        this.setProxy("127.0.0.1", port, "", "");
    }

    public void setProxy(String proxyAddress) {
        if (proxyAddress != null) {
            SocksAuthenticator.registerProxyCredentials(proxyAddress);
            this.bot.proxy = new Proxy(proxyAddress);
        }

    }

    public void setProxy(String proxyAddress, int port, String username, String password) {
        SocksAuthenticator.registerProxyCredentials(proxyAddress, "" + port, username, password);
        this.bot.proxy = new Proxy(proxyAddress, port, username, password);
    }
}
