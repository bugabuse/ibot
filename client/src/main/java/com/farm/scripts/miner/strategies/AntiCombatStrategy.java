package com.farm.scripts.miner.strategies;

import com.farm.ibot.api.methods.Combat;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.core.script.Strategy;

public class AntiCombatStrategy extends Strategy {
    public static int inCombatAmount = 0;
    public PaintTimer combatTimer = new PaintTimer(0L);

    public boolean active() {
        return true;
    }

    public void onAction() {
        if (Combat.isInCombat()) {
            Bank.openNearest();
            this.combatTimer.reset();
        } else if (this.combatTimer.getElapsedSeconds() < 30L) {
            ScriptUtils.interruptCurrentLoop();
        } else if (this.combatTimer.getElapsedSeconds() < 40L) {
            ++inCombatAmount;
            this.combatTimer.setTime(0L);
        }
    }
}
