package com.farm.scripts.accounttrainer;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.scripts.combat.Combat;
import com.farm.scripts.firemaker.Firemaker;
import com.farm.scripts.fisher.Fisher;
import com.farm.scripts.woodcutter.Chopper;

import java.awt.*;
import java.util.Arrays;

public class AccountTrainer extends BotScript implements PaintHandler, ScriptRuntimeInfo {
    public static Skill recentlyTrained;
    public PaintTimer timer = new PaintTimer();
    public PaintTimer timerScript = new PaintTimer(0L);
    private int runCurrentScriptMinutes = -1;
    private SkillToTrain currentSkill;
    private PaintTimer lastStatsCheck = new PaintTimer(0L);

    private static void shuffle(SkillToTrain[] array) {
        for (int i = array.length - 1; i > 0; --i) {
            int index = Random.next(0, i + 1);
            SkillToTrain temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }

    }

    public void onStart() {

    }

    public int onLoop() {

        if (!Client.isInGame()) {
            return 1000;
        } else {

            if (this.getTotalLevel() >= 100 || this.getTotalLevel() >= 59 && this.getScriptHandler().getScriptQueue().stream().anyMatch((s) -> {
                return s.getName().contains("Chopper");
            })) {

                this.getScriptHandler().startNextQueuedScript(this);
                return 1000;
            } else {

                Debug.log("Total level: " + this.getTotalLevel());
                if (!Varbit.MEMBERSHIP_DAYS.booleanValue() || !WorldHopping.isF2p(Client.getCurrentWorld()) || this.currentSkill.script instanceof MultipleStrategyScript && ((MultipleStrategyScript) this.currentSkill.script).getCurrentlyExecuting().toString().toLowerCase().contains("mule")) {
                    if (this.currentSkill == null || this.currentSkill.skill != null && this.currentSkill.skill.getRealLevel() >= this.currentSkill.maxLevelToTrain || this.currentSkill.getStopPredicate() != null && this.currentSkill.getStopPredicate().getAsBoolean()) {
                        if (this.currentSkill != null) {
                            recentlyTrained = this.currentSkill.skill;
                            this.currentSkill = null;
                        }

                        this.currentSkill = this.getNextSkill();
                        if (this.currentSkill.script instanceof MultipleStrategyScript) {
                            MultipleStrategyScript.instances.put(Bot.get(), (MultipleStrategyScript) this.currentSkill.script);
                        }

                        this.currentSkill.script.scriptHandler = this.getScriptHandler();
                        this.currentSkill.script.onStart();
                        this.currentSkill.script.onStartWhenLoggedIn();
                        this.timerScript.reset();
                        return 1000;
                    } else {
                        return this.currentSkill.script.onLoop();
                    }
                } else {

                    this.hopWorld();
                    return 1000;
                }
            }
        }
    }

    private SkillToTrain getNextSkill() {
        SkillToTrain[] scripts = this.getSkillsToTrain();
        Arrays.sort(scripts, (a, b) -> {
            return Integer.compare(b.getPriority(), a.getPriority());
        });
        return scripts[0];
    }

    private SkillToTrain[] getSkillsToTrain() {
        SkillToTrain[] skills = new SkillToTrain[]{new SkillToTrain(new Combat(true), () -> {
            return Player.getLocal().getCombatLevel() >= 14;
        }), new SkillToTrain(new Firemaker(), Skill.FIREMAKING, 25), new SkillToTrain(new Chopper(true), Skill.WOODCUTTING, 10), new SkillToTrain(new Fisher(true), Skill.FISHING, 20)};
        return skills;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "AccountTrainer Version 0.24");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        if (this.currentSkill != null && this.currentSkill.script instanceof PaintHandler) {
            ((PaintHandler) this.currentSkill.script).onPaint(g);
        }

    }

    public String runtimeInfo() {
        return this.currentSkill != null && this.currentSkill.script instanceof ScriptRuntimeInfo ? ((ScriptRuntimeInfo) this.currentSkill.script).runtimeInfo() : this.timer.getElapsedString() + "</th><th>Mage: " + Player.getLocal().getCombatLevel() + "</th>";
    }

    private void hopWorld() {
        ScriptUtils.interruptCurrentLoop();
        if (Dialogue.isInDialouge()) {
            WebWalking.walkTo(Locations.BANK_LUMBRIDGE, new Tile[0]);
        } else {
            Widgets.closeTopInterface();
            WorldHopping.hop(WorldHopping.getRandomP2p());
        }
    }

    public int getTotalLevel() {
        return Arrays.stream(Skill.values()).mapToInt(Skill::getRealLevel).sum();
    }
}
