package com.farm.scripts.magictrainer.strategies;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.util.Sorting;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;

import java.util.ArrayList;
import java.util.Iterator;

public class TrainMagicStrategy extends Strategy {
    public boolean active() {
        return true;
    }

    protected void onAction() {
        Magic spell = this.getMagicSpell();
        if (spell.select()) {
            Npc npc = this.findAttackableNpc("Grizzly bear");
            if (npc != null) {
                npc.interact("Cast");
                Time.sleep(() -> {
                    return Player.getLocal().getAnimation() != -1;
                });
                Time.sleep(1200, 1500);
            }
        }

    }

    private Magic getMagicSpell() {
        if (Skill.MAGIC.getRealLevel() >= 3) {
            return Magic.CONFUSE;
        } else if (Skill.MAGIC.getRealLevel() >= 11) {
            return Magic.WEAKEN;
        } else {
            return Skill.MAGIC.getRealLevel() >= 19 ? Magic.CURSE : Magic.WIND_STRIKE;
        }
    }

    private Npc findAttackableNpc(String name) {
        return this.findAttackableNpc((n) -> {
            return n.getName().equalsIgnoreCase(name);
        });
    }

    private Npc findAttackableNpc(Filter<Npc> filter) {
        ArrayList<Npc> available = new ArrayList();
        Iterator var3 = Npcs.getAll((n) -> {
            return n.isInteractingWithMe() && filter.accept(n);
        }).iterator();

        while (var3.hasNext()) {
            Npc npc = (Npc) var3.next();
            available.add(npc);
        }

        return available.size() > 0 ? (Npc) Sorting.getNearest(available) : (Npc) Sorting.getNearest(Npcs.getAll((n) -> {
            return !n.isInCombat() && filter.accept(n);
        }));
    }
}
