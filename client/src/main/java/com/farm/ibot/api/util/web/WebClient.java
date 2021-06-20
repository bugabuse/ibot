package com.farm.ibot.api.util.web;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.Bot;
import com.farm.ibot.init.ConsoleParams;
import com.farm.ibot.proxy.SocksAuthenticator;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.net.Proxy.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

public class WebClient {
    private final CookieManager cookieManager = new CookieManager();
    public int readTimeout = 20000;
    public boolean encodeUrl = true;
    public boolean useCookies = true;
    private String referer = "";
    private String proxy;
    private HashMap<String, String> headers = new HashMap();

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public WebClient useCookies(boolean useCookies) {
        this.useCookies = useCookies;
        return this;
    }

    public String downloadString(String url) {
        String[] result = new String[]{""};
        Throwable throwable = new Throwable("Started here.");
        ProxyRequestThread t = new ProxyRequestThread(Thread.currentThread().getThreadGroup(), this.proxy, () -> {
            int attempt = 0;

            while (attempt < 4) {
                try {
                    result[0] = this.downloadStringNormal(url);
                    break;
                } catch (Exception var6) {
                    if (url.contains("localhost:6666")) {
                        break;
                    }

                    var6.printStackTrace();
                    throwable.printStackTrace();
                    Time.sleep(2000);
                    ++attempt;
                }
            }

        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException var6) {

        }

        return result[0];
    }

    public WebResponse post(String url, String body) {
        WebResponse[] result = new WebResponse[1];
        Throwable throwable = new Throwable("Started here.");
        ProxyRequestThread t = new ProxyRequestThread(Thread.currentThread().getThreadGroup(), this.proxy, () -> {
            int attempt = 0;

            while (attempt < 4) {
                try {
                    result[0] = this.postNormal(url, body);
                    break;
                } catch (Exception var7) {
                    if (url.contains("localhost:6666")) {
                        break;
                    }

                    var7.printStackTrace();
                    throwable.printStackTrace();
                    Time.sleep(5000);
                    ++attempt;
                }
            }

        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException var7) {
        }

        return result[0];
    }

    public WebResponse post(String url, HashMap<String, String> values) {
        StringBuilder sb = new StringBuilder();
        Iterator var4 = values.entrySet().iterator();

        while (var4.hasNext()) {
            Entry<String, String> entry = (Entry) var4.next();
            sb.append((String) entry.getKey() + "=" + (String) entry.getValue() + "&");
        }

        sb.deleteCharAt(sb.length() - 1);
        return this.post(url, sb.toString());
    }

    public String downloadStringNormal(String webPage) throws Exception {
        InputStream is = null;
        InputStreamReader isr = null;
        URLConnection urlConnection = this.createUrlConnection(webPage);
        if (urlConnection == null) {
            return "";
        } else {
            is = urlConnection.getInputStream();
            if ("gzip".equals(urlConnection.getContentEncoding())) {
                is = new GZIPInputStream(urlConnection.getInputStream());
            }

            isr = new InputStreamReader((InputStream) is);
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();

            int numCharsRead;
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }

            IOUtils.closeQuietly((InputStream) is);
            IOUtils.closeQuietly(isr);
            return StringUtils.format(sb.toString());
        }
    }

    public WebResponse get(String webPage) {
        InputStream is = null;
        InputStreamReader isr = null;

        try {
            URLConnection urlConnection = this.createUrlConnection(webPage);
            if (urlConnection == null) {
                WebResponse var14 = new WebResponse("");
                return var14;
            } else {
                is = urlConnection.getInputStream();
                if ("gzip".equals(urlConnection.getContentEncoding())) {
                    is = new GZIPInputStream(urlConnection.getInputStream());
                }

                isr = new InputStreamReader((InputStream) is);
                char[] charArray = new char[1024];
                StringBuffer sb = new StringBuffer();

                int numCharsRead;
                while ((numCharsRead = isr.read(charArray)) > 0) {
                    sb.append(charArray, 0, numCharsRead);
                }

                WebResponse var8 = new WebResponse(StringUtils.format(sb.toString()), (HttpURLConnection) urlConnection);
                return var8;
            }
        } catch (Exception var12) {
            var12.printStackTrace();
            return new WebResponse("");
        } finally {
            IOUtils.closeQuietly((InputStream) is);
            IOUtils.closeQuietly(isr);
        }
    }

    private WebResponse postNormal(String url, String body) throws IOException {
        URLConnection conn = null;

        InputStream is;
        try {
            if (this.proxy != null && this.proxy.length() > 0) {
                SocksAuthenticator.registerProxyCredentials(this.proxy);
                conn = (new URL(url)).openConnection(new Proxy(Type.SOCKS, new InetSocketAddress(this.proxy.split(":")[0], Integer.parseInt(this.proxy.split(":")[1]))));
            } else {
                conn = (new URL(url)).openConnection();
            }

            conn = (new URL(url)).openConnection();
            this.buildHeaders(conn);
            this.cookieManager.setCookies(conn);
            ((HttpURLConnection) conn).setInstanceFollowRedirects(false);
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(body);
            writer.flush();
            conn.connect();
            this.cookieManager.storeCookies(conn);
            is = conn.getInputStream();
            if (is != null) {
                return new WebResponse(this.readStream(is, "gzip".equals(conn.getContentEncoding())), (HttpURLConnection) conn);
            }
        } catch (Exception var8) {
            is = ((HttpURLConnection) conn).getErrorStream();

            try {
                if (is != null) {
                    this.cookieManager.storeCookies(conn);
                    return new WebResponse(this.readStream(is, "gzip".equals(conn.getContentEncoding())), (HttpURLConnection) conn);
                }
            } catch (Exception var7) {
                throw var7;
            }
        }

        return new WebResponse("");
    }

    public URLConnection createUrlConnection(String webPage) throws Exception {
        HttpURLConnection conn = null;
        URL url = new URL(webPage);
        if (this.encodeUrl) {
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = new URL(uri.toURL().toString());
        }

        if (this.proxy != null && this.proxy.length() > 0) {
            SocksAuthenticator.registerProxyCredentials(this.proxy);
            conn = (HttpURLConnection) url.openConnection(new Proxy(Type.SOCKS, new InetSocketAddress(this.proxy.split(":")[0], Integer.parseInt(this.proxy.split(":")[1]))));
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }

        if (this.readTimeout > 0) {
            conn.setReadTimeout(this.readTimeout);
        }

        conn.setInstanceFollowRedirects(false);
        this.buildHeaders(conn);
        if (this.useCookies) {
            this.cookieManager.setCookies(conn);
        }

        conn.connect();
        if (this.useCookies) {
            this.cookieManager.storeCookies(conn);
        }

        return conn;
    }

    private void printThreadStackTrace() {
        if (Thread.currentThread() instanceof ProxyRequestThread) {
            ((ProxyRequestThread) Thread.currentThread()).throwable.printStackTrace();
        }

    }

    private void buildHeaders(URLConnection connection) {
        connection.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        connection.setRequestProperty("Accept-Language", "en-US;q=0.8,en;q=0.7");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        Authentication.authRequest(connection);
        Iterator var2 = this.headers.entrySet().iterator();

        while (var2.hasNext()) {
            Entry<String, String> entry = (Entry) var2.next();
            connection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
        }

        if (connection.getURL().toString().toLowerCase().contains("szczawiczne") && Main.getComputerName() != null) {
            Bot bot = Bot.get();
            if (bot != null) {
                connection.setRequestProperty("HostName", bot.getFullHostName());
            } else {
                connection.setRequestProperty("HostName", Main.getComputerName());
            }
        }

        this.cookieManager.setCookie(this.cookieManager.getDomainFromHost("api.hax0r.farm:8080".split(":")[0]), "auth", ConsoleParams.getValue("auth"));
        if (this.referer.length() > 0) {
            connection.setRequestProperty("Referer", this.referer);
            this.referer = "";
        }

    }

    private String readStream(InputStream inputStream, boolean gzip) {
        try {
            if (gzip) {
                inputStream = new GZIPInputStream((InputStream) inputStream);
            }

            return IOUtils.toString((InputStream) inputStream);
        } catch (IOException var4) {
            var4.printStackTrace();
            return "";
        }
    }

    public CookieManager getCookieManager() {
        return this.cookieManager;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public HashMap<String, String> getHeaders() {
        return this.headers;
    }
}
