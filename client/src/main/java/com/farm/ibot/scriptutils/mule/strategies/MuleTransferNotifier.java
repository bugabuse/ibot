package com.farm.ibot.scriptutils.mule.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.util.TradeData;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.scriptutils.mule.MuleManager;

public class MuleTransferNotifier extends Strategy {
    private final MuleManager manager;
    public int queueIndex = Integer.MAX_VALUE;

    public MuleTransferNotifier(MuleManager manager) {
        this.manager = manager;
    }

    public boolean active() {
        boolean active = this.manager.getEnabledCondition().active() && Player.getLocal() != null && this.manager.tradeAction != 0;
        if (!active) {
            this.queueIndex = Integer.MAX_VALUE;
        }

        return active;
    }

    public void onAction() {
        if (Bot.get().getFpsData().isOverloaded()) {
            this.manager.setState(0);

        } else {
            try {
                Tile tile;
                if (this.manager.tradeAction == this.manager.giveToMuleActionId) {
                    if (this.manager.muleTransferListener.isTradeRestricted()) {
                        return;
                    }

                    tile = this.manager.receivingMuleTile;
                } else {
                    tile = this.manager.resupplyMuleTile;
                }

                int tradeOnWorld = -1;
                if (this.manager.tradeOnWorldGive != -1 && this.manager.tradeAction == this.manager.giveToMuleActionId) {
                    tradeOnWorld = this.manager.tradeOnWorldGive;
                }

                if (this.manager.tradeOnWorldResupply != -1 && this.manager.tradeAction == this.manager.supplyFromMuleActionId) {
                    tradeOnWorld = this.manager.tradeOnWorldResupply;
                }

                int world = Client.getCurrentWorld();
                world = WorldHopping.toExpandedWorldNumber(world);
                if (tradeOnWorld == -1) {
                    tradeOnWorld = world;
                }

                TradeData data = new TradeData(this.manager.tradeAction, world, Player.getLocal().getName());
                data.setTile(Player.getLocal().getPosition());
                data.distanceToTile = Player.getLocal().getPosition().distance(tile);
                data.requireMuleHandlerAssigned = this.manager.requireMuleHandlerAssigned;
                data.tradeOnWorld = tradeOnWorld;
                this.queueIndex = Integer.parseInt(WebUtils.uploadObject(data, "http://api.hax0r.farm:8080/mule/add"));

            } catch (Exception var5) {
                this.queueIndex = Integer.MAX_VALUE;
                var5.printStackTrace();
            }

            this.sleep(3000);
        }
    }

    public boolean isBackground() {
        return true;
    }
}
