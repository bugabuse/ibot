package com.farm.scripts.woodcutter.strategy.main;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.woodcutter.ChopSettings;
import com.google.common.primitives.Ints;

public class BankStrategy extends Strategy {
    private static final String[] AXES = new String[]{"Bronze axe", "Iron axe", "Steel axe", "Mithril axe", "Adamant axe", "Rune axe", "Dragon axe"};
    private static final int[] AXES_LVL = new int[]{1, 1, 6, 21, 31, 41, 61};
    private static final int[] AXE_IDs = new int[]{1351, 1349, 1353, 1355, 1357, 1359, 6739};
    boolean hasAxe = false;
    long lastCheck = System.currentTimeMillis();

    public static String getBestAxe() {
        String best = "Bronze axe";

        for (int i = 0; i < AXES.length; ++i) {
            if (Skill.WOODCUTTING.getRealLevel() >= AXES_LVL[i] && (Bank.getCache().getCount(new String[]{AXES[i]}) >= 1 || Inventory.container().getCount(new String[]{AXES[i]}) >= 1)) {
                best = AXES[i];
            }
        }

        return best;
    }

    public boolean active() {
        return Inventory.isFull() && (!ChopSettings.isDroppingLogs() || !Inventory.container().contains((i) -> {
            return i.getName().toLowerCase().contains("log");
        })) || !this.hasAxe() || Inventory.contains(995, 1000);
    }

    public void onAction() {
        if (Bank.openNearest()) {
            if (Bank.open()) {
                String axe = getBestAxe();
                Bank.depositAllExcept((i) -> {
                    return i.getName().contains(axe) && Ints.contains(AXE_IDs, i.getId());
                });
                if (!Inventory.container().contains(axe) && Bank.getContainer().get(axe) != null) {
                    Bank.withdraw(false, Bank.getContainer().get(axe).getId(), 1);
                }
            }

        }
    }

    public boolean hasAxe() {
        if (!this.hasAxe || System.currentTimeMillis() - this.lastCheck > 5000L) {
            this.lastCheck = System.currentTimeMillis();
            this.hasAxe = Inventory.container().contains((i) -> {
                return i.getName().contains(getBestAxe()) && Ints.contains(AXE_IDs, i.getId());
            });
        }

        return this.hasAxe;
    }
}
