package com.farm.scripts.farmer.strategies;

import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.util.web.account.AccountConfig;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.farmer.api.FarmingPatch;
import com.farm.scripts.farmer.api.PatchState;

public class FarmingStateListener extends Strategy implements MessageListener {
    public static AccountConfig config = new AccountConfig();

    public FarmingStateListener(MultipleStrategyScript script) {
        config = AccountConfig.fetch(AccountData.current().username);
        script.addEventHandler(new MessageEventHandler(this));
    }

    public boolean isBackground() {
        return false;
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        FarmingPatch patch = FarmingPatch.current();
        if (patch != null) {
            if (!PatchState.get().equals(PatchState.GROWING) && !PatchState.get().equals(PatchState.GROWN) && (Long) config.getOrDefault(patch.getName(), 0L) > 0L) {
                config.put(patch.getName(), 0L);
                config.update();
                return;
            }

            if (PatchState.get().equals(PatchState.GROWING) && (Long) config.getOrDefault(patch.getName(), 0L) <= 0L) {
                config.put(patch.getName(), System.currentTimeMillis());
                config.update();
            }
        }

        this.sleep(10);
    }

    public void onMessage(String message) {
        if (message.contains("seed in the herb patch")) {
            FarmingPatch patch = FarmingPatch.current();
            if (patch != null) {
                config.put(patch.getName(), System.currentTimeMillis());
                config.update();
            }
        }

    }
}
