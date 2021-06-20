/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import com.farm.server.content.botdata.BotData;
import com.farm.server.core.util.RequestUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerController {
    public static HashMap<String, BotData> botServers = new HashMap();

    @RequestMapping(value={"/manager/list"})
    public Collection<BotData> printList() throws IOException {
        return botServers.values();
    }

    @RequestMapping(value={"/manager/update"})
    public String update(HttpServletRequest request) throws IOException {
        String ipAddress = RequestUtils.getIpAddress(request);
        botServers.put(ipAddress, new BotData(ipAddress, System.currentTimeMillis()));
        return "OK";
    }
}

