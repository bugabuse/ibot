package com.farm.ibot.api.util.web;

import java.net.URLConnection;
import java.util.Base64;

public class Authentication {
    public static void authRequest(URLConnection httpUrlConnection) {
        String encoding = Base64.getEncoder().encodeToString("kotlet:schabowy".getBytes());
        httpUrlConnection.setRequestProperty("Authorization", "Basic " + encoding);
    }
}
