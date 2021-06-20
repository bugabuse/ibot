package com.farm.scripts.winegrabber;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.StrategyScript;
import com.farm.scripts.mulereceiver.strategy.TradeStrategy;
import com.farm.scripts.winegrabber.strategy.GrabWineStrategy;

import java.awt.*;
import java.util.Iterator;

public class WineGrabber extends StrategyScript implements PaintHandler, ScriptRuntimeInfo, InventoryListener, MessageListener {
    public PaintTimer timer = new PaintTimer();
    public int wines = 0;
    public int fails = 0;

    public WineGrabber() {
        super(new Strategy[0]);
    }

    public void onStart() {
        this.addStrategy(new Strategy[]{new GrabWineStrategy()});
        this.addEventHandler(new InventoryEventHandler(this));
        this.addEventHandler(new MessageEventHandler(this));
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.2");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Wines: " + this.wines + "(" + this.timer.getHourRatio(this.wines) + ")");
        this.drawString(g, "Fails: " + this.fails + "(" + (int) (100.0D * (double) this.fails / (double) (this.wines + this.fails)) + "%)");
        this.drawString(g, "Our index: " + Client.getPlayerIndex());
        Iterator var2 = Players.getAll((p) -> {
            return p.getPosition().distance() < 5;
        }).iterator();

        while (var2.hasNext()) {
            Player player = (Player) var2.next();
            this.drawString(g, player.getName() + "(" + player.getIndex() + ")");
        }

        g.drawString("" + GrabWineStrategy.idleTimer.getElapsedSeconds(), GrabWineStrategy.ITEM_TILE.getScreenPoint().x, GrabWineStrategy.ITEM_TILE.getScreenPoint().y);
    }

    public int loopInterval() {
        return 5;
    }

    public String runtimeInfo() {
        return "Trading with: " + TradeStrategy.currentTrade;
    }

    public void onItemAdded(Item item) {
        if (item.getName().toLowerCase().contains("wine of zamorak")) {
            ++this.wines;
        }

    }

    public void onMessage(String message) {
        if (message.contains("Too late")) {
            ++this.fails;
        }

    }
}
