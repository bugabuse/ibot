package com.farm.scripts.quester.quests.sheepshearer;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.scripts.quester.quest.Quest;
import com.farm.scripts.quester.quests.sheepshearer.strategy.FarmerTalk;
import com.farm.scripts.quester.quests.sheepshearer.strategy.ShearSheep;

import java.awt.*;

public class SheepShearer extends Quest implements PaintHandler {
    public SheepShearer() {
        super(new FarmerTalk(), new ShearSheep());
    }

    public void onPaint(Graphics g) {
        g.drawString("State: " + SheepState.getState(), 5, 25);
    }

    public boolean isCompleted() {
        return SheepState.isInState(SheepState.QUEST_DONE);
    }

    public String getStateString() {
        return SheepState.getState().toString();
    }
}
