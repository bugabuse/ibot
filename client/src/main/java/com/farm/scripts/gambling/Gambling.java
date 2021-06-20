package com.farm.scripts.gambling;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.methods.Trade;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.scripts.gambling.strategies.GamblingStrategy;
import com.farm.scripts.gambling.strategies.TradeStrategy;

import java.awt.*;
import java.util.Iterator;
import java.util.Map.Entry;

public class Gambling extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo {
    public PaintTimer timer = new PaintTimer();

    public Gambling() {
        super(Strategies.DEFAULT);
    }

    public void onStart() {

        Strategies.init(this);
        GamblingStrategy.openNewBetting();
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Gambling Version 0.01");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Trading: " + Trade.getOponentName());
        if (TradeStrategy.inTradeTimer != null) {
            this.drawString(g, "In trade: " + TradeStrategy.inTradeTimer.getElapsedString());
        }

        if (GamblingStrategy.isGameRunning()) {
            this.drawString(g, "Time until draw: " + GamblingStrategy.timeUntilDraw.getElapsedString().replace("-", ""));
        }

        this.drawString(g, "");
        this.drawString(g, "");
        this.drawString(g, "Bets:");
        Iterator var2 = GamblingStrategy.playerBets.entrySet().iterator();

        Entry entry;
        while (var2.hasNext()) {
            entry = (Entry) var2.next();
            this.drawString(g, (String) entry.getKey() + ": " + entry.getValue());
        }

        this.drawString(g, "");
        this.drawString(g, "");
        this.drawString(g, "Penalties:");
        var2 = TradeStrategy.penalties.entrySet().iterator();

        while (var2.hasNext()) {
            entry = (Entry) var2.next();
            this.drawString(g, (String) entry.getKey() + ": " + entry.getValue());
        }

    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Mage: " + Player.getLocal().getCombatLevel() + "</th>";
    }
}
