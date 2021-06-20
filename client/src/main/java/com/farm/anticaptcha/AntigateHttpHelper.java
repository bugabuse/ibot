package com.farm.anticaptcha;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.cookie.BasicClientCookie;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map.Entry;

public class AntigateHttpHelper {
    static AntigateHttpResponse download(AntigateHttpRequest request) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        if (request.getCookies() != null) {
            Iterator var2 = request.getCookies().entrySet().iterator();

            while (var2.hasNext()) {
                Entry<String, String> cookieEntry = (Entry) var2.next();
                BasicClientCookie cookie = new BasicClientCookie((String) cookieEntry.getKey(), (String) cookieEntry.getValue());
                cookie.setDomain(getCookieDomain(request.getUrl()));
                cookieStore.addCookie(cookie);
            }
        }

        HttpClientBuilder httpClientBuilder;
        if (!request.isValidateTLSCertificates() && request.getUrl().toLowerCase().charAt(4) == 's') {
            httpClientBuilder = AntigateHttpHelper.HttpsClientBuilderGiver.INSTANCE.getHttpsClientBuilder();
        } else {
            httpClientBuilder = HttpClientBuilder.create();
        }

        if (request.getCookies() != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }

        if (request.getProxy() != null) {
            httpClientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(new HttpHost((String) request.getProxy().get("host"), Integer.parseInt((String) request.getProxy().get("port")))));
        }

        CloseableHttpClient httpClient;
        if (request.isFollowRedirects()) {
            httpClient = httpClientBuilder.build();
        } else {
            httpClient = httpClientBuilder.disableRedirectHandling().build();
        }

        Object apacheHttpRequest;
        if (request.getRawPost() == null) {
            apacheHttpRequest = new HttpGet(request.getUrl());
        } else {
            apacheHttpRequest = new HttpPost(request.getUrl());
            ((HttpPost) apacheHttpRequest).setEntity(new StringEntity(request.getRawPost(), "UTF-8"));
        }

        HttpClientContext context = HttpClientContext.create();
        ((HttpRequestBase) apacheHttpRequest).setConfig(RequestConfig.custom().setConnectionRequestTimeout(request.getTimeout()).setConnectTimeout(request.getTimeout()).setSocketTimeout(request.getTimeout()).build());
        Iterator var7 = request.getHeaders().entrySet().iterator();

        while (var7.hasNext()) {
            Entry<String, String> header = (Entry) var7.next();
            ((HttpRequestBase) apacheHttpRequest).addHeader((String) header.getKey(), (String) header.getValue());
        }

        HttpResponse response = httpClient.execute((HttpUriRequest) apacheHttpRequest, context);
        String charset = "utf8";
        if (response.getHeaders("Content-Type").length != 0) {
            String[] charsetSplitted = response.getHeaders("Content-Type")[0].getValue().split("; charset=");
            if (charsetSplitted.length == 2) {
                charset = charsetSplitted[1];
            }
        }

        return new AntigateHttpResponse(AntigateHttpHelper.InputOutput.INSTANCE.toString(response.getEntity().getContent(), charset, request.getMaxBodySize()), response, context);
    }

    private static String getCookieDomain(String url) {
        return "." + url.split("://")[1].split("/")[0];
    }

    private static enum InputOutput {
        INSTANCE;

        private final int DEFAULT_BUFFER_SIZE = 4096;

        public String toString(InputStream input, String encoding, Integer bytesMax) throws IOException {
            StringWriter output = new StringWriter();
            InputStreamReader in = new InputStreamReader(input, encoding);
            char[] buffer = new char[4096];
            long count = 0L;
            boolean var9 = false;

            int n;
            while (-1 != (n = in.read(buffer))) {
                output.write(buffer, 0, n);
                count += (long) n;
                if (bytesMax > 0 && count >= (long) bytesMax) {
                    break;
                }
            }

            return output.toString();
        }
    }

    private static enum HttpsClientBuilderGiver {
        INSTANCE;

        public HttpClientBuilder getHttpsClientBuilder() throws NoSuchAlgorithmException, KeyManagementException {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(new KeyManager[0], new TrustManager[]{new AntigateHttpHelper.HttpsClientBuilderGiver.HttpsTrustManager()}, new SecureRandom());
            SSLContext.setDefault(sslcontext);
            return HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslcontext, new AllowAllHostnameVerifier()));
        }

        private class HttpsTrustManager implements X509TrustManager {
            private HttpsTrustManager() {
            }

            // $FF: synthetic method
            HttpsTrustManager(Object x1) {
                this();
            }

            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }
    }
}
