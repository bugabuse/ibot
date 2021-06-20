package com.farm.scripts.humanfletcher.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.InputBox.MakeItemDialogue;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.Varbit;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.KeyBindingConfig;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.MathUtils;
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
import com.farm.scripts.humanfletcher.api.action.wrapper.PreWidgetAction;
import com.farm.scripts.humanfletcher.api.util.ItemMover;

import java.awt.*;
import java.util.Random;

public class LongbowsStringingStrategy extends Strategy implements InventoryListener, MessageListener {
    public ActionManager actionManager;
    private Tile STAND_TILE = new Tile(3162, 3488, 0);
    private boolean isIdling = false;
    private long lastAnimation = 0L;
    private boolean translated = false;
    private int bankSlot = 0;

    public LongbowsStringingStrategy() {
        HumanFletcher.get().addEventHandler(new InventoryEventHandler(this));
        HumanFletcher.get().addEventHandler(new MessageEventHandler(this));
        Random random = new Random(AccountData.seed());
        this.bankSlot = MathUtils.clamp(2 * random.nextInt(13) - 1, 0, 10);

        this.STAND_TILE = Constants.STAND_TILES[(new Random(AccountData.seedForCurrentHour())).nextInt(Constants.STAND_TILES.length)];
        this.actionManager = new ActionManager();
        this.actionManager.load("humandata/fletching_longbows.txt");
    }

    public void onItemAdded(Item item) {
        if (!item.getDefinition().isStackable() && item.getName().contains("bow") && !item.getName().contains("(u)")) {
            ++((HumanFletcher) HumanFletcher.get()).items;
        }

        if (Player.getLocal().getAnimation() != -1 && !Inventory.container().contains(1777)) {
            this.isIdling = true;

        }

    }

    public void onMessage(String message) {
        if (message.contains("congratulations")) {
            this.isIdling = true;
        }

    }

    public void onItemRemoved(Item item) {
        if (Skill.FLETCHING.getRealLevel() >= 10 && Player.getLocal().getAnimation() != -1 && !Inventory.container().contains(1777)) {
            this.isIdling = true;

        }

    }

    protected void onAction() {
        if (Skill.FLETCHING.getRealLevel() < 10) {
            this.fletchArrowSchafts();
        } else if (Skill.FLETCHING.getRealLevel() >= 70) {
            if (GrandExchange.isOpen()) {
                Bank.close();
            }

            Constants.loadItems();
            if (((HumanFletcher) HumanFletcher.get()).setCamera(this.STAND_TILE)) {
                this.fletchLongbows();
                KeyBindingConfig.setDefaults();
            }
        }
    }

    private void fletchArrowSchafts() {
        if (!WebWalking.walkTo(this.STAND_TILE, 10, new Tile[0])) {
            WebWalking.walkTo(this.STAND_TILE, 0, new Tile[0]);
            Time.sleep(5000, 9000);
        } else {
            if (Player.getLocal().getAnimation() != -1) {
                this.lastAnimation = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - this.lastAnimation >= 3200L) {

                if (Inventory.container().contains(946) && Inventory.container().contains(1511)) {
                    if (Bank.isOpen() || GrandExchange.isOpen()) {
                        Bank.close();
                    }

                    Inventory.get(946).interactWith(Inventory.get(1511));
                    Time.sleep(1200, 1800);
                    MakeItemDialogue.MAKE_ALL.selectAndExecute();
                    Time.sleep(1200, 1800);
                } else {

                    this.bankArrowSchafts();
                }

            }
        }
    }

    private void bankArrowSchafts() {
        if (Bank.open()) {
            if (!Bank.depositAllExcept(new int[]{946, 52})) {
                return;
            }

            if (!Inventory.contains(946)) {
                if (!Bank.getContainer().contains(946)) {
                    HumanFletcher.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                    return;
                }

                Bank.withdraw(946, 1);
            }

            if (!Inventory.contains(1511)) {
                if (!Bank.getContainer().contains(1511)) {
                    HumanFletcher.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
                    return;
                }

                Bank.withdraw(1511, 26);
            }
        }

    }

    protected void fletchLongbows() {
        Constants.loadItems();

        if (this.STAND_TILE.distance() > 0) {
            WebWalking.walkTo(this.STAND_TILE, 0, new Tile[0]);
            if (Time.sleep(8000, () -> {
                return this.STAND_TILE.distance() <= 0;
            })) {
                Time.sleep(1000, 2000);
            }

        } else {

            if (Player.getLocal().getAnimation() != -1) {
                this.lastAnimation = System.currentTimeMillis();
            }


            if (this.isIdling || System.currentTimeMillis() - this.lastAnimation >= 1800L) {

                if (this.isIdling) {
                    this.isIdling = false;

                    this.actionManager.execute(this.actionManager.findReactionAction());
                    Time.sleep(1, 800);
                }


                if (InputBox.isMakeItemDialogueOpen()) {

                    ActionContainer actionContainer = this.actionManager.findWidgetOrKeyAction(17694734, "String", 32).currentOrSplitAfter(PreWidgetAction.class);
                    this.actionManager.execute(actionContainer);
                    this.actionManager.execute(this.actionManager.findIdlingAction());
                    Time.sleep(2000, 3000);
                } else {

                    if (Inventory.container().contains(Constants.UNSTRUG_BOW_ID) && Inventory.container().contains(1777)) {

                        if (Bank.isOpen()) {
                            Bank.close();
                            Time.sleep(() -> {
                                return !Bank.isOpen();
                            });
                        }

                        if (!GameTab.INVENTORY.open()) {
                            return;
                        }

                        Client.deselectItem();

                        ActionContainer[] itemOnItemAction = this.actionManager.findItemOnItemAction(9764864, Constants.UNSTRUG_BOW_ID, 1777);
                        if (itemOnItemAction != null) {
                            this.actionManager.execute(itemOnItemAction[0]);
                            this.actionManager.execute(itemOnItemAction[1]);
                        } else {
                            Inventory.get(Constants.UNSTRUG_BOW_ID).interactWith(Inventory.get(1777));
                        }

                        this.actionManager.executeNextPreAtion();

                        Time.sleep(() -> {
                            return Widgets.get(270, 0) != null;
                        });

                    } else {

                        if (Bank.isOpen()) {
                            if (!this.ensureBankItemsLocation()) {
                                return;
                            }

                            if (Inventory.container().contains(Constants.BOW_ID)) {
                                this.actionManager.execute(this.actionManager.findWidgetAction(786474, "Deposit inventory"));
                            }

                            if (!Inventory.container().contains(Constants.UNSTRUG_BOW_ID)) {
                                if (!Inventory.container().hasSpace(new Item(Constants.UNSTRUG_BOW_ID, 14))) {
                                    this.actionManager.execute(this.actionManager.findWidgetAction(786474, "Deposit inventory"));
                                    Time.waitInventoryChange();
                                    return;
                                }

                                this.actionManager.execute(this.actionManager.findItemAction(786445, Constants.UNSTRUG_BOW_ID));
                            }

                            if (!Inventory.container().contains(1777)) {
                                if (!Inventory.container().hasSpace(new Item(1777, 14))) {
                                    this.actionManager.execute(this.actionManager.findWidgetAction(786474, "Deposit inventory"));
                                    Time.waitInventoryChange();
                                    return;
                                }

                                this.actionManager.execute(this.actionManager.findItemAction(786445, 1777));
                            }

                            Bank.close();
                            Time.sleep(2000, () -> {
                                return Inventory.container().contains(1777);
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
    }

    private boolean ensureBankItemsLocation() {
        if (Bank.getContainer().contains(Constants.UNSTRUG_BOW_ID, 24) && Bank.getContainer().contains(1777, 24)) {
            if (Bank.isOpen() && !this.translated) {
                if (Bank.getCache().getBoundsForSlot(5).x < 121) {
                    Bank.close();
                    return false;
                }

                this.actionManager.translateRectangle(new Rectangle(169, 119, 36, 32), Bank.getCache().getBoundsForSlot(this.bankSlot));
                this.actionManager.translateRectangle(new Rectangle(217, 119, 36, 32), Bank.getCache().getBoundsForSlot(this.bankSlot + 1));
                this.translated = true;
            }

            if (ItemMover.moveToSlot(Bank.getContainer(), Constants.UNSTRUG_BOW_ID, this.bankSlot) && ItemMover.moveToSlot(Bank.getContainer(), 1777, this.bankSlot + 1)) {
                if (Varbit.WITHDRAW_X_AMOUNT.intValue() / 2 != 14) {
                    Bank.depositAll();
                    Bank.withdraw(Constants.UNSTRUG_BOW_ID, 14);
                    return false;
                } else if (Config.get(1666) != 12) {
                    Widgets.forId(786466).interact("");
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            HumanFletcher.get().setCurrentlyExecutitng(Strategies.GRAND_EXCHANGE);
            return true;
        }
    }
}
