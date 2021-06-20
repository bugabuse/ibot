package com.farm.scripts.quester.quests.tutorialnew.strategy;

import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.core.script.Strategy;

public class DialogueSkip extends Strategy {
    protected void onAction() {

        Keyboard.press(32);
    }
}
