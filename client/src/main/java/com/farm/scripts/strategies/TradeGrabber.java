package com.farm.scripts.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.Trade;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.web.*;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;

public class TradeGrabber extends Strategy implements MessageListener {
    public static DepositSession currentSession;
    public static Tile RECEIVING_TILE = new Tile(3253, 3423, 0);
    public PaintTimer lastTradeRequestTimer = new PaintTimer(0L);
    public PaintTimer expiryTimer = new PaintTimer();
    private int failedTrades;

    public TradeGrabber(BotScript script) {
        script.addEventHandler(new MessageEventHandler(this));
    }

    public boolean active() {
        return Client.isInGame();
    }

    protected void onAction() {
        if (currentSession == null) {
            this.reset();
            ObjectRestResponse<DepositSession[]> availableSessions = (ObjectRestResponse) WebUtils.downloadObjectFromUrl((new TypeToken<ObjectRestResponse<DepositSession[]>>() {
            }).getType(), "http://localhost:8080/ingame/deposit/active");
            if (availableSessions != null && availableSessions.success && availableSessions.object != null) {
                currentSession = (DepositSession) Arrays.stream((DepositSession[]) availableSessions.object).filter((session) -> {
                    return session.getBotName().equalsIgnoreCase(Player.getLocal().getName());
                }).findAny().orElse(null);
                if (currentSession == null) {
                    currentSession = (DepositSession) Arrays.stream((DepositSession[]) availableSessions.object).filter((session) -> {
                        return session.getState() == DepositSessionState.WAIT_FOR_BOT;
                    }).findAny().orElse(null);
                }
            }

            if (currentSession == null) {
                this.sleep(1500);
            }

        } else {
            String botName;
            if (this.expiryTimer.getElapsedSeconds() <= 60L && this.failedTrades <= 3) {
                if (currentSession.getState().isCompleted()) {

                    if (this.updateState(currentSession.getState())) {
                        this.reset();
                    }

                } else if (currentSession.getState() == DepositSessionState.WAIT_FOR_BOT) {
                    botName = Player.getLocal().getName();
                    int world = Client.getCurrentWorld();
                    if (WebUtils.downloadString("http://localhost:8080/ingame/bot/deposit/action/botassign?actionId=" + currentSession.getHash() + "&botName=" + botName + "&world=" + world).contains("\"success\":true")) {
                        this.updateState(DepositSessionState.IN_PROGRESS);
                    }

                } else if (Trade.isOpen()) {
                    this.doTrade();
                } else if (this.updateState(DepositSessionState.IN_PROGRESS)) {
                    Player toTrade = Players.get(currentSession.getPlayerIngameName());
                    if (toTrade != null) {
                        if (!Bank.isOpen() && !Dialogue.isInDialouge()) {
                            if (toTrade.getPosition().distance() <= 1 && !toTrade.isMoving() && this.lastTradeRequestTimer.getElapsed() <= 7000L) {
                                this.expiryTimer.reset();
                                toTrade.interact("Trade");
                                Time.sleep(Trade::isOpen);
                            }

                        } else {
                            Widgets.closeTopInterface();
                            Walking.walkTo(RECEIVING_TILE, 1);
                            Time.sleep(3000);
                        }
                    }
                }
            } else {
                if (this.updateState(DepositSessionState.EXPIRED)) {
                    this.reset();
                }

                botName = this.failedTrades > 3 ? "Too many failed attempts to trade or it" : "It took you too long to trade bot.";
                WebUtils.downloadString("http://localhost:8080/punishment/ban/create?userId=" + currentSession.getUserId() + "&type=" + BanType.INGAME_TRADE.name() + "&giver=@GIVER@&minutes=15@reason=" + botName);
            }
        }
    }

    private void reset() {
        this.expiryTimer.reset();
        this.failedTrades = 0;
        currentSession = null;
    }

    private void doTrade() {
        if (!currentSession.getPlayerIngameName().equalsIgnoreCase(Trade.getOponentName())) {
            Trade.decline();
        } else {
            if (currentSession.getType() == DepositSessionType.DEPOSIT) {
                this.handleDeposit();
            } else if (currentSession.getType() == DepositSessionType.WITHDRAW) {
                this.handleWithdraw();
            }

        }
    }

    public void handleWithdraw() {
        if (!Trade.isOpen() || currentSession.getState() == DepositSessionState.IN_PROGRESS_TRADING_WITHDRAW || this.updateState(DepositSessionState.IN_PROGRESS_TRADING_WITHDRAW)) {
            if (Trade.getOpenedScreen() == 1) {
                if (Trade.add(995, currentSession.getAmount())) {
                    Trade.accept();
                    Time.sleep(1000, 2000);
                }
            } else if (Trade.getOpenedScreen() == 2 && Trade.hasOtherAccepted()) {
                Trade.accept();
                if (Time.waitInventoryChange()) {
                    this.onTradeAccepted();
                }
            }

        }
    }

    public void handleDeposit() {
        if (!Trade.isOpen() || currentSession.getState() == DepositSessionState.IN_PROGRESS_TRADING_DEPOSIT || this.updateState(DepositSessionState.IN_PROGRESS_TRADING_DEPOSIT)) {
            if (Trade.getOpenedScreen() == 1) {
                if (Trade.hasOtherAccepted() && Trade.getOponentItems().getCount(new int[]{995}) == currentSession.getAmount()) {
                    Trade.accept();
                    Time.sleep(1000, 2000);
                }
            } else if (Trade.getOpenedScreen() == 2) {
                if (Trade.getSecondScreenOponentAmount("Coins") == currentSession.getAmount()) {
                    Trade.accept();
                } else {
                    Trade.decline();
                    ++this.failedTrades;
                }
            }

        }
    }

    public void onTradeAccepted() {
        if (currentSession != null) {
            currentSession.setState(DepositSessionState.COMPLETED);
        } else {
            this.reset();
        }

        if (this.updateState(DepositSessionState.COMPLETED)) {
            this.reset();
        }

    }

    private boolean updateState(DepositSessionState state) {
        if (currentSession != null && WebUtils.downloadString("http://localhost:8080/ingame/bot/deposit/action/update?actionId=" + currentSession.getHash() + "&state=" + state.name()).contains("")) {
            currentSession.setState(state);
            return true;
        } else {
            return false;
        }
    }

    public boolean isBackground() {
        return false;
    }

    public void onMessage(String message) {
        if (message.toLowerCase().contains("declined trade")) {
            ++this.failedTrades;
        }

        if (message.toLowerCase().contains("accepted trade")) {
            this.onTradeAccepted();
        }

        if (currentSession != null && message.toLowerCase().contains("wishes to trade") && !Trade.isOpen()) {
            String name = StringUtils.formatColorsString(message.toLowerCase()).split(" wishes to trade")[0];
            if (name.equalsIgnoreCase(currentSession.getPlayerIngameName())) {
                this.expiryTimer.reset();
                this.lastTradeRequestTimer.reset();
            }
        }

    }
}
