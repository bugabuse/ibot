package com.farm.anticaptcha;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AntigateHttpRequest {
    private String url;
    private String postRaw;
    private Integer timeout = 60000;
    private Integer maxBodySize = 0;
    private boolean followRedirects = true;
    private boolean validateTLSCertificates = false;
    private Map<String, String> proxy = null;
    private Map<String, String> cookies = new HashMap();
    private Map<String, String> headers = new HashMap<String, String>() {
        {
            this.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            this.put("Accept-Encoding", "gzip, deflate, sdch");
            this.put("Accept-Language", "ru-RU,en;q=0.8,ru;q=0.6");
        }
    };
    private boolean noCache = false;
    private Set<Integer> acceptedHttpCodes = new HashSet<Integer>() {
        {
            this.add(200);
        }
    };
    private String urlCuttedForHash;
    private String[] urlChangingParts = new String[]{"session_id", "sessionid", "timestamp"};

    AntigateHttpRequest(String url) {
        this.url = url;
    }

    boolean isValidateTLSCertificates() {
        return this.validateTLSCertificates;
    }

    public void setValidateTLSCertificates(boolean validateTLSCertificates) {
        this.validateTLSCertificates = validateTLSCertificates;
    }

    String getUrl() {
        return this.url;
    }

    String getRawPost() {
        return this.postRaw;
    }

    public void setRawPost(String post) {
        this.postRaw = post;
    }

    Map<String, String> getProxy() {
        return this.proxy;
    }

    Integer getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getReferer() {
        return this.headers.get("Referer") != null ? (String) this.headers.get("Referer") : null;
    }

    public void setReferer(String referer) {
        this.headers.put("Referer", referer);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public Map<String, String> getCookies() {
        return this.cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Set<Integer> getAcceptedHttpCodes() {
        return this.acceptedHttpCodes;
    }

    public boolean isNoCache() {
        return this.noCache;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    public boolean isFollowRedirects() {
        return this.followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public Integer getMaxBodySize() {
        return this.maxBodySize;
    }

    public void setMaxBodySize(Integer maxBodySize) {
        this.maxBodySize = maxBodySize;
    }

    public String getUrlWithoutChangingParts(String url) throws Exception {
        String newUrl = url = url.toLowerCase();
        String[] var3 = this.urlChangingParts;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String partToRemove = var3[var5];
            String[] splitted = newUrl.split(partToRemove);
            if (splitted.length != 1) {
                String firstPiece = splitted[0];
                String secondPiece = splitted[1];
                if (splitted.length > 2) {
                    String[] splitted2 = new String[splitted.length - 1];
                    System.arraycopy(splitted, 1, splitted2, 0, splitted2.length);
                    secondPiece = String.join(partToRemove, splitted2);
                }

                Integer breakpointPos = secondPiece.length();
                if (secondPiece.contains("?")) {
                    breakpointPos = secondPiece.indexOf("?");
                } else if (secondPiece.contains("&")) {
                    breakpointPos = secondPiece.indexOf("&");
                }

                newUrl = firstPiece + secondPiece.substring(breakpointPos);
            }
        }

        if (newUrl.equals(url)) {
            return newUrl;
        } else {
            return this.getUrlWithoutChangingParts(newUrl);
        }
    }

    public void addToPost(String key, String value) throws UnsupportedEncodingException {
        if (this.postRaw == null) {
            this.postRaw = "";
        } else {
            this.postRaw = this.postRaw + "&";
        }

        this.postRaw = this.postRaw + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
        this.addHeader("Content-Type", "application/x-www-form-urlencoded");
    }

    public void setProxy(String proxyHost, Integer proxyPort) {
        this.proxy = new HashMap();
        this.proxy.put("host", proxyHost);
        this.proxy.put("port", String.valueOf(proxyPort));
    }

    public void addCookie(String key, String value) {
        this.cookies.put(key, value);
    }

    public void addAcceptedHttpCode(Integer httpCode) {
        this.acceptedHttpCodes.add(httpCode);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }
}
