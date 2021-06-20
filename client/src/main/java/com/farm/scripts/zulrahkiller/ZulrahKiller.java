package com.farm.scripts.zulrahkiller;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.StrategyScript;
import com.farm.scripts.zulrahkiller.strategy.*;

import java.awt.*;

public class ZulrahKiller extends StrategyScript implements PaintHandler, ScriptRuntimeInfo, MessageListener {
    public PaintTimer timer = new PaintTimer();
    public PrayerStrategy prayerStrategy = new PrayerStrategy();
    private int kills = 0;

    public ZulrahKiller() {
        super(new Strategy[0]);
    }

    public void onStart() {
        this.addStrategy(new Strategy[]{new DeathWalkStrategy(this)});
        this.addStrategy(new Strategy[]{new ListenerStrategy()});
        this.addStrategy(new Strategy[]{this.prayerStrategy});
        this.addStrategy(new Strategy[]{new HealStrategy()});
        this.addStrategy(new Strategy[]{new WalkStrategy()});
        this.addStrategy(new Strategy[]{new EquipmentStrategy(this)});
        this.addStrategy(new Strategy[]{new AttackStrategy(this)});
        this.addStrategy(new Strategy[]{new BoardTravelStrategy()});
        this.addStrategy(new Strategy[]{new DialogueStrategy()});
        this.addStrategy(new Strategy[]{new LootStrategy()});
        this.addStrategy(new Strategy[]{new BankStrategy()});
        this.addEventHandler(new MessageEventHandler(this));
        this.getScriptHandler().webNotFoundRandom.active = false;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "'s Zulrah Killer");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Kills: " + this.kills);
        this.drawString(g, "");
        this.drawString(g, "");
    }

    public String runtimeInfo() {
        return "ZulrahKiller bitches!";
    }

    public int loopInterval() {
        return 10;
    }

    public void onMessage(String message) {

        if (message.toLowerCase().contains("your zulrah")) {
            ++this.kills;
        }

    }
}
