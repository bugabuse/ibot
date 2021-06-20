package com.farm.scripts.runecrafter.strategies.crafting;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Trade;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.runecrafter.Constants;

public class IdleStrategy extends Strategy implements MessageListener {
    public String tradingWith = null;

    public IdleStrategy(BotScript script) {
        script.addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return (Trade.isOpen() || Inventory.getCount(7936) <= 0) && Inventory.contains(1438);
    }

    protected void onAction() {
        System.out.println("idle");
        if (WebWalking.walkTo(Constants.ALTAR_AIR, 1, new Tile[0])) {
            if (Dialogue.canClickContinue()) {
                Walking.walkTo(Player.getLocal().getPosition(), -1);
            }

            if (!Trade.isOpen()) {
                if (this.tradingWith != null) {
                    System.out.println("Trade: " + this.tradingWith);
                    Player player = Players.get(this.tradingWith);
                    if (player != null) {
                        if (player.getPosition().distance(Constants.ALTAR_AIR) <= 2 && player.interact("Trade")) {
                            Time.sleep(Trade::isOpen);
                        }
                    } else {
                        this.tradingWith = null;
                    }
                }

            } else {
                if (Trade.getOponentItems().contains(7936) || Trade.getOpenedScreen() == 2) {
                    Trade.accept();
                }

            }
        }
    }

    public void onMessage(String message) {
        if (message.contains("Accepted trade.")) {
            this.tradingWith = null;
        }

        if (message.toLowerCase().contains("wishes to trade") && !Trade.isOpen()) {
            System.out.println("Trade ask");
            String name = StringUtils.formatColorsString(message.toLowerCase()).split(" wishes to trade")[0];
            this.tradingWith = name;
        }

    }
}
