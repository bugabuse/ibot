package com.farm.scripts.quester.quests.tutorialnew.strategy;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Prayer;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorialnew.TutorialStateNew;

public class BrotherBrace extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialStateNew.getState()) {
            case TALK_TO_BROTHER_BRACE:
            case TALK_TO_BROTHER_BRACE_1_5:
            case TALK_TO_BROTHER_BRACE_2:
            case TALK_TO_BROTHER_BRACE_3:
            case TALK_TO_BROTHER_BRACE_4:
                this.doTalking();
                break;
            case LEAVE_CHURCH:
                this.leave();
                break;
            case USE_ALTAR:
                this.useAltar();
                break;
            case BURY_BONES:
                this.buryBones();
        }

    }

    private void buryBones() {
        Inventory.get(24655).interact("Bury");
    }

    private void useAltar() {
        Prayer.THICK_SKIN.setEnabled(true);
        Time.sleep(2000, 3000);
        GameObjects.get("Altar").interact("Pray");
        Time.sleep(3000, 4000);
    }

    private void leave() {
        GameObjects.get("Door", new Tile(1720, 6115)).interactAndWaitDisappear("Open");
    }

    private void doTalking() {
        if (Npcs.get("Brother Brace") != null) {
            if (Dialogue.talkTo("Brother Brace", true)) {
                Dialogue.goNext(new String[0]);
            }
        } else {
            Walking.walkTo(new Tile(1717, 6114, 0), 7);
        }

    }

    private GameObject getOpenedDoors() {
        GameObject doors = GameObjects.get("Large door", new Tile(3129, 3107));
        return doors != null && doors.getDefinition() != null && StringUtils.containsEqual("Open", doors.getDefinition().actions) ? doors : null;
    }
}
