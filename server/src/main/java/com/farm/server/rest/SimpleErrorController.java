/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.boot.autoconfigure.web.ErrorAttributes
 *  org.springframework.boot.autoconfigure.web.ErrorController
 *  org.springframework.util.Assert
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.context.request.RequestAttributes
 *  org.springframework.web.context.request.ServletRequestAttributes
 */
package com.farm.server.rest;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value={"/error"})
public class SimpleErrorController
implements ErrorController {
    private final ErrorAttributes errorAttributes;

    @Autowired
    public SimpleErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull((Object)errorAttributes, (String)"ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping
    public Map<String, Object> error(HttpServletRequest aRequest) {
        Map<String, Object> body = this.getErrorAttributes(aRequest, this.getTraceParameter(aRequest));
        String trace = (String)body.get("trace");
        if (trace != null) {
            String[] lines = trace.split("\n\t");
            body.put("trace", lines);
        }
        return body;
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
        return this.errorAttributes.getErrorAttributes((WebRequest) requestAttributes, includeStackTrace);
    }
}

