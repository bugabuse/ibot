package com.farm.botmanager.web;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class WebClient {
    private final CookieManager cookieManager;
    private String referer;

    public WebClient() {
        this.cookieManager = new CookieManager();
        this.referer = "";
    }

    public void setReferer(final String referer) {
        this.referer = referer;
    }

    public String downloadString(final String url) {
        return this.downloadString(url, 0);
    }

    public String downloadString(final String url, int tries) {
        final String result = this.downloadStringNormal(url);
        if (!result.contains("This process is automatic. Your browser will redirect to your requested content shortly.")) {
            if (tries > 0) {
                System.out.println("CloudFlare handled after " + tries + " tries.");
            }
            return result;
        }
        System.out.println("Handling CloudFlare");
        if (!CloudFlareHandler.handle(this, result, url)) {
            return "CLOUDFLARE";
        }
        if (result.contains("This process is automatic. Your browser will redirect to your requested content shortly.") && tries < 4) {
            return this.downloadString(url, ++tries);
        }
        System.out.println("CloudFlare handled after " + tries + " tries.");
        return result;
    }

    public String downloadStringNormal(final String webPage) {
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            final URLConnection urlConnection = this.createUrlConnection(webPage);
            is = urlConnection.getInputStream();
            if ("gzip".equals(urlConnection.getContentEncoding())) {
                is = new GZIPInputStream(urlConnection.getInputStream());
            }
            isr = new InputStreamReader(is);
            final char[] charArray = new char[1024];
            final StringBuffer sb = new StringBuffer();
            int numCharsRead;
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.format("[downloadStringNormal] url: %s%n", webPage);
            if (!webPage.contains("localhost:6666")) {
                e.printStackTrace();
            }
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(isr);
        }
        return "";
    }

    public WebResponse post(final String url, final HashMap<String, String> values) {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, String> entry : values.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return this.post(url, sb.toString());
    }

    public WebResponse post(final String url, final String body) {
        return this.post(url, body, 0);
    }

    public WebResponse post(final String url, final String body, int tries) {
        final WebResponse result = this.postNormal(url, body);
        if (!result.toString().contains("This process is automatic. Your browser will redirect to your requested content shortly.")) {
            if (tries > 0) {
                System.out.println("CloudFlare handled after " + tries + " tries.");
            }
            return result;
        }
        System.out.println("Handling CloudFlare");
        if (!CloudFlareHandler.handle(this, result.toString(), url)) {
            return new WebResponse("CLOUDFLARE");
        }
        if (result.toString().contains("This process is automatic. Your browser will redirect to your requested content shortly.") && tries < 4) {
            return this.post(url, body, ++tries);
        }
        System.out.println("CloudFlare handled after " + tries + " tries.");
        return result;
    }

    public WebResponse postNormal(final String url, final String body) {
        URLConnection conn = null;
        try {
            conn = new URL(url).openConnection();
            this.cookieManager.setCookies(conn);
            this.buildHeaders(conn);
            ((HttpURLConnection) conn).setInstanceFollowRedirects(false);
            conn.setDoOutput(true);
            final OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(body);
            writer.flush();
            conn.connect();
            this.cookieManager.storeCookies(conn);
            final InputStream is = conn.getInputStream();
            if (is != null) {
                return new WebResponse(this.readStream(is, "gzip".equals(conn.getContentEncoding())), (HttpURLConnection) conn);
            }
        } catch (Exception e) {
            final InputStream error = ((HttpURLConnection) conn).getErrorStream();
            try {
                if (error != null) {
                    this.cookieManager.storeCookies(conn);
                    return new WebResponse(this.readStream(error, "gzip".equals(conn.getContentEncoding())), (HttpURLConnection) conn);
                }
                return new WebResponse("");
            } catch (IOException e2) {
                System.out.format("[postNormal] error: %s%n", url);
                if (url.contains("localhost:6666")) {
                    return new WebResponse("");
                }
                e.printStackTrace();
            }
        }
        return new WebResponse("");
    }

    public URLConnection createUrlConnection(final String webPage) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(webPage);
            final URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = new URL(uri.toURL().toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            this.buildHeaders(conn);
            this.cookieManager.setCookies(conn);
            conn.connect();
            this.cookieManager.storeCookies(conn);
            return conn;
        } catch (Exception e) {
            System.out.format("[createUrlConnection] error loading: %s%n", webPage);
            if (!webPage.contains("localhost:6666")) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void buildHeaders(final URLConnection connection) {
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        this.cookieManager.setCookie(this.cookieManager.getDomainFromHost("api.hax0r.farm".split(":")[0]), "auth", "adil.is.a.fag");
        if (this.referer.length() > 0) {
            connection.setRequestProperty("Referer", this.referer);
            this.referer = "";
        }
    }

    private String readStream(InputStream inputStream, final boolean gzip) {
        try {
            if (gzip) {
                inputStream = new GZIPInputStream(inputStream);
            }
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public CookieManager getCookieManager() {
        return this.cookieManager;
    }
}
