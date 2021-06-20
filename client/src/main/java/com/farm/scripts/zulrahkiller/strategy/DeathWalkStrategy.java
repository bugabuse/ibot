package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.zulrahkiller.api.Positions;

import java.awt.*;

public class DeathWalkStrategy extends Strategy implements PaintHandler, MessageListener {
    private static final String[] ITEMS_TO_WEAR = new String[]{"Ahrim's hood", "Ahrim's robetop", "Ahrim's robeskirt", "Wizard boots", "Combat brace", "Ring of wealth", "Imbued saradomin cape", "Trident", "Occult", "Malediction ward"};
    private final BotScript script;
    private boolean deathWalking;

    public DeathWalkStrategy(BotScript script) {
        this.script = script;
        script.getPaintHandlers().add(this);
        script.addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    public void onAction() {
        if (this.deathWalking) {
            ScriptUtils.interruptCurrentLoop();
            if (Positions.BOAT_TILE.distance() < 30) {
                this.collect();
            } else {
                this.walk();
            }
        }
    }

    private void collect() {
        if (Inventory.getFreeSlots() <= 0) {
            String[] var6 = ITEMS_TO_WEAR;
            int var2 = var6.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                String str = var6[var3];
                Item item = Inventory.container().get((i) -> {
                    return i.getName().contains(str);
                });
                if (item != null) {
                    item.interact(ItemMethod.WEAR);
                    Time.waitInventoryChange();
                }
            }

        } else if (Walking.walkTo(Positions.BOAT_TILE, 8)) {
            Npc npc = Npcs.get("Priestess Zul-Gwenwynig");
            npc.interact("Collect");
            Time.waitInventoryChange();
            if (Dialogue.contains("You left the shrine")) {
                this.deathWalking = false;
            }

        }
    }

    private void walk() {
        if (Inventory.container().contains("Zul-andra teleport")) {
            if (Widgets.closeTopInterface()) {
                Inventory.container().get("Zul-andra teleport").interact(ItemMethod.EAT);
                Time.sleep(() -> {
                    return Positions.BOAT_TILE.distance() < 30;
                });
            }

        } else if (Bank.openNearest()) {
            Bank.withdraw(ItemDefinition.forName("Zul-andra teleport").id, 1);
            Time.waitInventoryChange();
        }
    }

    public void onPaint(Graphics g) {
        this.script.drawString(g, "" + Config.get(843));
        if (this.deathWalking) {
            g.setColor(Color.red);
            this.script.drawString(g, "Death walking");
            g.setColor(Color.white);
        }

    }

    public void onMessage(String message) {
        if (message.toLowerCase().contains("oh dear, you are dead!")) {
            this.deathWalking = true;
        }

    }
}
