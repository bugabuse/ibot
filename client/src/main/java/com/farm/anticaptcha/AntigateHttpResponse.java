package com.farm.anticaptcha;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AntigateHttpResponse {
    private String body = null;
    private Map<String, String> headers = new HashMap();
    private Map<String, String> cookies = new HashMap();
    private String charset = null;
    private String contentType = null;
    private Integer httpCode = null;
    private String httpMessage = null;

    public AntigateHttpResponse(String body, Map<String, String> headers, Map<String, String> cookies, String charset, String contentType, Integer httpCode, String statusMessage) {
        this.body = body;
        this.headers = headers;
        this.cookies = cookies;
        this.charset = charset;
        this.contentType = contentType;
        this.httpCode = httpCode;
        this.httpMessage = statusMessage;
    }

    AntigateHttpResponse(String body, HttpResponse apacheHttpResponse, HttpClientContext apacheHttpContext) {
        this.body = body;
        Header[] var4 = apacheHttpResponse.getAllHeaders();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Header header = var4[var6];
            this.headers.put(header.getName(), header.getValue());
        }

        Iterator var8 = apacheHttpContext.getCookieStore().getCookies().iterator();

        while (var8.hasNext()) {
            Cookie cookie = (Cookie) var8.next();
            this.cookies.put(cookie.getName(), cookie.getValue());
        }

        if (this.headers.get("Content-Type") != null) {
            String[] contentTypeHeaderSplitted = ((String) this.headers.get("Content-Type")).split("; charset=");
            this.contentType = contentTypeHeaderSplitted[0];
            if (contentTypeHeaderSplitted.length > 1) {
                this.charset = contentTypeHeaderSplitted[1];
            }
        }

        this.httpCode = apacheHttpResponse.getStatusLine().getStatusCode();
        this.httpMessage = apacheHttpResponse.getStatusLine().getReasonPhrase();
    }

    public String getBody() {
        return this.body;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    private Map<String, String> getHeadersWithoutDots() {
        Map<String, String> newHeaders = new HashMap();
        Iterator var2 = this.headers.entrySet().iterator();

        while (var2.hasNext()) {
            Entry<String, String> header = (Entry) var2.next();
            newHeaders.put(((String) header.getKey()).replace(".", "\\uff0E"), header.getValue());
        }

        return newHeaders;
    }

    public Map<String, String> getCookies() {
        return this.cookies;
    }

    private Map<String, String> getCookiesWithoutDots() {
        Map<String, String> newCookies = new HashMap();
        Iterator var2 = this.cookies.entrySet().iterator();

        while (var2.hasNext()) {
            Entry<String, String> cookie = (Entry) var2.next();
            newCookies.put(((String) cookie.getKey()).replace(".", "\\uff0E"), cookie.getValue());
        }

        return newCookies;
    }

    public String getCharset() {
        return this.charset;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Integer getHttpCode() {
        return this.httpCode;
    }

    public String getHttpMessage() {
        return this.httpMessage;
    }
}
