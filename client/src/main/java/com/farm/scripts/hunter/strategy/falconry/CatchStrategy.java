package com.farm.scripts.hunter.strategy.falconry;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.listener.*;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.hunter.strategy.trap.DropStrategy;

public class CatchStrategy extends Strategy implements MessageListener, InventoryListener {
    public CatchStrategy(BotScript script) {
        script.addEventHandler(new MessageEventHandler(this));
        script.addEventHandler(new InventoryEventHandler(this));
    }

    public boolean active() {
        return !FalconGetStrategy.instance.active() && !(new DropStrategy()).active();
    }

    public void onAction() {
        Npc ourFalcon = this.getFalcon();
        if (ourFalcon != null) {
            if (ourFalcon.interact("Retrieve")) {
                Time.waitInventoryChange();
            }

        } else {
            Npc kebbit = null;
            if (Skill.HUNTER.getRealLevel() >= 57) {
                kebbit = Npcs.get("Dark kebbit");
            }

            if (kebbit == null) {
                kebbit = Npcs.get("Spotted kebbit");
            }

            if (kebbit != null) {
                if (kebbit.interact("Catch")) {
                    this.sleepForCatch();
                }

            }
        }
    }

    private boolean sleepForCatch() {
        boolean[] catched = new boolean[]{false};
        EventHandler event = new MessageEventHandler((message) -> {
            if (message.contains("falcon")) {

                catched[0] = true;
            }

        });
        return Time.sleep(() -> {
            event.listen();
            return catched[0];
        });
    }

    private Npc getFalcon() {
        return Npcs.get((npc) -> {
            return npc.getName().contains("Gyr Falcon");
        });
    }

    public void onMessage(String message) {
        if (message.contains("but just misses catching")) {
        }

        if (message.contains("try to catch the creature but it is too quick for you")) {
            FalconGetStrategy.needsFalcon = true;
        }

    }

    public void onItemAdded(Item item) {
    }
}
