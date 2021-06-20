package com.farm.scripts.christmas.strategies;

import com.farm.ibot.api.accessors.*;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.interact.action.impl.ObjectAction;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Equipment;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.christmas.ChristmasEvent;
import com.farm.scripts.christmas.ChristmasState;

import java.util.Iterator;

public class ChristmasStrategy extends Strategy implements MessageListener {
    private static final Tile VARROCK_FOUNTAIN = new Tile(3219, 3425, 0);
    private static final Tile BAKER_TILE = new Tile(3193, 3399);
    private boolean useHooper = false;
    private boolean remindedAtDoors = false;

    public ChristmasStrategy() {
        ChristmasEvent.get().addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        switch (ChristmasState.getState()) {
            case START:
                this.stateStart();
                break;
            case TALK_TO_SANTA:
            case TALK_TO_SANTA_2:
                this.talkToSanta();
                break;
            case TALK_TO_BAKER:
                this.talkToBaker();
                break;
            case SKIP_DIALOGUES:
            case SKIP_DIALOGUES_2:
            case SKIP_DIALOGUES_3:
                this.skipDialogues();
                break;
            case TALK_TO_DOOR:
                this.talkToDoor();
                break;
            case CREATE_ITEMS:
            case PULL_LEVER:
                this.createItems();
                break;
            case NONE:
                this.empty();
                break;
            case TALK_TO_PAUL:
                this.talkToPaul();
                return;
            case FINISHED:
                this.onFinish();
        }

    }

    private void empty() {
        if (Config.get(2633) >= 30000000) {
            if (Inventory.getFreeSlots() >= 20) {
                Dialogue.goNext("Yes");
                ObjectAction.create(2, GameObjects.get(37591)).sendByMouse();
            } else {
                Iterator var1 = Inventory.container().getAll().iterator();

                while (var1.hasNext()) {
                    Item item = (Item) var1.next();
                    item.interact("Destroy");
                    Time.sleep(() -> {
                        return Widgets.get((w) -> {
                            return w.getText().contains("you sure");
                        }) != null;
                    });
                    Keyboard.type(49);
                    Time.waitInventoryChange();
                }

            }
        }
    }

    private void onFinish() {
        if (!Inventory.contains(1039) && !Inventory.contains(1038)) {
            if (Locations.GRAND_EXCHANGE.distance() > 10) {
                return;
            }

            this.rebind();
        } else {

        }

    }

    private boolean dropWhenTooMuch() {
        int[] ids = new int[]{24436, 24434, 24433, 24432};
        int[] var2 = ids;
        int var3 = ids.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            int i = var2[var4];
            if (Inventory.getCount(i) > 3) {
                this.drop(i);
                return false;
            }
        }

        return true;
    }

    private void drop(int id) {
        Inventory.get(id).interact("Destroy");
        Time.sleep(() -> {
            return Widgets.get((w) -> {
                return w.getText().contains("you sure");
            }) != null;
        });
        Keyboard.type(49);
        Time.waitInventoryChange();
    }

    private void talkToBaker() {
        if (WebWalking.walkTo(BAKER_TILE, 8, new Tile[0])) {
            this.talkTo(1034);
        }

    }

    private void talkToPaul() {
        if (WebWalking.canFindPath(VARROCK_FOUNTAIN)) {
            if (Dialogue.contains("return tomorrow")) {
                this.rebindTomorrow();
                return;
            }

            this.talkToSanta();
        } else {
            this.talkTo(9401);
        }

    }

    private void talkToSanta() {
        if (WebWalking.walkTo(VARROCK_FOUNTAIN, 10, new Tile[0])) {
            this.talkTo(1030);
        }

    }

    private void pullLever() {
        GameObjects.get(37588).interact("");
        Time.sleep(Dialogue::isInDialouge);
        Time.sleep(1000, 2000);
    }

    private void createItems() {
        if (WebWalking.canFindPath(Locations.GRAND_EXCHANGE)) {
            this.goBackToRoom();
        } else if (Inventory.contains(24442)) {
            if (Player.getLocal().getPosition().getX() < 3176) {
                GameObjects.get(37547).interact("Pass-through");
                Time.sleep(3000);
            } else {
                this.talkToDoor();
            }

        } else if (!Inventory.contains(24437) && !Inventory.contains(24438)) {
            if (Equipment.isEquipped((ix) -> {
                return ix.getId() == 24438 || ix.getId() == 24437 || ix.getId() == 24439 || ix.getId() == 24440;
            }, true)) {
                this.pickupKey();
                Time.waitInventoryChange();
            } else {
                GroundItem groundItem = GroundItems.get(24437);
                if (groundItem != null) {
                    this.pickItem(24437);
                    Time.waitInventoryChange();
                } else if (this.useHooper) {
                    this.createShield();
                } else if (this.dropWhenTooMuch()) {
                    if (Inventory.getCount(24436) >= 3) {
                        if (Inventory.getCount(24434) < 3) {
                            GameObjects.get(37553).interact("");
                            Time.waitInventoryChange();
                        } else if (Inventory.getCount(24433) < 3) {
                            GameObjects.get(37552).interact("");
                            Time.waitInventoryChange();
                        } else if (Inventory.getCount(24432) < 3) {
                            GameObjects.get(37550).interact("");
                            Time.waitInventoryChange();
                        } else {
                            this.useHooper = true;
                        }
                    } else if (Inventory.contains(24435)) {
                        GameObjects.get(37551).interact("");
                        Time.waitInventoryChange();
                    } else {
                        for (int i = 0; i < 5 - (Inventory.getCount(24435) + Inventory.getCount(24436)); ++i) {
                            this.pickItem(24435);
                        }

                    }
                }
            }
        } else {
            this.equipShield();
            Time.waitInventoryChange();
        }
    }

    private void rebindTomorrow() {

        AccountData account = Bot.get().getSession().getAccount();
        if (account != null) {
            if (!Login.logout()) {
                return;
            }

            WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + account.username + "&script=ChristmasEvent(tomorrow)");
            Bot.get().getSession().setAccount((AccountData) null);
            Bot.get().getScriptHandler().stop();
        }

    }

    private void rebind() {

        AccountData account = Bot.get().getSession().getAccount();
        if (account != null) {
            if (!Login.logout()) {
                return;
            }

            WebUtils.downloadObject(AccountData.class, "http://api.hax0r.farm:8080/accounts/bind/?username=" + account.username + "&script=ChristmasEvent_Done");
            Bot.get().getSession().setAccount((AccountData) null);
            Bot.get().getScriptHandler().stop();
        }

    }

    private void pickupKey() {

        if (Player.getLocal().getPosition().getX() < 3176) {
            if (Inventory.contains(24442)) {
                GameObjects.get(37547).interact("Pass-through");
                Time.sleep(3000);
            } else {
                GroundItems.get(24442).interact("Take");
                Time.waitInventoryChange();
            }
        } else {
            GameObjects.get(37547).interact("Pass-through");
            Time.sleep(3000);
        }

    }

    private void equipShield() {
        Item item = Inventory.container().get((i) -> {
            return i.getId() == 24437 || i.getId() == 24438;
        });
        item.interact("Wield");
        Time.waitInventoryChange();
    }

    private void createShield() {

        Item flour = Inventory.get(24436);
        Item egg = Inventory.get(24434);
        Item powder = Inventory.get(24433);
        Item sticks = Inventory.get(24432);
        if (this.dropWhenTooMuch()) {
            GroundItem groundItem = GroundItems.get(24437);
            if (groundItem != null) {
                groundItem.interact("Take");
                Time.waitInventoryChange();
            } else if (InputBox.isOpen()) {
                InputBox.input("3");
            } else if (this.remindedAtDoors && !Dialogue.isInDialouge()) {
                if (flour != null) {
                    flour.interactWith(GameObjects.get(37591));
                    if (Time.sleep(InputBox::isOpen)) {
                        InputBox.input("3");
                        Time.waitInventoryChange();
                    }

                } else if (egg != null) {
                    egg.interactWith(GameObjects.get(37591));
                    if (Time.sleep(InputBox::isOpen)) {
                        InputBox.input("3");
                        Time.waitInventoryChange();
                    }

                } else if (powder != null) {
                    powder.interactWith(GameObjects.get(37591));
                    if (Time.sleep(InputBox::isOpen)) {
                        InputBox.input("3");
                        Time.waitInventoryChange();
                    }

                } else if (sticks != null) {
                    sticks.interactWith(GameObjects.get(37591));
                    if (Time.sleep(InputBox::isOpen)) {
                        InputBox.input("3");
                        Time.waitInventoryChange();
                    }

                } else {
                    this.pullLever();
                }
            } else {
                this.talkToDoor();
                this.remindedAtDoors = true;
            }
        }
    }

    private void pickItem(int itemId) {
        GroundItem groundItem = GroundItems.get(itemId);
        if (groundItem != null) {
            groundItem.interact("Take");
            Time.waitInventoryChange();
            Time.sleep(600, 800);
        }

    }

    private void talkToDoor() {
        if (Time.sleep(3000, Dialogue::isInDialouge)) {
            Dialogue.goNext(new String[0]);
        } else {
            GameObject door = GameObjects.get(37598);
            door.interact("");
            Time.sleep(Dialogue::isInDialouge);
        }
    }

    private void skipDialogues() {
        if (Dialogue.isInDialouge()) {
            Dialogue.goNext(new String[0]);
        } else {
            if (this.atRoom()) {
                this.talkToDoor();
            } else {
                this.goBackToRoom();
            }

        }
    }

    private void goBackToRoom() {
        if (ChristmasState.isInState(ChristmasState.SKIP_DIALOGUES, ChristmasState.SKIP_DIALOGUES_2)) {
            this.talkToSanta();
        } else if (WebWalking.walkTo(new Tile(3193, 3389), 1, new Tile[0])) {
            GameObject trapDoor = GameObjects.get(37597);
            trapDoor.interact("");
            Time.sleep(3000);
        }

    }

    private boolean atRoom() {
        return GameObjects.get(37598) != null;
    }

    private void stateStart() {
        if (Inventory.isEmpty()) {
            if (WebWalking.walkTo(VARROCK_FOUNTAIN, 12, new Tile[0])) {
                this.talkToBill();
            }
        } else {
            Iterator var1 = Inventory.container().getAll().iterator();

            while (var1.hasNext()) {
                Item item = (Item) var1.next();
                item.interact("Drop");
                Time.sleep(10, 20);
            }

        }
    }

    private void talkToBill() {
        this.talkTo(3081);
    }

    private void talkTo(int npcId) {
        Npc bill = Npcs.get((n) -> {
            return n.getId() == npcId;
        });
        if (bill != null) {
            if (Dialogue.isInDialouge()) {
                Dialogue.goNext(new String[0]);
            } else {
                Debug.log("Dialogue " + Dialogue.isInDialouge());
                bill.interact("Talk-to");
                Time.sleep(Dialogue::isInDialouge);
            }
        }

    }

    public void onMessage(String message) {
        if (message.contains("lever") && message.contains("nothing")) {
            this.useHooper = false;
        }

    }
}
