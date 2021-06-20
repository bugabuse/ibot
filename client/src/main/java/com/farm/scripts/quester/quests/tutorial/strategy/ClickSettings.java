package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Menu;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorial.TutorialState;

public class ClickSettings extends Strategy {
    public static boolean needToSolve() {
        return getTab() != null;
    }

    private static GameTab getTab() {
        if (Dialogue.canClickContinue()) {
            if (Menu.isVisible()) {
                Menu.close();
            }

            Dialogue.clickContinue();
        }

        switch (TutorialState.getState()) {
            case CLICK_SETTINGS:
            case OPEN_SETTINGS:
                return GameTab.OPTIONS;
            case OPEN_INVENTORY:
                return GameTab.INVENTORY;
            case OPEN_FRIENDS_TAB:
                return GameTab.FRIEND_LIST;
            case OPEN_IGNORE_TAB:
                return GameTab.ACCOUNT_MANAGEMENT;
            case OPEN_MAGIC_TAB:
                return GameTab.MAGIC;
            case OPEN_PRAYER_TAB:
                return GameTab.PRAYER;
            case OPEN_SKILLS_TAB:
                return GameTab.STATS;
            case OPEN_MUSIC_TAB:
                return GameTab.MUSIC;
            case OPEN_EMOTE_TAB:
                return GameTab.EMOTES;
            case OPEN_COMBAT_OPTIONS_TAB:
                return GameTab.COMBAT;
            case OPEN_QUEST_TAB:
                return GameTab.QUESTS;
            case OPEN_EQUIP_TAB:
                return GameTab.EQUIPMENT;
            case OPEN_ACCOUNT_MANAGEMENT:
                return GameTab.ACCOUNT_MANAGEMENT;
            default:
                return null;
        }
    }

    public boolean active() {
        return needToSolve();
    }

    public void onAction() {
        GameTab toOpen = getTab();
        if (toOpen == GameTab.OPTIONS && Client.getGameConfig().getResizableMode() != 1) {
            Mouse.click(688, 486, true);
        } else {
            if (toOpen != null) {
                toOpen.openByClick();
                Time.sleep(600, 800);
            }

        }
    }
}
