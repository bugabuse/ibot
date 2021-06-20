package com.farm.botmanager.web;

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
        Map domainStore;
        String domain = this.getDomainFromHost(conn.getURL().getHost());
        if (this.store.containsKey(domain)) {
            domainStore = (Map) this.store.get(domain);
        } else {
            domainStore = new HashMap();
            this.store.put(domain, domainStore);
        }
        String headerName = null;
        int i = 1;
        while ((headerName = conn.getHeaderFieldKey(i)) != null) {
            if (headerName.equalsIgnoreCase(SET_COOKIE)) {
                String token;
                HashMap<String, String> cookie = new HashMap<String, String>();
                StringTokenizer st = new StringTokenizer(conn.getHeaderField(i), COOKIE_VALUE_DELIMITER);
                if (st.hasMoreTokens()) {
                    token = st.nextToken();
                    String name = token.substring(0, token.indexOf(61));
                    String value = token.substring(token.indexOf(61) + 1, token.length());
                    domainStore.put(name, cookie);
                    cookie.put(name, value);
                }
                while (st.hasMoreTokens()) {
                    token = st.nextToken();
                    if (token.indexOf(61) <= -1) continue;
                    cookie.put(token.substring(0, token.indexOf(61)).toLowerCase(), token.substring(token.indexOf(61) + 1, token.length()));
                }
            }
            ++i;
        }
    }

    public void setCookies(URLConnection conn) throws IOException {
        URL url = conn.getURL();
        String domain = this.getDomainFromHost(url.getHost());
        String path = url.getPath();
        Map domainStore = (Map) this.store.get(domain);
        if (domainStore == null) {
            return;
        }
        StringBuffer cookieStringBuffer = new StringBuffer();
        Iterator cookieNames = domainStore.keySet().iterator();
        while (cookieNames.hasNext()) {
            String cookieName = (String) cookieNames.next();
            Map cookie = (Map) domainStore.get(cookieName);
            if (!this.comparePaths((String) cookie.get(PATH), path) || !this.isNotExpired((String) cookie.get(EXPIRES)))
                continue;
            cookieStringBuffer.append(cookieName);
            cookieStringBuffer.append("=");
            cookieStringBuffer.append((String) cookie.get(cookieName));
            if (!cookieNames.hasNext()) continue;
            cookieStringBuffer.append(SET_COOKIE_SEPARATOR);
        }
        try {
            conn.setRequestProperty(COOKIE, cookieStringBuffer.toString());
        } catch (IllegalStateException ise) {
            IOException ioe = new IOException("Illegal State! Cookies cannot be set on a URLConnection that is already connected. Only call setCookies(java.net.URLConnection) AFTER calling java.net.URLConnection.connect().");
            throw ioe;
        }
    }

    public String getDomainFromHost(String host) {
        if (host.indexOf(46) != host.lastIndexOf(46)) {
            return host.substring(host.indexOf(46) + 1);
        }
        return host;
    }

    private boolean isNotExpired(String cookieExpires) {
        if (cookieExpires == null) {
            return true;
        }
        Date now = new Date();
        try {
            return now.compareTo(this.dateFormat.parse(cookieExpires)) <= 0;
        } catch (ParseException pe) {
            pe.printStackTrace();
            return false;
        }
    }

    private boolean comparePaths(String cookiePath, String targetPath) {
        if (cookiePath == null) {
            return true;
        }
        if (cookiePath.equals("/")) {
            return true;
        }
        return targetPath.regionMatches(0, cookiePath, 0, cookiePath.length());
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

    public void setCookie(String domain, String name, String value) {
        Map domainStore;
        if (this.store.containsKey(domain)) {
            domainStore = (Map) this.store.get(domain);
        } else {
            domainStore = new HashMap();
            this.store.put(domain, domainStore);
        }
        HashMap<String, String> cookie = new HashMap<String, String>();
        domainStore.put(name, cookie);
        cookie.put(name, value);
    }

    public String getDomainName(String urlString) {
        try {
            URL url = new URL(urlString);
            return this.getDomainFromHost(url.getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }
}

