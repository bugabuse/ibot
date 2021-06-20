package com.farm.ibot.api.util;

import com.google.gson.Gson;

public class WebConfig {
    public static int getInt(String key) {
        String str = WebUtils.sendRequest("http://api.hax0r.farm:8080/webconfig/?get=get&key=" + key);
        return Integer.parseInt(str);
    }

    public static String getString(String key) {
        return WebUtils.sendRequest("http://api.hax0r.farm:8080/webconfig/?get=get&key=" + key);
    }

    public static <T> T getObject(Class<T> clazz, String key) {
        return WebUtils.downloadObject(clazz, "http://api.hax0r.farm:8080/webconfig/?get=get&key=" + key);
    }

    public static void set(String key, int value) {
        set(key, "" + value);
    }

    public static void set(String key, String value) {
        WebUtils.sendRequest("http://api.hax0r.farm:8080/webconfig/?get=set&key=" + key + "&value=" + value);
    }

    public static void setObject(String key, Object obj) {
        String value = (new Gson()).toJson(obj);
        WebUtils.sendRequest("http://api.hax0r.farm:8080/webconfig/?get=set&key=" + key + "&value=" + value);
    }
}
