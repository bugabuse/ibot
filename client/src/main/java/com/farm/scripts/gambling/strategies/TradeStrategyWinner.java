package com.farm.scripts.gambling.strategies;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Trade;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.gambling.Gambling;

public class TradeStrategyWinner extends Strategy implements MessageListener {
    public TradeStrategyWinner() {
        Gambling.get().addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (GamblingStrategy.winner != null) {
            if (Trade.isOpen()) {
                if (Trade.isOpen()) {
                    if (!Trade.getOponentName().equalsIgnoreCase(GamblingStrategy.winner)) {
                        Trade.decline();
                        return;
                    }

                    if (Trade.getOpenedScreen() == 1) {
                        int winnings = 50;
                        int current = Trade.getMyItems().getCount(new int[]{995});
                        if (current > winnings) {
                            Trade.decline();
                        } else if (current < winnings) {
                            Trade.add(995, winnings - current);
                        } else {
                            Trade.accept();
                        }
                    } else {
                        Trade.accept();
                    }
                }

            }
        }
    }

    public void onMessage(String message) {
        if (GamblingStrategy.winner != null) {
            try {
                if (message.contains("Accepted trade")) {
                    GamblingStrategy.openNewBetting();
                }

                if (message.toLowerCase().contains("wishes to trade") && !Trade.isOpen()) {
                    String name = StringUtils.formatColorsString(message.toLowerCase()).split(" wishes to trade")[0];
                    name = StringUtils.format(name);

                    if (!name.equalsIgnoreCase(GamblingStrategy.winner)) {
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
