package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Character;
import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Sorting;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;

import java.util.ArrayList;
import java.util.Iterator;

public class Combat {
    public static boolean isSpecialEnabled() {
        return Config.get(301) == 1;
    }

    public static boolean setSpecialAttack(boolean enabled) {
        if (isSpecialEnabled() == enabled) {
            return true;
        } else {
            if (GameTab.COMBAT.open() && Time.sleep(1000, () -> {
                return Widgets.get(593, 30) != null;
            })) {
                Widgets.get(593, 30).interact("Use <col=00ff00>Special Attack</col>");
            }

            return Time.sleep(1300, () -> {
                return isSpecialEnabled() == enabled;
            });
        }
    }

    public static int getSpecialAttackEnergy() {
        return Config.get(300) / 10;
    }

    public static boolean isInCombat() {
        Character attacking = getCharacterImAttacking();
        return Player.getLocal().isInCombat() && attacking != null;
    }

    public static boolean isUnderAttack() {
        return getAttackingCharacters().size() > 0;
    }

    public static Npc findAttackableNpc(String name) {
        return findAttackableNpc((n) -> {
            return n.getName().equalsIgnoreCase(name);
        });
    }

    public static Npc findAttackableNpc(Filter<Npc> filter) {
        ArrayList<Npc> available = new ArrayList();
        Iterator var2 = Npcs.getAll((n) -> {
            return n.isInteractingWithMe() && n.isReachable() && filter.accept(n);
        }).iterator();

        while (var2.hasNext()) {
            Npc npc = (Npc) var2.next();
            available.add(npc);
        }

        return available.size() > 0 ? (Npc) Sorting.getNearest(available) : (Npc) Sorting.getNearest(Npcs.getAll((n) -> {
            return !n.isInCombat() && n.isReachable() && filter.accept(n);
        }));
    }

    public static ArrayList<Npc> getAttackingNpcs() {
        ArrayList<Npc> temp = new ArrayList();
        Iterator var1 = Npcs.getAll().iterator();

        while (var1.hasNext()) {
            Npc npc = (Npc) var1.next();
            if (npc.isInteractingWithMe() && npc.isInCombat()) {
                temp.add(npc);
            }
        }

        return temp;
    }

    public static Character getCharacterImAttacking() {
        return Player.getLocal().isInCombat() ? Player.getLocal().getInteracting() : null;
    }

    public static ArrayList<Character> getAttackingCharacters() {
        ArrayList<Character> temp = new ArrayList();
        Iterator var1 = Npcs.getAll().iterator();

        while (var1.hasNext()) {
            Npc npc = (Npc) var1.next();
            if (npc.isInteractingWithMe() && npc.isInCombat()) {
                temp.add(npc);
            }
        }

        var1 = Players.getAll().iterator();

        while (var1.hasNext()) {
            Player npc = (Player) var1.next();
            if (npc.isInteractingWithMe() && npc.isInCombat()) {
                temp.add(npc);
            }
        }

        return temp;
    }
}
