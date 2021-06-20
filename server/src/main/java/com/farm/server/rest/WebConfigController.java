/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.farm.server.core.util.FileSave;
import com.farm.server.core.util.FileUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;

@RestController
public class WebConfigController {
    public static HashMap<String, String> config;

    @PostConstruct
    public void onInitialize() throws IOException {
        this.load();
    }

    @RequestMapping(value={"/webconfig"})
    public String doThings(@RequestParam(value="get", required=false) String req, @RequestParam(value="key", required=false) String key, @RequestParam(value="value", required=false) String value) throws IOException {
        if (config == null) {
            this.load();
        }
        String str = "";
        if (req.equals("get")) {
            str = config.get(key);
        }
        if (req.equals("set")) {
            config.put(key, value);
            this.save();
            str = this.printConfigPage(str);
        }
        if (req.equals("all")) {
            str = this.printConfigPage(str);
        }
        return str;
    }

    private String printConfigPage(String str) {
        str = str + "<a href='../index.html'>OSRSBot Panel</a><br>";
        str = str + "<a href='../accounts/online'>Online List</a><br>";
        str = str + "<br><br>";
        for (Map.Entry<String, String> entry : config.entrySet()) {
            str = str + "<form action='./'><input type='hidden' name='get' value='set'></input><input type='text' name='key' value='" + entry.getKey() + "'></input><input type='text' name='value' value='" + entry.getValue() + "'></input><input type='submit' value='Set'></input></form>";
        }
        return str;
    }

    public static int getInt(String key) {
        return Integer.parseInt(config.get(key));
    }

    public void save() throws IOException {
        FileSave.serialize(FileUtils.getFile("files/WebConfig.json"), config);
    }

    public void load() throws IOException {
        File file = FileUtils.getFile("files/WebConfig.json");
        if (file.exists()) {
            FileReader reader = new FileReader(file);
            config = (HashMap)new Gson().fromJson((Reader)reader, new TypeToken<HashMap<String, String>>(){}.getType());
            reader.close();
        } else {
            config = new HashMap();
        }
    }

}

