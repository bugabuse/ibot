package com.farm.ibot.plugins.muleutilsupdater;

import com.farm.ibot.core.plugin.Plugin;
import com.farm.ibot.scriptutils.mule.MuleUtils;

public class MuleUtilsUpdater extends Plugin {
    public void onStart() {
    }

    public int onLoop() {
        MuleUtils.updateOnlineData();
        return 5000;
    }
}
