package com.farm.ibot.proxy;

import com.farm.ibot.api.util.Random;

public class Proxy {
    public int port;
    public String host;
    public String username;
    public String password;
    public int sessionId = Random.next(0, Integer.MAX_VALUE);

    public Proxy(String host, int port, String username, String password) {
        SocksAuthenticator.registerProxyCredentials(host, "" + port, username, password);
        this.port = port;
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public Proxy(String proxy) {
        SocksAuthenticator.registerProxyCredentials(proxy);
        String[] proxyData = proxy.split(":");
        this.host = proxyData[0];
        this.port = Integer.parseInt(proxyData[1].trim());
        this.username = proxyData.length > 2 ? proxyData[2] : "";
        this.password = proxyData.length > 3 ? proxyData[3] : "";
    }

    public String toString() {
        return this.getName();
    }

    public String getName() {
        return this.username != null ? this.host + ":" + this.port + ":" + this.username + ":" + this.password : this.host + ":" + this.port;
    }

    public String getSimpleName() {
        return this.host + ":" + this.port;
    }
}
