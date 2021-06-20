package com.farm.scripts.quester;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.StrategyScript;
import com.farm.scripts.quester.quest.Quest;
import com.farm.scripts.quester.quests.goblindiplomacy.GoblinDiplomacy;
import com.farm.scripts.quester.quests.romeojuliet.RomeoAndJuliet;
import com.farm.scripts.quester.quests.tutorial.TutorialIsland;
import com.farm.scripts.quester.quests.tutorialnewnew.TutorialIslandNew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Quester extends StrategyScript implements ScriptRuntimeInfo {
    private static BotScript instance;
    public ArrayList<Quest> quests = new ArrayList();
    public Quest currentQuest;
    private PaintTimer timer = new PaintTimer();
    private PaintTimer inGameTimer = null;

    public Quester() {
        super(new Strategy[0]);
        instance = this;
    }

    public void onStartWhenLoggedIn() {
    }

    public void onStart() {
        Quest[] allQuests = new Quest[]{new TutorialIsland(), new TutorialIslandNew(), new GoblinDiplomacy(), new RomeoAndJuliet()};
        System.out.println("E E  egf, d]ff  E dd" + this.startArguments);
        this.timer.reset();
        WebWalking.enableRunningCondition = WebWalking.RUNNING_ON_COMBAT_OR_ENOUGH_ENERGY;
        if (this.startArguments.length() < 1) {
            Collections.addAll(this.quests, allQuests);
        } else {
            String[] var2 = this.startArguments.split(",");
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String str = var2[var4];
                Quest[] var6 = allQuests;
                int var7 = allQuests.length;

                for (int var8 = 0; var8 < var7; ++var8) {
                    Quest quest = var6[var8];
                    if (quest.getClass().getSimpleName().equalsIgnoreCase(str)) {
                        this.quests.add(quest);
                        if (str.contains("Tutorial")) {
                            this.quests.add(allQuests[1]);
                        }
                    }
                }
            }
        }

    }

    public int onLoop() {
        if (Client.isInGame()) {
            this.checkQuestState();
            if (this.currentQuest != null) {
                this.currentQuest.onLoop();
            }
        } else {
            this.inGameTimer = null;
        }

        return 100;
    }

    public int loopInterval() {
        return 100;
    }

    private void checkQuestState() {
        if (this.inGameTimer == null) {
            this.inGameTimer = new PaintTimer();
        }

        if (this.inGameTimer.getElapsedSeconds() >= 8L) {
            if (this.currentQuest == null || this.currentQuest.isCompleted()) {
                if (this.currentQuest != null) {
                    System.out.println("Completed: " + this.currentQuest.getName());
                }

                this.currentQuest = this.getNextQuest();
                if (this.currentQuest != null) {
                    if (this.currentQuest instanceof PaintHandler) {
                        this.paintHandlers.clear();
                        this.paintHandlers.add((PaintHandler) this.currentQuest);
                    }
                } else {


                    try {
                        Bot.get().getScriptHandler().startNextQueuedScript(instance);
                    } catch (Exception var2) {
                        var2.printStackTrace();
                    }
                }
            }

        }
    }

    public Quest getNextQuest() {
        Iterator var1 = this.quests.iterator();

        Quest quest;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            quest = (Quest) var1.next();
            System.out.println(quest.getName() + " completed: " + quest.isCompleted());
        } while (quest.isCompleted());

        return quest;
    }

    public String runtimeInfo() {
        return this.currentQuest != null ? "<th>" + this.timer.getElapsedString() + "</th><th>" + this.currentQuest.getStateString() + "</th><th>" + this.currentQuest.getClass().getSimpleName() + "</th>" : "<th>" + this.timer.getElapsedString() + "</th><th>No quest</th>";
    }
}
