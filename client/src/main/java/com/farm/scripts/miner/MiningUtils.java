package com.farm.scripts.miner;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.google.common.primitives.Ints;

public class MiningUtils {
    public static final String[] AXES = new String[]{"Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe"};
    public static final int[] AXES_LVL = new int[]{1, 1, 6, 21, 31, 41};
    public static final int[] AXE_IDs = new int[]{1265, 1267, 1269, 1273, 1271, 1275};

    public static String getBestPickaxe() {
        String best = AXES[0];

        for (int i = 0; i < AXES.length; ++i) {
            if (Skill.MINING.getRealLevel() >= AXES_LVL[i] && (Bank.getContainer().getCount(new String[]{AXES[i]}) >= 1 || Inventory.container().getCount(new String[]{AXES[i]}) >= 1)) {
                best = AXES[i];
            }
        }

        return best;
    }

    public static boolean hasPickaxe() {
        return Inventory.container().contains((i) -> {
            return i.getName().contains(getBestPickaxe()) && Ints.contains(AXE_IDs, i.getId());
        });
    }
}
