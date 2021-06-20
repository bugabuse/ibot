package com.farm.scripts.combat.strategy;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.impl.NpcAction;
import com.farm.ibot.api.methods.*;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.combat.Settings;
import org.apache.commons.lang3.ArrayUtils;

public class AttackStrategy extends Strategy {
    private boolean needToTalk = true;
    private Tile graveTile = null;

    public boolean active() {
        return true;
    }

    public void onAction() {
        System.out.println("Lop");
        if (GameObjects.get(39549) != null) {
            System.out.println("Death shit " + (Npcs.get((n) -> {
                return n.getId() == 9855;
            }) != null));
            if (!Dialogue.isInDialouge() && !this.needToTalk) {
                GameObjects.get(39549).interact("Use");
                Time.sleep(5000);
            } else if (Dialogue.isInDialouge()) {
                if (Dialogue.contains("I haven't finished")) {
                    this.needToTalk = true;
                    Time.sleep(Dialogue::isInDialouge);
                }

                Dialogue.goNext(new String[]{"How do I pay", "How long do I have", "How do I know what", "I think I'm done"});
                this.needToTalk = false;
            } else {
                (new NpcAction(9, Npcs.get((n) -> {
                    return n.getId() == 9855;
                }).getIndex())).sendByMouse();
            }

        } else if (Player.getLocal().getAnimation() != 836 && Skill.HITPOINTS.getCurrentLevel() > 0) {
            Npc grave = Npcs.get((n) -> {
                return n.getName().contains("Grave") || n.getId() >= 9900 && n.getId() < 10200 && (("" + n.getName()).equals("null") || ("" + n.getName()).equals(""));
            });
            if (grave != null) {
                System.out.println("" + grave.getName());
                System.out.println("Grave is there. " + grave.isInteractingWithMe());
                (new NpcAction(11, grave.getIndex())).sendByMouse();
                Time.sleep(5000);
                this.graveTile = null;
            } else {
                System.out.println("Grave not found");
                System.out.println("xd?");
                if (this.graveTile != null) {
                    WebWalking.walkTo(this.graveTile, 5, new Tile[0]);
                } else if (Inventory.get("Bronze sword") != null && !Equipment.isEquipped("Bronze sword")) {
                    Inventory.get("Bronze sword").interact(ItemMethod.WEAR);
                    Time.sleep(3000);
                } else if (Inventory.get("Wooden shield") != null && !Equipment.isEquipped("Wooden shield")) {
                    Inventory.get("Wooden shield").interact(ItemMethod.WEAR);
                    Time.sleep(3000);
                } else {
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

                    if (Settings.SPOT_CHICKENS.area.contains(Player.getLocal().getPosition()) || WebWalking.walkTo(Settings.SPOT_CHICKENS.area.centerTile(), 1, new Tile[0])) {
                        if (!Combat.isInCombat()) {
                            Npc npc = Combat.findAttackableNpc((n) -> {
                                return Settings.SPOT_CHICKENS.area.contains(n.getPosition()) && ArrayUtils.contains(Settings.SPOT_CHICKENS.npcNames, n.getName());
                            });
                            if (npc != null) {
                                npc.interact("Attack");
                                Time.sleep(4500, Combat::isInCombat);
                            }

                        }
                    }
                }
            }
        } else {
            this.graveTile = Player.getLocal().getPosition();
            Time.sleep(7000);
        }
    }

    public Skill getAttackModeSkill() {
        Skill[] skills = new Skill[]{Skill.STRENGTH, Skill.ATTACK, Skill.DEFENSE};
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
