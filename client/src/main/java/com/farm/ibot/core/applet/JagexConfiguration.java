package com.farm.ibot.core.applet;

import com.farm.ibot.api.util.web.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JagexConfiguration {
    public int gameWorld;
    private Map<String, Object> data = new HashMap();

    public static JagexConfiguration fetch(int world) throws IOException {
        if (world == -1) {
            return null;
        } else {

            JagexConfiguration configuration = new JagexConfiguration();
            configuration.gameWorld = world;
            WebClient webClient = new WebClient();
            webClient.readTimeout = 5000;

            String result;
            try {
                result = webClient.downloadStringNormal("http://oldschool" + world + ".runescape.com/jav_config.ws");
            } catch (Exception var12) {
                return null;
            }

            if (result.split("\\n").length < 5) {
                return null;
            } else {
                String[] var4 = result.split("\\n");
                int var5 = var4.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    String line = var4[var6];
                    if (line.trim().length() >= 1) {
                        String key = line.substring(0, line.indexOf(61));
                        String value = line.substring(line.indexOf(61) + 1);
                        byte var11 = -1;
                        switch (key.hashCode()) {
                            case 108417:
                                if (key.equals("msg")) {
                                    var11 = 0;
                                }
                                break;
                            case 106436749:
                                if (key.equals("param")) {
                                    var11 = 1;
                                }
                        }

                        switch (var11) {
                            case 0:
                            case 1:
                                configuration.putCompound(key, value.substring(0, value.indexOf(61)), value.substring(value.indexOf(61) + 1));
                                break;
                            default:
                                configuration.put(key, value);
                        }
                    }
                }

                return configuration;
            }
        }
    }

    public void put(String key, Object value) {
        this.data.put(key, value);
    }

    public void putCompound(String structure, String key, Object value) {
        Map<String, Object> m = (Map) this.data.get(structure);
        if (m == null) {
            this.put(structure, m = new HashMap());
        }

        ((Map) m).put(key, value);
    }

    public <T> T get(String key) {
        if (!key.contains(".")) {
            return (T) this.data.get(key);
        } else {
            Map<String, Object> m = this.data;
            String[] sub = key.split("\\.");

            for (int i = 0; i < sub.length - 1; ++i) {
                m = (Map) m.get(sub[i]);
            }

            return (T) m.get(sub[sub.length - 1]);
        }
    }

    public Map<String, Object> getCompoundMapping(String key) {
        return (Map) this.data.get(key);
    }

    public boolean has(String key) {
        if (!key.contains(".")) {
            return this.data.containsKey(key);
        } else {
            Map<String, Object> m = this.data;
            String[] sub = key.split("\\.");

            for (int i = 0; i < sub.length - 1; ++i) {
                m = (Map) m.get(sub[i]);
            }

            return m.containsKey(sub[sub.length - 1]);
        }
    }
}
