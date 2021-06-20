package com.farm.scripts.stronghold.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.stronghold.util.RewardBox;

import java.util.Arrays;

public class DoStrongholdStrategy extends Strategy {
    public static final String[] ANSWERS = new String[]{"Report the player", "Nobody", "Inform Jagex by emailing", "Don't type in my password", "Only on the Old School RuneScape website", "Don't give them the information and send an", "No.", "Authenticator and two-step login on my registered email", "Don't give them my password", "Secure my device and reset my password", "Don't give out your password to anyone", "Report the incident and do not", "No, you should never", "Don't tell them anything", "Don't share your information", "No way!", ""};

    public static RewardBox getRewardBox() {
        RewardBox[] var0 = RewardBox.values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            RewardBox box = var0[var2];
            if (!box.isRedeemed()) {
                return box;
            }
        }

        return null;
    }

    public static boolean isAtDangerousFloor() {
        return RewardBox.SECOND_FLOOR.tile.distance() < 100 || RewardBox.THIRD_FLOOR.tile.distance() < 100;
    }

    public static boolean isAtStronghold() {
        return Arrays.stream(RewardBox.values()).anyMatch((b) -> {
            return b.tile.distance() < 300;
        });
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        RewardBox box = getRewardBox();
        if (box == null) {
            this.leave();
        } else if (Dialogue.isInDialouge()) {
            Dialogue.goNext(ANSWERS);
        } else {
            Widget enterNextLevelWidget = Widgets.get((w) -> {
                return w.isRendered() && w.getText().contains("Yes - I know that it may be");
            });
            if (enterNextLevelWidget != null) {
                enterNextLevelWidget.interact("Yes");
                Time.sleep(3000);
            } else if (isAtStronghold() || Inventory.container().getCount(new String[]{"Jug of wine"}) > 10) {
                if (Client.getRunEnergy() < 30) {
                    Walking.setRun(isAtDangerousFloor());
                } else {
                    Walking.setRun(isAtDangerousFloor() || Client.getRunEnergy() > 50);
                }

                if (WebWalking.walkTo(box.tile, 3, new Tile[0])) {
                    if (!Inventory.container().hasSpace(new Item(995, 1))) {
                        Inventory.get("Jug of wine").interact("Drop");
                        Time.sleep(1000, 3000);
                    } else {
                        GameObjects.get(box.boxName).interact("Open");
                        Time.sleep(1000, 3000);
                    }
                }
            }
        }
    }

    private void leave() {
        if (isAtStronghold()) {
            GameObject o = GameObjects.get((i) -> {
                return i.isReachable() && (i.getName().contains("Goo covered vine") || i.getName().contains("Dripping vine") || i.getName().contains("Ladder")) && i.getActions().length > 0 && i.getActions()[0].contains("Climb-up");
            });
            if (o != null) {
                o.interact("Climb-up");
                Time.sleep(1000, 3000);
            }

        } else {
            if (!Bank.getCache().contains(995, 1000) && !Inventory.container().contains(995, 1000)) {

                if (AccountData.current() != null) {
                    AccountData.current().autostartScript = "FreeAgent_After_Stronghold";
                    WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + AccountData.current().username + "&script=FreeAgent_After_Stronghold");
                }

                while (!Login.logout()) {
                    Time.sleep(5000);
                }

                Bot.get().getSession().setAccount((AccountData) null);
                Bot.get().getScriptHandler().stop();
            }

        }
    }
}
