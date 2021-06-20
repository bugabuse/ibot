package com.farm.ibot.api.util.string;

import com.farm.ibot.api.util.Time;

public class WebConfigAsyncDynamicString extends WebConfigDynamicString {
    private String str;


    public WebConfigAsyncDynamicString(String key, long updateRatioMs) {
        super(key, updateRatioMs);

        (new Thread(() -> {
            while (true) {
                try {
                    this.str = super.toString();
                    Time.sleep(updateRatioMs);
                } catch (Exception var4) {
                }
            }
        })).start();
    }

    public String toString() {
        return this.str;
    }
}
