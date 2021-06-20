package com.farm.ibot.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.HashMap;

public class SocksAuthenticator extends Authenticator {
    private static HashMap<String, String[]> proxyAuths = new HashMap();

    public static void registerProxyCredentials(String proxy) {
        if (proxy.split(":").length > 3) {
            String[] proxyData = proxy.split(":");
            String address = proxyData[0];
            String port = proxyData[1];
            String username = proxyData.length > 2 ? proxyData[2] : "";
            String password = proxyData.length > 3 ? proxyData[3] : "";
            if (!proxyAuths.containsKey(address + ":" + port)) {

            }

            registerProxyCredentials(address, port, username, password);
        }
    }

    public static void registerProxyCredentials(String address, String port, String username, String password) {
        proxyAuths.put(address + ":" + port, new String[]{username, password});
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        if (!this.getRequestingHost().contains("95.216.19.23") && !this.getRequestingHost().contains("144.76.41.210")) {
            String[] data = (String[]) proxyAuths.get(this.getRequestingHost() + ":" + this.getRequestingPort());
            return data != null && data.length > 0 ? new PasswordAuthentication(data[0], data[1].toCharArray()) : null;
        } else {
            return new PasswordAuthentication("schabowy", "kotlet".toCharArray());
        }
    }
}
