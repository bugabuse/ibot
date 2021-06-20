package com.farm.scripts.combat.strategy;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.AttackMode;
import com.farm.ibot.api.methods.Combat;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.combat.Settings;
import org.apache.commons.lang3.ArrayUtils;

public class AttackStrategyNew extends Strategy {
    Npc npc;

    public boolean active() {
        return true;
    }

    public void onAction() {
        Walking.setRun(true);
        if (Player.getLocal().getCombatLevel() >= com.farm.scripts.combat.Combat.maxCombat) {
            Bot.get().getScriptHandler().startNextQueuedScript(com.farm.scripts.combat.Combat.instance);
        } else if (Settings.getSpot().area.contains(Player.getLocal().getPosition()) || WebWalking.walkTo(Settings.getSpot().area.centerTile(), 1, new Tile[0])) {
            if (Dialogue.isInDialouge()) {
                Walking.walkTo(Player.getLocal().getPosition(), -1);
            } else {
                if (Settings.usingMagic()) {
                    if (!this.ensureMagicAutospell()) {
                        return;
                    }
                } else {
                    this.ensureAttackStyle();
                }

                if (this.npc != null) {
                    Debug.log(this.npc.getAnimation());
                }

                if (this.npc == null || !this.npc.exists() || this.isDead(this.npc) || !Settings.getSpot().isMultiCombat && this.npc.getInteracting() != null && !this.npc.getInteracting().equals(Player.getLocal())) {
                    this.npc = Combat.findAttackableNpc((n) -> {
                        return n != null && !this.isDead(n) && Settings.getSpot().area.contains(n.getPosition()) && ArrayUtils.contains(Settings.getSpot().npcNames, n.getName());
                    });
                }

                if (Player.getLocal().getInteracting() == null || (this.npc == null || this.npc.getAnimation() == 5851) && !this.npc.isInCombat()) {
                    if (this.npc != null) {
                        if (!this.npc.isReachable() && Settings.getSpot().isWalkingToUnreavhableNpcs && !WebWalking.walkTo(this.npc.getPosition(), 2, new Tile[0])) {
                            return;
                        }

                        this.npc.interact("Attack");
                        Time.sleep(6000, () -> {
                            return Player.getLocal().getAnimation() != -1 && this.npc.isInCombat();
                        });
                    }

                }
            }
        }
    }

    private void ensureAttackStyle() {
        Skill toTrain = this.getAttackModeSkill();
        switch (toTrain) {
            case STRENGTH:
                AttackMode.AGGRESIVE.enable();
                break;
            case DEFENSE:
                AttackMode.DEFENSIVE.enable();
                break;
            case ATTACK:
                AttackMode.ACCURATE.enable();
        }

    }

    private boolean ensureMagicAutospell() {
        int index = 1;
        int configValue = 3;
        if (Skill.MAGIC.getRealLevel() >= 5) {
            index = 2;
            configValue = 5;
        }

        if (Skill.MAGIC.getRealLevel() >= 9) {
            index = 3;
            configValue = 7;
        }

        if (Skill.MAGIC.getRealLevel() >= 13) {
            index = 4;
            configValue = 9;
        }

        if (Config.get(108) != configValue) {
            if (!GameTab.COMBAT.open()) {
                return false;
            }

            Widget spellWidget = Widgets.get(201, 1, index);
            if (spellWidget != null && spellWidget.isRendered()) {
                spellWidget.interact("");
                Time.sleep(1200, 1500);
                return Config.get(108) == configValue;
            }

            Widget chooseSpell = Widgets.get(593, 26);
            if (chooseSpell != null && chooseSpell.isRendered()) {
                chooseSpell.interact("");
                Time.sleep(1200, 1500);
                return Config.get(108) == configValue;
            }
        }

        return Config.get(108) == configValue;
    }

    private boolean isDead(Npc npc) {
        return npc.getAnimation() == 5851 || npc.getAnimation() == 836 || npc.getAnimation() == 1795;
    }

    public Skill getAttackModeSkill() {
        Skill[] skills = new Skill[]{Skill.STRENGTH, Skill.ATTACK, Skill.DEFENSE};
        if (Skill.STRENGTH.getRealLevel() < 20) {
            if (Skill.ATTACK.getRealLevel() >= 5) {
                skills = new Skill[]{Skill.STRENGTH};
            } else {
                skills = new Skill[]{Skill.STRENGTH, Skill.ATTACK};
            }
        } else if (Skill.ATTACK.getRealLevel() < 20) {
            skills = new Skill[]{Skill.ATTACK};
        }

        Skill lowest = Skill.STRENGTH;
        Skill[] var3 = skills;
        int var4 = skills.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Skill skill = var3[var5];
            if (skill.getRealLevel() < lowest.getRealLevel()) {
                lowest = skill;
            }
        }

        return lowest;
    }
}
