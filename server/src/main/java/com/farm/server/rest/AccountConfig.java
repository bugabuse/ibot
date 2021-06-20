/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestMethod
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.farm.server.core.util.FileSave;
import com.farm.server.core.util.FileUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;

@RestController
public class AccountConfig {
    private static Logger logger = Logger.getLogger(AccountConfig.class.getName());
    public static ConcurrentHashMap<String, HashMap<String, Object>> configs = new ConcurrentHashMap();
    private long lastUpdate = 0L;

    @PostConstruct
    public void onInitialize() throws IOException {
        this.load();
    }

    @RequestMapping(value={"/account/config/get/{loginEmail}"})
    public HashMap<String, Object> get(@PathVariable String loginEmail) {
        return configs.getOrDefault(loginEmail, new HashMap());
    }

    @RequestMapping(value={"/account/config/set/{loginEmail}"}, method={RequestMethod.POST})
    public String set(String jsonObject, @PathVariable String loginEmail) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap configLocal = (HashMap)mapper.readValue(jsonObject, HashMap.class);
        configs.put(loginEmail, configLocal);
        this.save();
        return "Thanks";
    }

    private void load() throws IOException {
        File file;
        if (configs.size() == 0 && (file = FileUtils.getFile("files/AccountConfig.json")).exists()) {
            logger.info("Loading account configs.");
            FileReader reader = new FileReader(file);
            ObjectMapper mapper = new ObjectMapper();
            configs = (ConcurrentHashMap)mapper.readValue((Reader)reader, ConcurrentHashMap.class);
            reader.close();
        }
    }

    private void save() {
        if (System.currentTimeMillis() - this.lastUpdate > 30000L) {
            this.lastUpdate = System.currentTimeMillis();
            FileSave.serialize(FileUtils.getFile("files/AccountConfig.json"), configs);
            this.lastUpdate = System.currentTimeMillis();
        }
    }
}

