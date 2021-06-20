/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.catalina.connector.ClientAbortException
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.core.config;

import java.io.PrintStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations={RestController.class})
public class ExceptionHandling {
    @ExceptionHandler(value={Exception.class})
    public String handleError(HttpServletRequest req, Exception exception) {
        if (exception instanceof ClientAbortException) {
            return "";
        }
        exception.printStackTrace();
        return "";
    }
}

