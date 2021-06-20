/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 */
package com.farm.server.core.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static String getHostName(HttpServletRequest request) {
        return request.getHeader("HostName");
    }
}

