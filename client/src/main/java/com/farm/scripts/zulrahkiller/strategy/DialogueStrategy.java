package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.core.script.Strategy;

public class DialogueStrategy extends Strategy {
    public boolean active() {
        return Dialogue.isInDialouge();
    }

    protected void onAction() {
        Dialogue.clickContinue();
        Dialogue.goNext(new String[]{"Yes"});
    }
}
