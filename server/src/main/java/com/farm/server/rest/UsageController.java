/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsageController {
    @RequestMapping(value={"/usage"})
    public String bind() throws IOException {
        StringBuilder sb = new StringBuilder();
        int mb = 1048576;
        Runtime runtime = Runtime.getRuntime();
        sb.append("##### Heap utilization statistics [MB] #####");
        sb.append("<br>");
        sb.append("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / (long)mb);
        sb.append("<br>");
        sb.append("Free Memory:" + runtime.freeMemory() / (long)mb);
        sb.append("<br>");
        sb.append("Total Memory:" + runtime.totalMemory() / (long)mb);
        sb.append("<br>");
        sb.append("Max Memory:" + runtime.maxMemory() / (long)mb);
        return sb.toString();
    }
}

