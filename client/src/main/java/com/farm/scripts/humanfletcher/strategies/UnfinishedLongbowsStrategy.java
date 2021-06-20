package com.farm.scripts.humanfletcher.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.KeyBindingConfig;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.humanfletcher.Constants;
import com.farm.scripts.humanfletcher.HumanFletcher;
import com.farm.scripts.humanfletcher.Strategies;
import com.farm.scripts.humanfletcher.api.action.ActionContainer;
import com.farm.scripts.humanfletcher.api.action.ActionManager;
import com.farm.scripts.humanfletcher.api.util.ItemMover;

import java.awt.*;
import java.util.Random;

public class UnfinishedLongbowsStrategy extends Strategy implements InventoryListener {
    private Tile STAND_TILE = new Tile(3162, 3488, 0);
    private int lastAnimation = -1;
    private ActionManager actionManager;
    private boolean translated = false;
    private int bankSlot = 0;
    private int lastLongbowId = 0;
    private long lastDepositTime = System.currentTimeMillis();

    public UnfinishedLongbowsStrategy() {
        HumanFletcher.get().addEventHandler(new InventoryEventHandler(this));
        Random random = new Random(AccountData.seed());
        this.bankSlot = random.nextInt(20);
        this.STAND_TILE = Constants.STAND_TILES[(new Random(AccountData.seedForCurrentHour())).nextInt(Constants.STAND_TILES.length)];

        this.actionManager = new ActionManager();
        this.actionManager.load("humandata/fletching_longbows_unf.txt");
    }

    public void onItemAdded(Item item) {
        if (Skill.FLETCHING.getRealLevel() >= 10) {
            if (Skill.FLETCHING.getRealLevel() < 70 && !item.getDefinition().isStackable() && item.getName().contains("bow") && item.getName().contains("(u)")) {
                ++((HumanFletcher) HumanFletcher.get()).items;
            }

        }
    }

    public void onItemRemoved(Item item) {
    }

    protected void onAction() {
        if (Skill.FLETCHING.getRealLevel() < 70) {
            Constants.loadItems();
            KeyBindingConfig.setDefaults();
            if (Skill.FLETCHING.getRealLevel() >= 10) {
                if (GrandExchange.isOpen()) {
                    Bank.close();
                }

                if (((HumanFletcher) HumanFletcher.get()).setCamera(this.STAND_TILE)) {
                    this.fletchLongbows();
                }
            }
        }
    }

    protected void fletchLongbows() {
        if (!Inventory.contains(946)) {
            Bank.openAndWithdraw(new Item[]{new Item(946, 1)});
        } else if (this.STAND_TILE.distance() > 0) {
            WebWalking.walkTo(this.STAND_TILE, 0, new Tile[0]);
            Time.sleep(5000, 9000);
        } else if (Player.getLocal().getAnimation() != -1) {
            this.lastAnimation = Player.getLocal().getAnimation();
        } else if (Player.getLocal().getAnimation() == -1 && this.lastAnimation != -1) {

            this.actionManager.execute(this.actionManager.findReactionAction());
            Time.sleep(1, 800);
            this.lastAnimation = -1;
        } else {

            if (InputBox.isMakeItemDialogueOpen()) {

                if (Skill.FLETCHING.getRealLevel() < 25) {
                    Keyboard.type(52);
                } else {
                    this.actionManager.execute(this.actionManager.findWidgetOrKeyAction(17694736, "Make", 51));
                }


                this.actionManager.execute(this.actionManager.findIdlingAction());
                Time.sleep(2000, 3000);
            } else {

                if (Inventory.container().contains(Constants.LOGS_ID) && Inventory.container().contains(946)) {

                    if (Bank.isOpen()) {
                        this.actionManager.execute(this.actionManager.findWidgetOrKeyAction(786435, "Close", 27));
                        Time.sleep(() -> {
                            return !Bank.isOpen();
                        });
                    }

                    if (!GameTab.INVENTORY.open()) {
                        return;
                    }

                    if (!ItemMover.moveToSlot(Inventory.container(), 946, 0)) {
                        return;
                    }

                    Client.deselectItem();

                    ActionContainer[] itemOnItemAction = this.actionManager.findItemOnItemAction(9764864, 946, Constants.LOGS_ID);
                    if (itemOnItemAction != null) {
                        this.actionManager.execute(itemOnItemAction[0]);
                        this.actionManager.execute(itemOnItemAction[1]);
                    } else {

                        Inventory.get(946).interactWith(Inventory.get(Constants.LOGS_ID));
                    }

                    if (Skill.FLETCHING.getRealLevel() >= 25) {
                        this.actionManager.executeNextPreAtion();
                    }

                    Time.sleep(() -> {
                        return Widgets.get(270, 0) != null;
                    });

                } else {

                    if (Bank.isOpen()) {
                        if (!this.ensureBankItemsLocation()) {
                            return;
                        }

                        Item longbow = Inventory.container().get((i) -> {
                            return i.getName().toLowerCase().contains("bow (u)");
                        });
                        if (longbow != null) {
                            this.actionManager.execute(this.actionManager.findItemAction(983043, longbow.getId()));
                            this.lastLongbowId = longbow.getId();
                            this.lastDepositTime = System.currentTimeMillis();
                        }

                        Item junk = Inventory.container().get((i) -> {
                            return !i.getName().toLowerCase().contains("bow (u)") && i.getId() != Constants.LOGS_ID && i.getId() != 946;
                        });
                        if (junk != null) {
                            this.actionManager.execute(Strategies.longbowsStringingStrategy.actionManager.findWidgetAction(786474, "Deposit inventory"));
                            Time.waitInventoryChange();
                        }

                        if (!this.withdraw(Constants.LOGS_ID, 27)) {
                            return;
                        }

                        this.actionManager.execute(this.actionManager.findWidgetOrKeyAction(786435, "Close", 27));
                        Time.sleep(2000, () -> {
                            return Inventory.container().contains(Constants.LOGS_ID);
                        });
                    } else {
                        this.actionManager.execute(this.actionManager.findGameObjectAction(10060, "Bank"));
                        this.actionManager.executeNextPreAtion();
                        Time.sleep(Bank::isOpen);
                    }
                }

            }
        }
    }

    private boolean withdraw(int itemId, int amount) {
        Debug.log("Has space for: " + itemId + " " + amount + " :: " + Inventory.container().hasSpace(new Item(itemId, amount)));
        if (!Inventory.container().contains(itemId)) {
            if (!Inventory.container().hasSpace(new Item(itemId, amount)) && System.currentTimeMillis() - this.lastDepositTime > 1000L) {
                this.actionManager.execute(Strategies.longbowsStringingStrategy.actionManager.findWidgetAction(786474, "Deposit inventory"));
                Time.waitInventoryChange();
                return false;
            } else {
                this.actionManager.execute(this.actionManager.findItemAction(786445, itemId));
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean ensureBankItemsLocation() {
        if (!Bank.getContainer().contains(Constants.LOGS_ID, 27)) {
            HumanFletcher.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
            return true;
        } else {
            if (Bank.isOpen() && !this.translated) {
                if (Bank.getCache().getBoundsForSlot(5).x < 121) {
                    this.actionManager.execute(this.actionManager.findWidgetOrKeyAction(786435, "Close", 27));
                    return false;
                }

                this.actionManager.translateRectangle(new Rectangle(361, 83, 36, 32), Bank.getCache().getBoundsForSlot(this.bankSlot));
                this.translated = true;
            }

            if (!ItemMover.moveToSlot(Bank.getContainer(), Constants.LOGS_ID, this.bankSlot)) {
                return false;
            } else if (Varbit.WITHDRAW_X_AMOUNT.intValue() / 2 != 27) {
                Bank.depositAll();
                Bank.withdraw(Constants.LOGS_ID, 27);
                return false;
            } else if (Config.get(1666) != 12) {
                Widgets.forId(786466).interact("");
                return false;
            } else {
                return true;
            }
        }
    }
}
