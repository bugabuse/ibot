package com.farm.scripts.gambling.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Trade;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.gambling.Gambling;

import java.util.HashMap;

public class TradeStrategy extends Strategy implements MessageListener {
    public static PaintTimer inTradeTimer = new PaintTimer();
    public static HashMap<String, Integer> penalties = new HashMap();
    private ItemContainer inTradeContainer = null;
    private String lastTradedPlayer = "";
    private String playerToTrade = "";

    public TradeStrategy() {
        Gambling.get().addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (GamblingStrategy.isBettingClosed()) {
            if (Trade.isOpen() && !Trade.getOponentName().toLowerCase().equalsIgnoreCase(GamblingStrategy.winner)) {
                Trade.decline();

            }

        } else {
            if (Trade.isOpen()) {
                if (inTradeTimer == null) {
                    inTradeTimer = new PaintTimer();
                }

                int secondsToTrade = Trade.getOpenedScreen() == 2 ? 10 : 20;
                if (inTradeTimer.getElapsedSeconds() > (long) secondsToTrade) {
                    this.penalty(2);
                    return;
                }

                if (Trade.hasOtherAccepted()) {
                    if (Trade.getOpenedScreen() == 1) {
                        if (Trade.getOponentItems().getAll().size() == 1 && Trade.getOponentItems().getCount(new int[]{995}) > 0) {
                            this.inTradeContainer = Trade.getOponentItems();
                            this.lastTradedPlayer = Trade.getOponentName();
                            Trade.accept();
                            inTradeTimer.reset();
                        } else {
                            this.penalty(1);
                        }
                    } else {
                        Trade.accept();
                    }
                } else if (Trade.getOpenedScreen() == 1) {
                    this.inTradeContainer = Trade.getOponentItems();
                    this.lastTradedPlayer = Trade.getOponentName();
                } else {
                    Trade.accept();
                    inTradeTimer.reset();
                }
            } else {
                inTradeTimer = null;
            }

        }
    }

    private void penalty(int points) {
        Integer orDefault = this.getPenaltyPoints(Trade.getOponentName());
        penalties.put(Trade.getOponentName().toLowerCase(), orDefault + points);
        this.inTradeContainer = null;
        this.lastTradedPlayer = null;
        inTradeTimer = null;
        if (Trade.isOpen()) {
            Trade.decline();
        }

    }

    private int getPenaltyPoints(String oponentName) {
        return (Integer) penalties.getOrDefault(oponentName.toLowerCase(), 0);
    }

    public void onMessage(String message) {
        System.out.println(message);
        if (GamblingStrategy.winner == null) {
            if (message.contains("declined trade") && this.lastTradedPlayer != null) {
                this.penalty(1);
            }

            if (message.contains("Accepted trade") && this.lastTradedPlayer != null && this.inTradeContainer != null) {
                int amount = (Integer) GamblingStrategy.playerBets.getOrDefault(this.lastTradedPlayer, 0);
                GamblingStrategy.startTimer();
                GamblingStrategy.playerBets.put(this.lastTradedPlayer, amount + this.inTradeContainer.getCount(new int[]{995}));
            }

            try {
                if (GamblingStrategy.isBettingClosed()) {
                    Trade.decline();
                    return;
                }

                if (message.toLowerCase().contains("wishes to trade") && !Trade.isOpen()) {
                    String name = StringUtils.formatColorsString(message.toLowerCase()).split(" wishes to trade")[0];
                    name = StringUtils.format(name);

                    if (this.getPenaltyPoints(name) >= 5) {

                        return;
                    }

                    Player player = Players.get(name);
                    if (player != null && player.getPosition().distance() <= 1) {
                        player.interact("Trade");
                    }
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }

        }
    }
}
