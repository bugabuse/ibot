package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class BrotherBrace extends Strategy {
    public boolean active() {
        return !ClickSettings.needToSolve();
    }

    public void onAction() {
        switch (TutorialState.getState()) {
            case TALK_TO_BROTHER_BRACE:
            case TALK_TO_BROTHER_BRACE_2:
            case TALK_TO_BROTHER_BRACE_3:
                this.doTalking();
                break;
            case LEAVE_CHURCH:
                this.leave();
        }

    }

    private void leave() {
        GameObjects.get("Door", new Tile(3122, 3103)).interactAndWaitDisappear("Open");
    }

    private void doTalking() {
        GameObject doors = this.getOpenedDoors();
        if (doors != null && doors.interactAndWaitDisappear("Open")) {
            Time.sleep(200, 500);
        }

        if (Npcs.get("Brother Brace") != null) {
            if (Dialogue.talkTo("Brother Brace", true)) {
                Dialogue.goNext(new String[0]);
            }
        } else {
            Walking.walkTo(new Tile(3130, 3109, 0), 5);
        }

    }

    private GameObject getOpenedDoors() {
        GameObject doors = GameObjects.get("Large door", new Tile(3129, 3107));
        return doors != null && doors.getDefinition() != null && StringUtils.containsEqual("Open", doors.getDefinition().actions) ? doors : null;
    }
}
