package com.farm.scripts.stronghold;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.scripts.stronghold.strategies.DoStrongholdStrategy;
import com.farm.scripts.stronghold.util.RewardBox;

import java.awt.*;

public class StrongholdMoney extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();

    public StrongholdMoney() {
        super(Strategies.DEFAULT);
    }

    public void onStart() {

        Strategies.init(this);
        WebWalking.enableRunningCondition = WebWalking.RUNNING_MANUAL;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "StrongholdMoney Version 0.11");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
        this.drawString(g, "At stronghold: " + DoStrongholdStrategy.isAtStronghold());
        this.drawString(g, "At stronghold dangerous: " + DoStrongholdStrategy.isAtDangerousFloor());
        this.drawString(g, "");
        RewardBox[] var2 = RewardBox.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            RewardBox box = var2[var4];
            this.drawString(g, box.name() + ": " + box.isRedeemed());
        }

        this.drawString(g, "");
        this.drawString(g, "Trade state: " + Strategies.muleManager.getCurrentState());
        this.drawString(g, "Mule name: " + Strategies.muleManager.getMuleName());
        if (Strategies.muleManager != null && Strategies.muleManager.toWithdraw != null) {
            this.drawString(g, "Wealth: " + Strategies.muleManager.toWithdraw.calculateWealth());
        }

    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Floor: " + DoStrongholdStrategy.getRewardBox() + "</th> <th>" + this.getCurrentlyExecuting() + "</th>";
    }
}
