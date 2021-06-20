package com.farm.scripts.quester.quests.tutorial;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.impl.random.LoginRandom;
import com.farm.scripts.quester.quest.Quest;
import com.farm.scripts.quester.quests.tutorial.strategy.*;

import java.awt.*;

public class TutorialIsland extends Quest implements PaintHandler {
    public static final boolean SKIPPY_ENABLED = false;

    public TutorialIsland() {
        super(new DisplayNameSelector(), new DisplayNameSelector2(), new CharacterInterface(), new DialogueSkip(), new Skippy(), new RunescapeGuide(), new ClickSettings(), new SurvivalExpert(), new CookStrategy(), new QuestGuide(), new EmoteStrategy(), new MiningInstructor(), new CombatInstructor(), new BankInteractions(), new BrotherBrace(), new MagicInstructor(), new FinancialAdvisor());
    }

    public void onPaint(Graphics g) {
        g.drawString("Tutorial OLD", 5, 25);
        g.drawString("State:  " + TutorialState.getState(), 5, 45);
        g.drawString("ID: " + Config.get(281), 5, 65);
    }

    public boolean isCompleted() {
        if ((new Tile(1686, 6115, 0)).distance() < 80) {

            return true;
        } else if (Config.get(2686) > 1) {

            return true;
        } else {
            return Npcs.get("Survival Expert") == null && !LoginRandom.isLoggedOut() && (new Tile(3139, 3091, 0)).distance() > 80 && (new Tile(3110, 9509, 0)).distance() > 80 && TutorialState.isInState(TutorialState.NONE) || TutorialState.isInState(TutorialState.TUTORIAL_DONE_2) || TutorialState.isInState(TutorialState.TUTORIAL_DONE);
        }
    }

    public String getStateString() {
        return TutorialState.getState().toString();
    }
}
