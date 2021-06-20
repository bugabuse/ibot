package com.farm.scripts.autogold;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.StrategyScript;
import com.farm.scripts.autogold.strategies.StockChecker;
import com.farm.scripts.autogold.strategies.TradeStrategy;
import com.farm.scripts.autogold.strategies.UpdateMuleStrategy;
import com.farm.scripts.autogold.web.Order;

import java.awt.*;

public class AutoGold extends StrategyScript implements PaintHandler, ScriptRuntimeInfo, InventoryListener {
    public static final Tile TRADE_TILE = new Tile(3152, 3488, 0);
    public static int currentStock = -1;
    public static String DOMAIN = "https://autogold.io/";
    TradeStrategy tradeStrategy = new TradeStrategy(this);

    public AutoGold() {
        super(new Strategy[0]);
    }

    public void onStart() {
        Bot.get().getScriptHandler().loginRandom.autoWorldAssignEnabled = false;
        this.addEventHandler(new InventoryEventHandler(this));
        this.tradeStrategy = new TradeStrategy(this);
        this.addStrategy(new Strategy[]{this.tradeStrategy, new StockChecker(), new UpdateMuleStrategy()});
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "AutoGold.io 0.12");
        this.drawString(g, "" + this.tradeStrategy.idleTimer.getElapsedString());
        this.drawString(g, "Trade wait: " + this.tradeStrategy.waitingForTradeTimer.getElapsedString());
        this.drawString(g, "In trade: " + this.tradeStrategy.inTradeTimer.getElapsedString());
        this.drawString(g, "");
        if (this.tradeStrategy.currentOrder != null) {
            this.drawString(g, "Id: " + this.tradeStrategy.currentOrder.orderHash);
            this.drawString(g, "Username: " + this.tradeStrategy.currentOrder.gameUsername);
            this.drawString(g, "Amount:  " + this.tradeStrategy.currentOrder.goldAmount + "m");
            this.drawString(g, "Penalty points:  " + this.tradeStrategy.getPenaltyPoints(this.tradeStrategy.currentOrder));
        }

        this.drawString(g, "");
        if (this.tradeStrategy.orders != null && this.tradeStrategy.orders.length > 0) {
            Order[] var2 = this.tradeStrategy.orders;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Order order = var2[var4];
                this.drawString(g, order.gameUsername + " | " + order.orderHash);
            }
        }

    }

    public String runtimeInfo() {
        return null;
    }

    public void onItemAdded(Item item) {
    }
}
