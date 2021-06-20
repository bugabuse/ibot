package com.farm.ibot.scriptutils.mule.util;

import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.TradeData;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.util.string.DynamicString;
import com.farm.ibot.init.AccountData;

public class OnDemandMuleDynamicString extends DynamicString {
    public TradeData tradeData = null;
    private PaintTimer timer = new PaintTimer(0L);

    public String toString() {
        if (this.timer.getElapsed() >= 2000L) {
            this.tradeData = (TradeData) WebUtils.downloadObject(TradeData.class, "http://api.hax0r.farm:8080/mule/get?slaveName=" + AccountData.current().getGameUsername());
            this.timer.reset();
        }

        return this.tradeData != null && this.tradeData.muleHandlerName != null && this.tradeData.muleHandlerName.length() > 0 ? this.tradeData.muleHandlerName : null;
    }
}
