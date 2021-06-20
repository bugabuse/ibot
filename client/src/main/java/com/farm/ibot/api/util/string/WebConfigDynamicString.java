package com.farm.ibot.api.util.string;

public class WebConfigDynamicString extends WebDynamicString {
    private final String key;

    public WebConfigDynamicString(String key) {
        this(key, 10000L);
    }

    public WebConfigDynamicString(String key, long updateRatioMs) {
        super("http://api.hax0r.farm:8080/webconfig/?get=get&key=" + key, updateRatioMs);
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public double doubleValue() {
        return Double.parseDouble(this.toString());
    }
}
