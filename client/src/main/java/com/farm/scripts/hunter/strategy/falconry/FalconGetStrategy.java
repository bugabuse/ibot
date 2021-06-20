package com.farm.scripts.hunter.strategy.falconry;

import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;

public class FalconGetStrategy extends Strategy {
    public static FalconGetStrategy instance;
    public static boolean needsFalcon = false;

    public FalconGetStrategy() {
        instance = this;
    }

    public boolean active() {
        return needsFalcon;
    }

    public void onAction() {
        if (!Dialogue.talkTo("Matthias")) {
            if (!Walking.walkTo(new Tile(2372, 3607, 0), 5)) {
                return;
            }
        } else if (!Dialogue.contains("Hello again") && !Dialogue.contains("The falconer brings a bird")) {
            Dialogue.goNext(new String[]{"Yes", "reasonable", "with your bird"});
        } else {
            needsFalcon = false;
        }

    }
}
