package com.farm.scripts.bondbuyer.strategies;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.bondbuyer.BondBuyer;

public class RedeemBondStrategy extends Strategy implements MessageListener {
    public RedeemBondStrategy(MultipleStrategyScript buyer) {
        buyer.addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (Inventory.contains(13192)) {

            Widget widget = Widgets.get((w) -> {
                return w.isRendered() && w.getText().contains("1 Bond");
            });
            if (widget != null) {
                widget.interact("1 Bond");
                Time.sleep(4000, 6000);
                Widgets.get((w) -> {
                    return w.isRendered() && StringUtils.containsAny("Confirm", w.getActions());
                }).interact("");
            } else {
                if (!Widgets.closeTopInterface()) {
                    return;
                }

                Inventory.get(13192).interact(ItemMethod.EAT);
                Time.sleep(2000, 4000);
            }

        }
    }

    public void onMessage(String message) {
        if (message.contains("days of membership")) {
            BondBuyer.onMembershipActivated();
        }

    }
}
