package com.farm.ibot.api.util.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CookieManager {
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String COOKIE_VALUE_DELIMITER = ";";
    private static final String PATH = "path";
    private static final String EXPIRES = "expires";
    private static final String DATE_FORMAT = "EEE, dd-MMM-yyyy hh:mm:ss z";
    private static final String SET_COOKIE_SEPARATOR = "; ";
    private static final String COOKIE = "Cookie";
    private static final char NAME_VALUE_SEPARATOR = '=';
    private static final char DOT = '.';
    private Map store = new HashMap();
    private DateFormat dateFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy hh:mm:ss z");

    public void storeCookies(URLConnection conn) throws IOException {
        String domain = this.getDomainFromHost(conn.getURL().getHost());
        Object domainStore;
        if (this.store.containsKey(domain)) {
            domainStore = (Map) this.store.get(domain);
        } else {
            domainStore = new HashMap();
            this.store.put(domain, domainStore);
        }

        String headerName = null;

        for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; ++i) {
            if (headerName.equalsIgnoreCase("Set-Cookie")) {
                Map cookie = new HashMap();
                StringTokenizer st = new StringTokenizer(conn.getHeaderField(i), ";");
                String token;
                if (st.hasMoreTokens()) {
                    token = st.nextToken();
                    String name = token.substring(0, token.indexOf(61));
                    String value = token.substring(token.indexOf(61) + 1, token.length());
                    ((Map) domainStore).put(name, cookie);
                    cookie.put(name, value);
                }

                while (st.hasMoreTokens()) {
                    token = st.nextToken();
                    if (token.indexOf(61) > -1) {
                        cookie.put(token.substring(0, token.indexOf(61)).toLowerCase(), token.substring(token.indexOf(61) + 1, token.length()));
                    }
                }
            }
        }

    }

    public void setCookies(URLConnection conn) throws IOException {
        conn.setRequestProperty("Cookie", this.getCookies(conn.getURL()));
    }

    public String getCookies(URL url) throws IOException {
        String domain = this.getDomainFromHost(url.getHost());
        String path = url.getPath();
        Map domainStore = (Map) this.store.get(domain);
        if (domainStore == null) {
            return null;
        } else {
            StringBuffer cookieStringBuffer = new StringBuffer();
            Iterator cookieNames = domainStore.keySet().iterator();

            while (cookieNames.hasNext()) {
                String cookieName = (String) cookieNames.next();
                Map cookie = (Map) domainStore.get(cookieName);
                if (this.comparePaths((String) cookie.get("path"), path) && this.isNotExpired((String) cookie.get("expires"))) {
                    cookieStringBuffer.append(cookieName);
                    cookieStringBuffer.append("=");
                    cookieStringBuffer.append((String) cookie.get(cookieName));
                    if (cookieNames.hasNext()) {
                        cookieStringBuffer.append("; ");
                    }
                }
            }

            try {
                return cookieStringBuffer.toString();
            } catch (IllegalStateException var9) {
                IOException ioe = new IOException("Illegal State! Cookies cannot be set on a URLConnection that is already connected. Only call setCookies(java.net.URLConnection) AFTER calling java.net.URLConnection.connect().");
                throw ioe;
            }
        }
    }

    public String getDomainFromHost(String host) {
        return host.indexOf(46) != host.lastIndexOf(46) ? host.substring(host.indexOf(46) + 1) : host;
    }

    private boolean isNotExpired(String cookieExpires) {
        if (cookieExpires == null) {
            return true;
        } else {
            Date now = new Date();

            try {
                return now.compareTo(this.dateFormat.parse(cookieExpires)) <= 0;
            } catch (ParseException var4) {
                var4.printStackTrace();
                return false;
            }
        }
    }

    private boolean comparePaths(String cookiePath, String targetPath) {
        if (cookiePath == null) {
            return true;
        } else if (cookiePath.equals("/")) {
            return true;
        } else {
            return targetPath.regionMatches(0, cookiePath, 0, cookiePath.length());
        }
    }

    public String toString() {
        return this.store.toString();
    }

    public Map getStore() {
        return this.store;
    }

    public String getCookie(String domain, String cookie) {
        return ((Map) this.store.get(domain)).get(cookie) != null ? (String) ((Map) ((Map) this.store.get(domain)).get(cookie)).get(cookie) : "";
    }

    public String getDomainName(String urlString) {
        try {
            URL url = new URL(urlString);
            return this.getDomainFromHost(url.getHost());
        } catch (MalformedURLException var3) {
            var3.printStackTrace();
            return "";
        }
    }

    public void setCookie(String domain, String name, String value) {
        Object domainStore;
        if (this.store.containsKey(domain)) {
            domainStore = (Map) this.store.get(domain);
        } else {
            domainStore = new HashMap();
            this.store.put(domain, domainStore);
        }

        Map cookie = new HashMap();
        ((Map) domainStore).put(name, cookie);
        cookie.put(name, value);
    }
}
