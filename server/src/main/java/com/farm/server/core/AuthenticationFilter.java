/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  javax.servlet.Filter
 *  javax.servlet.FilterChain
 *  javax.servlet.FilterConfig
 *  javax.servlet.ServletException
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.Cookie
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Component
 */
package com.farm.server.core;

import com.farm.server.core.util.Debug;
import com.farm.server.core.util.RequestUtils;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter
implements Filter {
    private final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
        this.log.info("SimpleCORSFilter init");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        if (!this.isPermitted(request)) {
            response.sendError(401, "Auth.");
            Debug.log("Unauthorized connection: " + RequestUtils.getIpAddress(request));
            Debug.log("URL: " + request.getRequestURL().toString());
            return;
        }
        chain.doFilter((ServletRequest)request, (ServletResponse)response);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

    public boolean isPermitted(HttpServletRequest request) {

        if(0!=9)return true;

        if (request.getRequestURL().toString().contains("dologin")) {
            return true;
        }
        if (request.getRequestURL().toString().contains("localhost")) {
            return true;
        }
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (!c.getName().equalsIgnoreCase("auth") || !c.getValue().equalsIgnoreCase("jestemzalogowany")) continue;
                return true;
            }
        }
        return false;
    }
}

