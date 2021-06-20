package com.farm.ibot.api.interfaces;

import com.farm.ibot.core.Bot;

public interface PlayerSpotInfo {
    default int maxSpotCount() {
        return 1;
    }

    default int currentSpotId() {
        return Bot.get().getSession().getAccount().getUniqueScriptId() % this.maxSpotCount();
    }
}
