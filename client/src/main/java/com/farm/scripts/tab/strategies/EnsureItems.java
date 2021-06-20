package com.farm.scripts.tab.strategies;

import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.impl.random.LoginRandom;

public class EnsureItems extends Strategy {
    int tries = 0;

    public boolean active() {
        return !LoginRandom.isLoggedOut();
    }

    protected void onAction() {
        this.sleep(5000);
        if (!Inventory.container().containsAny(new int[]{1761, 1762})) {
            this.stopScript();
        } else if (!Inventory.container().containsAny(new int[]{557, 554})) {
            this.stopScript();
        } else if (!Inventory.container().containsAny(new int[]{563})) {
            this.stopScript();
        } else if (!Inventory.container().contains(995, 200)) {
            this.stopScript();
        } else {
            Bot.get().getScriptHandler().loginRandom.active = true;
            Bot.get().getScriptHandler().antiKick.active = true;
        }
    }

    private void stopScript() {
        ++this.tries;
        if (this.tries > 10) {

            this.tries = 0;
            Bot.get().getScriptHandler().loginRandom.active = false;
            Bot.get().getScriptHandler().antiKick.active = false;
            Login.logout();
            ScriptUtils.interruptCurrentLoop();
        }

    }
}
