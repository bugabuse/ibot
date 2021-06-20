package com.farm.scripts.autogold.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Trade;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.web.WebClient;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.autogold.AutoGold;
import com.farm.scripts.autogold.web.Order;
import com.farm.scripts.autogold.web.OrderState;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;

public class TradeStrategy extends Strategy implements MessageListener {
    //private TradeRecorder recorder = new TradeRecorder();
    public Order[] orders = new Order[0];
    public PaintTimer ordersRefreshTimer = new PaintTimer(0L);
    public PaintTimer idleTimer = new PaintTimer(0L);
    public PaintTimer waitingForTradeTimer = new PaintTimer(0L);
    public PaintTimer inTradeTimer = new PaintTimer(0L);
    public Order currentOrder = null;
    public HashMap<String, Order> completedOrders = new HashMap();
    public HashMap<String, Integer> penaltyPoints = new HashMap();
    boolean inTrade = false;

    public TradeStrategy(AutoGold script) {
        script.addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        this.listenTrades();
        if (this.ordersRefreshTimer.getElapsedSeconds() > 2L) {
            this.orders = (Order[]) (new Gson()).fromJson(StringUtils.format((new WebClient()).downloadString(AutoGold.DOMAIN + "admin/7668e/orders/list/deliver")), Order[].class);
            if (this.orders == null) {
                this.orders = new Order[0];
            }

            this.ordersRefreshTimer.reset();
        }

        if (this.getPenaltyPoints(this.currentOrder) > 10) {
            (new Thread(() -> {
                (new WebClient()).downloadString(AutoGold.DOMAIN + "admin/7668e/orders/update?orderid=" + this.currentOrder.orderHash + "&state=" + OrderState.MANUAL_TRADE_PENALTY.name());
            })).start();
        } else if (this.ensureLocation()) {
            if (this.currentOrder == null && Trade.isOpen()) {
                Trade.decline();
            } else {
                if (this.currentOrder != null) {
                    this.doTrade();
                }

                if (this.currentOrder == null && this.orders.length == 0) {
                    this.idle();
                } else {
                    Bot.get().getScriptHandler().loginRandom.active = true;
                    Bot.get().getScriptHandler().antiKick.active = true;
                }

            }
        }
    }

    private void listenTrades() {
        if (Trade.isOpen() != this.inTrade) {
            this.inTradeTimer.reset();
            this.inTrade = true;
        }

    }

    public boolean ensureLocation() {
        if (Client.getCurrentWorld() != 335) {
            WorldHopping.hop(335);
            return false;
        } else if ((Trade.isOpen() || Player.getLocal().getPosition().distance(AutoGold.TRADE_TILE) <= 0) && Player.getLocal().getPosition().distance(AutoGold.TRADE_TILE) <= 5) {
            return true;
        } else {
            WebWalking.walkTo(AutoGold.TRADE_TILE, 0, new Tile[0]);
            return false;
        }
    }

    public void idle() {
        if (Client.isInGame() && this.idleTimer.getElapsedSeconds() > 15L && this.idleTimer.getElapsedSeconds() < 900L) {
            Bot.get().getScriptHandler().loginRandom.active = false;
            Bot.get().getScriptHandler().antiKick.active = false;
            Login.logout();
        } else {
            if (this.idleTimer.getElapsedSeconds() > 900L) {
                Bot.get().getScriptHandler().loginRandom.active = true;
                Bot.get().getScriptHandler().antiKick.active = true;
                if (Client.isInGame()) {
                    this.idleTimer.reset();
                }
            }

        }
    }

    public void doTrade() {
        this.idleTimer.reset();
        if (!Trade.isOpen()) {
            Player player = Players.get(this.currentOrder.gameUsername);
            if (player != null && player.getPosition().distance(AutoGold.TRADE_TILE) <= 1) {

                player.interact("Trade");
                Time.sleep(Trade::isOpen);
            }

            if (this.waitingForTradeTimer.getElapsedSeconds() > 60L) {
                this.waitingForTradeTimer.reset();
                this.addPenaltyPoints(this.currentOrder, 3);
                this.stopCurrentTrade();
            }

        } else if (Trade.isOpen() && this.currentOrder.state != OrderState.READY_FOR_DELIVERY) {
            Trade.decline();
        } else if (!this.currentOrder.gameUsername.equalsIgnoreCase(Trade.getOponentName())) {
            Trade.decline();
        } else if (Trade.hasAccepted() && this.inTradeTimer.getElapsedSeconds() > 60L) {
            this.addPenaltyPoints(this.currentOrder, 2);
            this.stopCurrentTrade();
            Trade.decline();
        } else {
            if (Trade.getOpenedScreen() == 1) {
                if (Trade.add(995, this.currentOrder.goldAmount * 1000000)) {
                    if (Trade.getValue() != this.currentOrder.goldAmount * 1000000) {
                        Trade.decline();
                        return;
                    }

                    Trade.accept();
                    Time.sleep(1000, 2000);
                }
            } else if (Trade.getOpenedScreen() == 2 && Trade.hasOtherAccepted()) {
                Trade.accept();
                if (Time.waitInventoryChange()) {
                    this.onTradeAccepted();
                }

                return;
            }

        }
    }

    public void onTradeAccepted() {
        if (this.currentOrder != null) {
            this.completedOrders.put(this.currentOrder.orderHash, this.currentOrder);
            String orderId = this.currentOrder.orderHash;
            (new Thread(() -> {
                (new WebClient()).downloadString(AutoGold.DOMAIN + "admin/7668e/orders/update?orderid=" + orderId + "&state=" + OrderState.DELIVERED.name());
            })).start();
            this.stopCurrentTrade();
        }

    }

    public void onMessage(String message) {
        if (message.contains("player declined")) {
            this.addPenaltyPoints(this.currentOrder, 1);
            this.stopCurrentTrade();
        }

        if (message.contains("Accepted trade.")) {
            this.onTradeAccepted();

        }

        if (message.toLowerCase().contains("wishes to trade") && !Trade.isOpen()) {
            String name = StringUtils.formatColorsString(message.toLowerCase()).split(" wishes to trade")[0];
            name = StringUtils.format(name);

            String str = "";
            char[] var4 = name.toCharArray();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                char c = var4[var6];
                str = str + c + ",";
            }


            if (this.currentOrder == null || !Trade.isOpen() && this.waitingForTradeTimer.getElapsedSeconds() > 15L) {
                this.currentOrder = this.tradeableOrderForName(name);
                this.waitingForTradeTimer.reset();
            }

            if (this.currentOrder != null && this.currentOrder.gameUsername.equalsIgnoreCase(name)) {
                this.waitingForTradeTimer.reset();
            }
        }

    }

    public Order orderForName(String name) {
        return (Order) Arrays.stream(this.orders).filter((o) -> {
            return name.equalsIgnoreCase(o.gameUsername);
        }).findAny().orElse(null);
    }

    public Order tradeableOrderForName(String name) {
        Order[] var2 = this.orders;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Order order = var2[var4];
            String str = "ORDER: ";
            char[] var7 = order.gameUsername.toCharArray();
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                char c = var7[var9];
                str = str + c + ",";
            }


        }

        Order order = (Order) Arrays.stream(this.orders).filter((o) -> {
            return !this.completedOrders.containsKey(o.orderHash) && name.equalsIgnoreCase(StringUtils.format(o.gameUsername));
        }).findAny().orElse(null);
        if (order == null || order.state != OrderState.READY_FOR_DELIVERY && order.state != OrderState.IN_TRADE) {
            return null;
        } else if (Inventory.getCount(995) / 1000000 < order.goldAmount) {
            (new Thread(() -> {
                (new WebClient()).downloadString(AutoGold.DOMAIN + "admin/7668e/orders/update?orderid=" + order.orderHash + "&state=" + OrderState.MANUAL_TRADE.name());
            })).start();
            return null;
        } else {
            return order;
        }
    }

    public int getPenaltyPoints(Order order) {
        return order != null ? (Integer) this.penaltyPoints.getOrDefault(order.gameUsername.toLowerCase(), 0) : -1;
    }

    public void addPenaltyPoints(Order order, int points) {
        if (order != null) {
            this.penaltyPoints.put(order.gameUsername.toLowerCase(), this.getPenaltyPoints(order) + points);
        }

    }

    public void stopCurrentTrade() {
        this.currentOrder = null;
        //this.recorder.stop();
    }
}
