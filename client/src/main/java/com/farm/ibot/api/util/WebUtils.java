package com.farm.ibot.api.util;

import com.farm.ibot.api.util.web.WebClient;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.net.URL;

public class WebUtils {
    static PaintTimer lastTimer = new PaintTimer(0L);
    private static String myIpAddress = "";

    public static String download(String location) {
        return downloadString(location);
    }

    public static String downloadString(String webPage) {
        WebClient wc = new WebClient();
        wc.readTimeout = 160000;
        wc.setProxy((String) null);
        return wc.downloadString(webPage);
    }

    public static String uploadString(String webPage, String body) {
        return (new WebClient()).post(webPage, body).getBody();
    }

    public static void downloadFile(String filename, String urlString) {
        BufferedInputStream in = null;
        FileOutputStream fout = null;

        try {
            in = new BufferedInputStream((new URL("http://" + urlString)).openStream());
            fout = new FileOutputStream(filename);
            byte[] data = new byte[1024];

            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(fout);
        }

    }

    public static <T> T downloadObjectFromUrl(Type type, String url) {
        Gson gson = new Gson();

        try {
            return gson.fromJson(downloadString(url), type);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static <T> T downloadObjectFromUrl(Class<T> clazz, String url) {
        Gson gson = new Gson();

        try {
            return gson.fromJson(downloadString(url), clazz);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static <T> T downloadObject(Class<T> clazz, String location) {
        Gson gson = new Gson();
        String str;
        if (location.startsWith("http")) {
            str = downloadString(location);
        } else {
            str = downloadString(location);
        }

        try {
            return gson.fromJson(str, clazz);
        } catch (Exception var5) {
            return null;
        }
    }

    public static String sendRequest(String data) {
        return downloadString(data);
    }

    public static String uploadObject(Object object, String location) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return uploadString(location, "jsonObject=" + json);
    }

    public static String sendObject(Object object, String location) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return downloadString(location + "?jsonObject=" + json);
    }

    public static String sendObject(Object object, String location, String additionalParameters) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return downloadString(location + "?jsonObject=" + json + "&" + additionalParameters);
    }

    public static <T> T sendObject(Class<T> clazz, Object object, String location) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        String response = downloadString(location + "?jsonObject=" + json);
        return gson.fromJson(response, clazz);
    }

    public static <T> T sendObject(Class<T> clazz, Object object, String location, String additionalParameters) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        String response = downloadString(location + "?jsonObject=" + json + "&" + additionalParameters);

        return gson.fromJson(response, clazz);
    }

    public static <T> T tryDownloadObject(Class<T> clazz, String location) {
        T object = null;

        for (int i = 0; i < 5 && object == null; ++i) {
            object = downloadObject(clazz, location);
            if (object == null) {
                Time.sleep(3000);
            }
        }

        return object;
    }

    public static String fetchMyIpAddress(String proxy) {
        WebClient wc = new WebClient();
        wc.setProxy(proxy);
        wc.readTimeout = 10000;
        return wc.downloadString("https://api.ipify.org");
    }

    public static String fetchMyIpAddress() {
        myIpAddress = downloadString("https://api.ipify.org");
        lastTimer.reset();
        return myIpAddress;
    }

    public static String getMyIpAddress() {
        if (lastTimer.getElapsedSeconds() > 30L) {
            myIpAddress = downloadString("https://api.ipify.org");
            lastTimer.reset();

        }

        return myIpAddress;
    }
}
