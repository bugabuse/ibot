package com.farm.scripts.quester.quests.cooksassistant;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.scripts.quester.quest.Quest;
import com.farm.scripts.quester.quests.cooksassistant.strategy.CookTalk;
import com.farm.scripts.quester.quests.cooksassistant.strategy.GetCookShit;

import java.awt.*;

public class CooksAssistant extends Quest implements PaintHandler {
    public CooksAssistant() {
        super(new GetCookShit(get()), new CookTalk());
    }

    public void onPaint(Graphics g) {
        g.drawString("State: " + CookState.getState(), 5, 25);
    }

    public boolean isCompleted() {
        return CookState.isInState(CookState.QUEST_DONE);
    }

    public String getStateString() {
        return CookState.getState().toString();
    }
}
