package com.farm.scripts.birthday.strategies;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.impl.WidgetAction;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.birthday.BirthdayState;
import com.farm.scripts.quester.quests.romeojuliet.RomeoAndJuliet;

import java.util.Iterator;

public class BirthdayStrategy extends Strategy {
    public static final int COMB_ID = 24524;
    public static final int CAT_HAIR_ID = 24523;
    public static final int CAT_EARS_ID = 24522;
    private static final Tile TILE_GERTRUDE = new Tile(3152, 3413, 0);
    private static final Tile TILE_GERTRUDE_HOUSE = new Tile(3149, 3410, 0);
    private static final Tile TILE_IFFIE = new Tile(3205, 3418, 0);
    private int emoteIndex = 0;
    private boolean emotesDone = false;

    public boolean active() {
        return true;
    }

    protected void onAction() {
        switch (BirthdayState.getState()) {
            case START:
                this.stateStart();
                break;
            case TALK_TO_PHILLIPA:
                this.askAboutBday("Phillipa");
                break;
            case TAKE_COMB_AND_TALK_TO_IFFIE:
            case TALK_IFFIE_1:
            case TALK_IFFIE_2:
                this.makeCatHair();
                break;
            case TALK_TO_JULIET:
                this.talkJuliet();
                break;
            case PERFORM_EMOTES:
                this.performEmotes();
                break;
            case FINISHED:
                this.onFinish();
                return;
        }

    }

    private void onFinish() {
        if (!Inventory.contains(1990) && !Inventory.contains(1989)) {
            if (Locations.GRAND_EXCHANGE.distance() > 10) {
                return;
            }

            if (Bank.openNearest()) {
                if (Bank.getContainer().contains(1989)) {
                    Bank.withdraw(true, 1989, 500);
                    Time.waitInventoryChange();
                } else {
                    this.rebind();
                }
            }
        } else {

        }

    }

    private void talkJuliet() {
        if (Inventory.contains(24522)) {
            Inventory.get(24522).interact(ItemMethod.WEAR);
            Time.waitInventoryChange();
        } else if (Dialogue.contains("Try performing")) {
            this.performEmotes();
        } else {
            this.askAboutBday("Juliet");
        }
    }

    private void performEmotes() {
        if (!Dialogue.contains("Wow, thanks") && !this.emotesDone) {
            if (GameTab.EMOTES.open()) {
                WidgetAction.create(14155777, 12 + this.emoteIndex).send();
                this.emoteIndex = ++this.emoteIndex % 3;
                Time.sleep(1000, 1300);
            }
        } else {
            this.emotesDone = true;
            Dialogue.goNext(new String[0]);
        }
    }

    private void makeCatHair() {
        if (Inventory.contains(24523)) {
            this.talkToIffie();
        } else if (!Inventory.contains(24524)) {
            if (WebWalking.walkTo(TILE_GERTRUDE_HOUSE, 0, new Tile[0])) {
                GameObject table = GameObjects.get(37976);
                table.interact("");
                Time.waitInventoryChange();
            }

        } else if (BirthdayState.getState() == BirthdayState.TAKE_COMB_AND_TALK_TO_IFFIE) {
            this.talkToIffie();
        } else if (Dialogue.isInDialouge()) {
            Dialogue.goNext(new String[0]);
        } else {
            Npc gertrude = Npcs.get("Gertrude");
            if (gertrude != null) {
                if (gertrude.isReachable() && gertrude.getPosition().distance() < 4) {
                    Inventory.get(24524).interactWith(Npcs.get("Gertrude"));
                    Time.sleep(Dialogue::isInDialouge);
                } else {
                    WebWalking.walkTo(gertrude.getPosition(), 1, new Tile[0]);
                }
            } else {
                WebWalking.walkTo(TILE_GERTRUDE, 5, new Tile[0]);
            }

        }
    }

    private void talkToIffie() {
        if (WebWalking.walkTo(TILE_IFFIE, 3, new Tile[0])) {
            this.askAboutBday("Iffie");
        }
    }

    private void stateStart() {
        if (Inventory.isEmpty()) {
            this.askAboutBday("Juliet");
        } else {
            Iterator var1 = Inventory.container().getAll().iterator();

            while (var1.hasNext()) {
                Item item = (Item) var1.next();
                item.interact("Drop");
                Time.sleep(300, 600);
            }

        }
    }

    private void askAboutBday(String name) {
        Npc npc = Npcs.get(name);
        if (npc == null) {
            WebWalking.walkTo(RomeoAndJuliet.JULIET_TILE, 3, new Tile[0]);
        } else if (Dialogue.isInDialouge() || npc.isReachable() && npc.getPosition().distance() <= 3) {
            if (Dialogue.talkTo(name)) {
                Dialogue.goNext(new String[]{"Ask about the birthday party"});
            }

        } else {
            WebWalking.walkTo(npc.getPosition(), 1, new Tile[0]);
        }
    }

    private void rebind() {

        AccountData account = Bot.get().getSession().getAccount();
        if (account != null) {
            if (!Login.logout()) {
                return;
            }

            WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + account.username + "&script=BirthdayEvent_Done");
            Bot.get().getSession().setAccount((AccountData) null);
            Bot.get().getScriptHandler().stop();
        }

    }
}
