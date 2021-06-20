package com.farm.ibot.api.util;

import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.StrategyExecuteThread;
import com.farm.ibot.core.script.StrategyScript;
import com.farm.ibot.init.AccountData;

public class ScriptUtils {
    public static void rebindAndDelete(String rebindTo) {
        AccountData.current().autostartScript = rebindTo;
        AccountData.current().update();
        Bot.get().getSession().setAccount((AccountData) null);

        while (!Login.logout()) {
            Time.sleep(1000);
        }

        Bot.get().getScriptHandler().stop();
    }

    public static void interruptCurrentLoop() {
        BotScript script = Bot.get().getScriptHandler().getScript();
        if (script instanceof StrategyScript) {
            StrategyExecuteThread thread = ((StrategyScript) script).getCurrentStrategyLoopThread();
            if (thread != null && !thread.isInterrupted()) {
                thread.stopExecutingCurrentLoop();
            }
        }

    }

    public static Strategy getLockedStrategy() {
        BotScript script = Bot.get().getScriptHandler().getScript();
        return script instanceof StrategyScript ? ((StrategyScript) script).getWaitStrategy() : null;
    }

    public static void unlockStrategyWait() {
        BotScript script = Bot.get().getScriptHandler().getScript();
        if (script instanceof StrategyScript) {
            ((StrategyScript) script).unlockStrategyWait();
        }

    }

    public static void waitForStrategyExecute(Strategy strategy) {
        BotScript script = Bot.get().getScriptHandler().getScript();
        if (script instanceof StrategyScript) {
            ((StrategyScript) script).waitForStrategy(strategy);
        }

    }
}
