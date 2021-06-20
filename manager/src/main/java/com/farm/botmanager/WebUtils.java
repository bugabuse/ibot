package com.farm.botmanager;

import com.farm.botmanager.web.WebClient;

public class WebUtils {
    public static final String GOLDFARM_DOMAIN = "api.hax0r.farm";

    public static String downloadString(String webPage) {
        return new WebClient().downloadString(webPage);
    }

    public static void sendRequest(String data) {
        WebUtils.downloadString("http://api.hax0r.farm:8080/" + data);
    }
}

