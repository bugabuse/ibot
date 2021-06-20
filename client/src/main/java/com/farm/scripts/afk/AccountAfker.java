package com.farm.scripts.afk;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.string.AccountPlaytimeDynamicString;
import com.farm.ibot.api.util.string.WebConfigAsyncDynamicString;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.scripts.quester.quests.cooksassistant.CookState;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

import java.awt.*;

public class AccountAfker extends BotScript implements ScriptRuntimeInfo, PaintHandler {
    private PaintTimer timer = new PaintTimer();
    private AccountPlaytimeDynamicString playTime = new AccountPlaytimeDynamicString();
    private WebConfigDynamicString afkTime = new WebConfigAsyncDynamicString("afk_time", 60000L);
    private String hansMessage = "";

    public boolean isCompletedTutorial() {
        return TutorialState.isInState(TutorialState.NONE) || TutorialState.isInState(TutorialState.TUTORIAL_DONE_2) || TutorialState.isInState(TutorialState.TUTORIAL_DONE);
    }

    public String runtimeInfo() {
        return String.format("<th>%s</th><th>%s</th><th>%s</th><th>%s</th>", "Current: " + this.timer.getElapsedString(), "Account hours: " + this.getTotalPlayTime(), "Cooks: " + CookState.isInState(CookState.QUEST_DONE), this.hansMessage);
    }

    private long getTotalPlayTime() {
        return (long) this.playTime.intValue() / 1000L / 60L / 60L;
    }

    public void onStart() {
    }

    public int onLoop() {
        Debug.log(this.getTotalPlayTime() + " total playtime. We need: " + this.afkTime.intValue());
        if (!Client.isInGame()) {

            return 1000;
        } else if (this.getTotalPlayTime() >= (long) this.afkTime.intValue()) {
            Debug.log(this.afkTime.intValue() + " passed. Starting next script, current queue " + this.getScriptHandler().getScriptQueue().size());
            this.getScriptHandler().startNextQueuedScript(this);
            return 1000;
        } else {
            Client.setIdleTime(0);
            Mouse.moveOffScreen();
            Time.sleep(200, 5000);
            Mouse.loseFocus();
            Time.sleep(30000, 240000);
            return 1;
        }
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "AccountAfker 0.08");
        this.drawString(g, "Current: " + this.timer.getElapsedString());
        this.drawString(g, "Hours: " + this.getTotalPlayTime());
        this.drawString(g, "Needed: " + this.afkTime.intValue());
        this.drawString(g, "Time: " + this.hansMessage);
        Bot.get().properties.put("longSleep", "2");
    }
}
