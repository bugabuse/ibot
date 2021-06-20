package com.farm.scripts.mulereceiver.strategy;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.*;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.*;
import com.farm.ibot.api.util.string.DynamicString;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.container.WithdrawContainer;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.Session;
import com.farm.ibot.scriptutils.mule.MuleUtils;
import com.farm.scripts.mulereceiver.HandlingStatement;
import com.farm.scripts.mulereceiver.MuleReceiver;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TradeStrategy extends Strategy implements MessageListener {
    public static final int PACKAGE_MULE_RECEIVE_VARROCK = 1;
    public static final int PACKAGE_MULE_RESUPPLY_VARROCK = 2;
    public static final int PACKAGE_MULE_RESUPPLY_VARROCK_5k = 11;
    public static final int PACKAGE_MULE_RESUPPLY_VARROCK_8k = 16;
    public static final int PACKAGE_MULE_RECEIVE_FALADOR = 3;
    public static final int PACKAGE_MULE_RECEIVE_RIMMINGTON = 15;
    public static final int PACKAGE_GLOBAL_PROXY_MULE_RECEIVE_VARROCK = 4;
    public static final int PACKAGE_GLOBAL_MULE_RESUPPLY_BOND = 5;
    public static final int PACKAGE_MULE_RECEIVE_SALTPETRE = 6;
    public static final int PACKAGE_GLOBAL_MULE_RECEIVE_VARROCK = 7;
    public static final int PACKAGE_GLOBAL_MULE_RESUPPLY_100k = 8;
    public static final int PACKAGE_GLOBAL_MULE_RESUPPLY_500k = 12;
    public static final int PACKAGE_GLOBAL_MULE_RESUPPLY_1000k = 13;
    public static final int PACKAGE_GLOBAL_MULE_RESUPPLY_2_MILLION_GOLD = 10;
    public static final int PACKAGE_GLOBAL_MULE_RESUPPLY_3_MILLION_GOLD = 18;
    public static final int PACKAGE_GLOBAL_MULE_RESUPPLY_5_MILLION_GOLD = 17;
    public static final int PACKAGE_GLOBAL_MULE_RESUPPLY_10_MILLION_GOLD = 19;
    public static final int PACKAGE_GLOBAL_PROXY_MULE_RECEIVE_VARROCK_MAIN = 9;
    public static final int PACKAGE_GLOBAL_MULE_RECEIVE_VARROCK_FARMING = 14;
    public static TradeData currentTrade = null;
    public static int tradesReceived = 0;
    public static int tradesGiven = 0;
    public static PaintTimer currentTradeTimer = new PaintTimer();
    public static DynamicString bondPrice = new WebConfigDynamicString("bond_price");
    public boolean onlyP2p = false;
    public int tradeOnlyWorld = -1;
    public int[] allowedPackages = new int[]{1};
    public HandlingStatement handlingStatement = null;
    AlchStrategy alchStrategy = new AlchStrategy();
    int lastLoginState = -1;
    long lastAccept = 0L;
    private PaintTimer timer = new PaintTimer();
    private PaintTimer afkTimer = new PaintTimer();
    private long lastWorldHop = -1L;
    private int attempts = 0;

    public TradeStrategy(BotScript script) {
        script.addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return true;
    }

    public void onAction() {

        if (Bank.hasCache() && !Bank.getCache().contains(995) && BankEnsureStrategy.loaded) {

            if (Client.isInGame() && this.onlyP2p && !Varbit.MEMBERSHIP_DAYS.booleanValue()) {
                ++this.attempts;
                Time.sleep(10000);
                if (this.attempts > 10) {
                    AccountData.current().autostartScript = AccountData.current().autostartScript + "_memeberhip";
                    AccountData.current().update();
                    Bot.get().getSession().setAccount((AccountData) null);

                    while (!Login.logout()) {
                        Time.sleep(1000);
                    }

                    Bot.get().getScriptHandler().stop();
                }

            } else {

                if (this.lastLoginState != Client.getLoginState()) {
                    this.lastLoginState = Client.getLoginState();
                    this.afkTimer.reset();
                }

                if (InputBox.isOpen()) {
                    InputBox.input("0");
                } else {

                    if (currentTrade == null) {

                        currentTrade = this.getToTrade();
                        if (currentTrade == null) {
                            if (this.alchStrategy.active()) {
                                if (!this.alchStrategy.isSleeping()) {
                                    this.alchStrategy.execute();
                                }
                            } else if (this.afkTimer.getElapsedSeconds() > 60L && MuleReceiver.get().getStartArguments().contains("main")) {
                                Bot.get().getScriptHandler().loginRandom.active = false;
                                Bot.get().getScriptHandler().antiKick.active = false;
                                Login.logout();
                            }

                        } else {
                            currentTradeTimer.reset();
                        }
                    } else {
                        Bot.get().getScriptHandler().loginRandom.active = true;
                        Bot.get().getScriptHandler().antiKick.active = true;

                        if (currentTrade != null) {
                            if (currentTradeTimer.getElapsedSeconds() > 60L) {

                                Trade.decline();
                                WebUtils.sendRequest("mule/remove?username=" + currentTrade.username);
                                currentTrade = null;
                                currentTradeTimer.reset();
                                return;
                            }

                            if (!MuleUtils.isOnline(currentTrade.username)) {
                                currentTrade = null;

                                return;
                            }
                        }


                        this.afkTimer.reset();
                        if (!Objects.equals(currentTrade.muleHandlerName, AccountData.current().getGameUsername())) {
                            currentTrade = null;
                        } else if (Client.isInGame()) {
                            if (Client.getCurrentWorld() != currentTrade.tradeOnWorld) {
                                Debug.log("World hopping: " + Client.getCurrentWorld() + " to " + currentTrade.tradeOnWorld);
                                currentTradeTimer.reset();
                                if (System.currentTimeMillis() - this.lastWorldHop > 20000L) {
                                    WorldHopping.hop(currentTrade.tradeOnWorld);
                                    this.lastWorldHop = System.currentTimeMillis();
                                }

                            } else if (Trade.isOpen() || WebWalking.walkTo(MuleReceiver.RECEIVING_TILE, 1, new Tile[0])) {
                                if (Trade.isOpen()) {
                                    this.doTrade();
                                } else {
                                    Player toTrade = Players.get(currentTrade.username);
                                    if (toTrade == null) {
                                        if (currentTradeTimer.getElapsedSeconds() > 60L) {

                                            WebUtils.sendRequest("mule/remove/?username=" + currentTrade.username);
                                            currentTrade = null;
                                        }

                                    } else {

                                        if (!Bank.isOpen() && !Dialogue.isInDialouge()) {
                                            if (toTrade.getPosition().distance() <= 1 && !toTrade.isMoving() && toTrade.interact("Trade") && Time.sleep(10000, Trade::isOpen)) {
                                                currentTradeTimer.reset();
                                                Time.sleep(600, 800);
                                            }

                                        } else {
                                            Widgets.closeTopInterface();
                                            Walking.walkTo(MuleReceiver.RECEIVING_TILE, 0);
                                            Time.sleep(3000);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {

        }
    }

    private boolean isReceivingItems() {
        return currentTrade != null && (currentTrade.id == 1 || currentTrade.id == 15 || currentTrade.id == 14 || currentTrade.id == 3 || currentTrade.id == 7 || currentTrade.id == 4 || currentTrade.id == 6 || currentTrade.id == 9);
    }

    private void doTrade() {

        if (MuleUtils.isOnline(Trade.getOponentName()) && this.containsCurrentTrade()) {

            if (this.isReceivingItems()) {
                if (Trade.hasOtherAccepted() || Trade.getOpenedScreen() == 2) {
                    this.accept();
                }
            } else {
                if (Trade.getOpenedScreen() == 1 && !this.hasItems(currentTrade.id)) {
                    currentTrade = null;
                    Trade.decline();

                    return;
                }

                if (currentTrade.id == 2) {
                    if (Trade.add(995, 35000)) {
                        this.accept();
                    }
                } else if (currentTrade.id == 5) {
                    if (Trade.add(995, bondPrice.intValue())) {
                        this.accept();
                    }
                } else if (currentTrade.id == 11) {
                    if (Trade.add(995, 5000)) {
                        this.accept();
                    }
                } else if (currentTrade.id == 16) {
                    if (Trade.add(995, 14000)) {
                        this.accept();
                    }
                } else if (currentTrade.id == 8) {
                    if (Trade.add(995, 100000)) {
                        this.accept();
                    }
                } else if (currentTrade.id == 12) {
                    if (Trade.add(995, 500000)) {
                        this.accept();
                    }
                } else if (currentTrade.id == 13) {
                    if (Trade.add(995, 1000000)) {
                        this.accept();
                    }
                } else if (currentTrade.id == 10) {
                    if (Trade.add(995, 2000000)) {
                        this.accept();
                    }
                } else if (currentTrade.id == 17) {
                    if (Trade.add(995, 6000000)) {
                        this.accept();
                    }
                } else if (currentTrade.id == 19) {
                    if (Trade.add(995, 10000000)) {
                        this.accept();
                    }
                } else if (currentTrade.id == 18 && Trade.add(995, 3000000)) {
                    this.accept();
                }
            }


        } else {
            Debug.log("DoTrade - decline " + Trade.getOponentName());
            Trade.decline();
        }
    }

    private boolean containsCurrentTrade() {
        if (currentTrade != null) {
            AccountData data = (AccountData) Arrays.stream(Session.getAccountsCache()).filter((a) -> {
                return a.getGameUsername().equalsIgnoreCase(Trade.getOponentName());
            }).findAny().orElse(null);
            if (data != null) {
                Debug.log(data.getGameUsername() + " != " + currentTrade.username);
                return data.getGameUsername().equalsIgnoreCase(currentTrade.username);
            }
        }

        return false;
    }

    private void accept() {
        int screen = Trade.getOpenedScreen();
        Trade.accept();
        if (screen == 2 && Time.sleep(() -> {
            return !Trade.isOpen();
        })) {
            this.onMessage("accepted trade");
        }

    }

    private boolean hasItems(int tradeId) {
        WithdrawContainer container = new WithdrawContainer(Inventory.container().getItems());
        if (Trade.isOpen()) {
            container.merge(new WithdrawContainer(Trade.getMyItems().getItems()));
        }

        return tradeId == 2 && container.getCount(new int[]{995}) >= 40000 || tradeId == 11 && container.getCount(new int[]{995}) >= 5000 || tradeId == 16 && container.getCount(new int[]{995}) >= 14000 || tradeId == 8 && container.getCount(new int[]{995}) >= 100000 || tradeId == 12 && container.getCount(new int[]{995}) >= 500000 || tradeId == 13 && container.getCount(new int[]{995}) >= 1000000 || tradeId == 10 && container.getCount(new int[]{995}) >= 2000000 || tradeId == 17 && container.getCount(new int[]{995}) >= 6000000 || tradeId == 19 && container.getCount(new int[]{995}) >= 10000000 || tradeId == 18 && container.getCount(new int[]{995}) >= 3000000 || tradeId == 5 && container.getCount(new int[]{995}) >= bondPrice.intValue();
    }

    public void onMessage(String message) {
        if (System.currentTimeMillis() - this.lastAccept > 3000L && message.toLowerCase().contains("accepted trade") && currentTrade != null) {
            this.lastAccept = System.currentTimeMillis();
            if (this.isReceivingItems()) {
                ++tradesReceived;
            } else {
                ++tradesGiven;
            }

            WebUtils.sendRequest("mule/remove/?username=" + currentTrade.username);
            currentTrade = null;
        }

    }

    public TradeData getToTrade() {
        ArrayList<Integer> idList = new ArrayList();

        for (int i = 0; i < 101; ++i) {
            if (this.isHandling(i)) {
                idList.add(i);
            }
        }

        System.out.println("Allowed " + Arrays.toString(this.allowedPackages));
        System.out.println("Taking " + idList.toString());
        if (currentTrade != null && this.timer.getElapsedSeconds() <= 3L) {
            return currentTrade;
        } else {
            this.timer.reset();
            currentTradeTimer.reset();
            return (TradeData) WebUtils.sendObject(TradeData.class, idList.toArray(new Integer[0]), "muleassign/assign", "onlyworld=" + this.tradeOnlyWorld + "&muleName=" + AccountData.current().username);
        }
    }

    private boolean isHandling(int id) {
        if ((this.handlingStatement == null || this.handlingStatement.handling(id)) && Ints.contains(this.allowedPackages, id)) {
            return id != 2 && id != 11 && id != 16 && id != 5 && id != 8 && id != 12 && id != 13 && id != 17 && id != 19 && id != 18 && id != 10 || this.hasItems(id);
        } else {
            return false;
        }
    }
}
