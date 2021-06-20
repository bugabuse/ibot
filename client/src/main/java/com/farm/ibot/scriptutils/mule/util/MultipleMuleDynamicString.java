package com.farm.ibot.scriptutils.mule.util;

import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.WebConfig;
import com.farm.ibot.api.util.string.DynamicString;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.scriptutils.mule.MuleUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class MultipleMuleDynamicString extends DynamicString {
    private String currentString;
    private PaintTimer timer = new PaintTimer(0L);
    private WebConfigDynamicString[] mules;

    public MultipleMuleDynamicString(String... mules) {
        ArrayList<WebConfigDynamicString> list = new ArrayList();
        Arrays.stream(mules).forEach((m) -> {
            list.add(new WebConfigDynamicString(m, 60000L));
        });
        this.mules = (WebConfigDynamicString[]) list.toArray(new WebConfigDynamicString[0]);
    }

    public String determineMuleName() {
        Debug.log(AccountData.current().getGameUsername() + " " + AccountData.current().getId());

        for (int i = 0; i < this.mules.length; ++i) {
            if (AccountData.current().getId() % this.mules.length == i && MuleUtils.isOnline(this.mules[i].toString())) {
                return this.mules[i].getKey();
            }
        }

        return this.mules[0].getKey();
    }

    public String toString() {
        if (this.timer.getElapsed() >= 60000L) {
            String temp = WebConfig.getString(this.determineMuleName());
            if (temp.length() > 0) {
                this.currentString = temp;
            }

            this.timer.reset();
        }

        return this.currentString;
    }
}
