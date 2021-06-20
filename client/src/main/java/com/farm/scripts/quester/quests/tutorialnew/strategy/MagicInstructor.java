package com.farm.scripts.quester.quests.tutorialnew.strategy;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Magic;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorialnew.TutorialStateNew;

public class MagicInstructor extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialStateNew.getState()) {
            case TALK_TO_MAGIC_INTRUCTOR:
            case TALK_TO_MAGIC_INTRUCTOR_2:
            case TALK_TO_MAGIC_INTRUCTOR_3:
                this.doTalking();
                break;
            case GET_SOME_KFC_WINGS:
                this.attackChicken();
                break;
            case USE_HOME_TELEPORT:
                this.useMagicTeleport();
        }

    }

    private void useMagicTeleport() {
        Magic.LUMBRIDGE_HOME_TELEPORT.click();
        if (Time.sleep(() -> {
            return Player.getLocal().getAnimation() != -1;
        })) {
            Time.sleep(15000);
        }

    }

    private void attackChicken() {
        if (Walking.walkTo(new Tile(1732, 6099, 0), 2) && Magic.WIND_STRIKE.select() && Npcs.get("Chicken").interact("Cast")) {
            TutorialStateNew.waitStateChange();
        }

    }

    private void doTalking() {
        Npc n = Npcs.get("Magic Instructor");
        if (n != null && n.isReachable()) {
            if (Dialogue.talkTo("Magic Instructor", true)) {
                Dialogue.goNext(new String[]{"Yes.", "No, I'm not planning to do that."});
            }
        } else {
            WebWalking.walkTo(new Tile(1733, 6097, 0), 7, new Tile[0]);
        }

    }
}
