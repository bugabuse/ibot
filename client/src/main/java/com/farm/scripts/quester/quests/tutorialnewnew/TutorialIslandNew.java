package com.farm.scripts.quester.quests.tutorialnewnew;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.impl.random.LoginRandom;
import com.farm.scripts.quester.quest.Quest;
import com.farm.scripts.quester.quests.tutorialnew.TutorialStateNew;
import com.farm.scripts.quester.quests.tutorialnew.strategy.*;

import java.awt.*;

public class TutorialIslandNew extends Quest implements PaintHandler {
    public static final boolean SKIPPY_ENABLED = false;

    public TutorialIslandNew() {
        super(new DisplayNameSelector(), new CharacterInterface(), new DialogueSkip(), new Skippy(), new RunescapeGuide(), new ClickSettings(), new SurvivalExpert(), new CookStrategy(), new QuestGuide(), new EmoteStrategy(), new MiningInstructor(), new CombatInstructor(), new BankInteractions(), new BrotherBrace(), new MagicInstructor());
    }

    public void onPaint(Graphics g) {
        g.drawString("Tutorial NEW", 5, 25);
        g.drawString("State:  " + TutorialStateNew.getState(), 5, 45);
        g.drawString("ID: " + Config.get(2686), 5, 65);
    }

    public boolean isCompleted() {
        return !LoginRandom.isLoggedOut() && (new Tile(1686, 6115, 0)).distance() > 80 && (new Tile(3110, 9509, 0)).distance() > 80 && (TutorialStateNew.isInState(TutorialStateNew.NONE) && (new Tile(1686, 6115, 0)).distance() < 20 || TutorialStateNew.isInState(TutorialStateNew.TUTORIAL_DONE_2) || TutorialStateNew.isInState(TutorialStateNew.TUTORIAL_DONE));
    }

    public String getStateString() {
        return TutorialStateNew.getState().toString();
    }
}
