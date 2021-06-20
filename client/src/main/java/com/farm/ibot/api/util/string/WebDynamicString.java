package com.farm.ibot.api.util.string;

import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.WebUtils;

public class WebDynamicString extends DynamicString {
    private final String url;
    private final long updateRatioMs;
    private String currentString;
    private PaintTimer timer = new PaintTimer(0L);

    public WebDynamicString(String url) {
        this.url = url;
        this.updateRatioMs = 5000L;
    }

    public WebDynamicString(String url, long updateRatioMs) {
        this.url = url;
        this.updateRatioMs = updateRatioMs;
    }

    public String toString() {
        if (this.timer.getElapsed() >= this.updateRatioMs) {
            String temp = WebUtils.downloadString(this.url);
            if (temp.length() > 0) {
                this.currentString = temp;
            }

            this.timer.reset();
        }

        return this.currentString;
    }
}
