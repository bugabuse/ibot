package com.farm.scripts.mulereceiver;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.WebConfig;
import com.farm.ibot.api.util.string.DynamicString;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.MuleManager;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.mulereceiver.strategy.BankEnsureStrategy;
import com.farm.scripts.mulereceiver.strategy.TradeStrategy;

import java.awt.*;
import java.util.Random;

public class MuleReceiver extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, MessageListener {
    public static final String MULE_GLOBAL = WebConfig.getString("mule_name_global_proxy");
    public static final int CLAY = 434;
    public static final int SOFT_CLAY = 1761;
    public static final int RUNE_DEATH = 560;
    public static final WithdrawItem[] REQUIRED_ITEMS_MULE;
    public static final WithdrawItem[] REQUIRED_ITEMS_MULE_FLAXING;
    public static final WithdrawItem[] REQUIRED_ITEMS_MULE_GLOBAL;
    public static final WithdrawItem[] KEEP_MULE_NON_LITE;
    public static final WithdrawItem[] KEEP_MULE;
    public static final WithdrawItem[] KEEP_MULE_GLOBAL;
    public static final WithdrawItem[] KEEP_MULE_GLOBAL_BIG;
    public static final WithdrawItem[] KEEP_MULE_GLOBAL_P2P;
    public static final PriceHandler PRICES;
    public static Tile RECEIVING_TILE;
    public static boolean isMainMule;
    public static StrategyContainer DEFAULT;
    public static int JUG_OF_WINE;
    public static int JUG_EMPTY;
    public static int JUG_OF_WATER;

    static {
        RECEIVING_TILE = Locations.RESUPPLY_REGULAR_TILE;
        DEFAULT = new StrategyContainer("Default");
        JUG_OF_WINE = 1993;
        JUG_EMPTY = 1935;
        JUG_OF_WATER = 1937;
        REQUIRED_ITEMS_MULE = new WithdrawItem[]{new WithdrawItem(995, 30000, 1)};
        REQUIRED_ITEMS_MULE_FLAXING = new WithdrawItem[]{new WithdrawItem(995, 99999, 1)};
        REQUIRED_ITEMS_MULE_GLOBAL = new WithdrawItem[]{new WithdrawItem(995, 500000, 1)};
        KEEP_MULE_NON_LITE = new WithdrawItem[]{new WithdrawItem(995, 1000000, 1)};
        KEEP_MULE = new WithdrawItem[]{new WithdrawItem(995, 100000, 1)};
        KEEP_MULE_GLOBAL = new WithdrawItem[]{new WithdrawItem(995, 2000000, 1)};
        KEEP_MULE_GLOBAL_BIG = new WithdrawItem[]{new WithdrawItem(995, 10000000, 1)};
        KEEP_MULE_GLOBAL_P2P = new WithdrawItem[]{new WithdrawItem(995, 3000000, 1)};
        PRICES = new PriceHandler(new WithdrawItem[]{new WithdrawItem(1989, 0, 15000), new WithdrawItem(1990, 0, 15000), new WithdrawItem(1038, 0, 5000), new WithdrawItem(1039, 0, 5000), new WithdrawItem(1040, 0, 5000), new WithdrawItem(1041, 0, 5000), new WithdrawItem(1042, 0, 5000), new WithdrawItem(1043, 0, 5000), new WithdrawItem(1044, 0, 5000), new WithdrawItem(1045, 0, 5000), new WithdrawItem(1046, 0, 5000), new WithdrawItem(1047, 0, 5000), new WithdrawItem(1048, 0, 5000), new WithdrawItem(1049, 0, 5000), new WithdrawItem(1050, 0, 5000), new WithdrawItem(1051, 0, 5000), new WithdrawItem(962, 0, 5000), new WithdrawItem(963, 0, 5000), new WithdrawItem(995, 0, 1), new WithdrawItem(314, 0, 3), new WithdrawItem(1609, 0, 200), new WithdrawItem(1610, 0, 200), new WithdrawItem(1625, 0, 200), new WithdrawItem(1626, 0, 200), new WithdrawItem(1777, 0, 70), new WithdrawItem(1778, 0, 70), new WithdrawItem(5295, 0, 50000), new WithdrawItem(5300, 0, 50000), new WithdrawItem(5304, 0, 50000), new WithdrawItem(852, 0, 230), new WithdrawItem(63, 0, 60), new WithdrawItem(66, 0, 360), new WithdrawItem(67, 0, 360), new WithdrawItem(855, 0, 560), new WithdrawItem(856, 0, 560), new WithdrawItem(58, 0, 30), new WithdrawItem(59, 0, 30), new WithdrawItem(56, 0, 30), new WithdrawItem(57, 0, 30), new WithdrawItem(8778, 0, 350), new WithdrawItem(8779, 0, 350), new WithdrawItem(377, 0, 180), new WithdrawItem(371, 0, 320), new WithdrawItem(333, 0, 20), new WithdrawItem(331, 0, 60), new WithdrawItem(359, 0, 70), new WithdrawItem(335, 0, 20), new WithdrawItem(378, 0, 180), new WithdrawItem(372, 0, 320), new WithdrawItem(334, 0, 20), new WithdrawItem(332, 0, 60), new WithdrawItem(360, 0, 70), new WithdrawItem(336, 0, 20), new WithdrawItem(1511, 0, 32), new WithdrawItem(1512, 0, 32), new WithdrawItem(JUG_EMPTY + 1, 0, 2), new WithdrawItem(JUG_EMPTY, 0, 2), new WithdrawItem(JUG_OF_WINE + 1, 0, 15), new WithdrawItem(JUG_OF_WINE, 0, 15), new WithdrawItem(JUG_OF_WATER, 0, 20), new WithdrawItem(JUG_OF_WATER + 1, 0, 20), new WithdrawItem(560, 0, 250), new WithdrawItem(561, 0, 250), new WithdrawItem(1521, 0, 32), new WithdrawItem(1522, 0, 32), new WithdrawItem(1515, 0, 300), new WithdrawItem(1516, 0, 300), new WithdrawItem(435, 0, 50), new WithdrawItem(434, 0, 50), new WithdrawItem(440, 0, 120), new WithdrawItem(441, 0, 120), new WithdrawItem(1761, 0, 100), new WithdrawItem(1762, 0, 100), new WithdrawItem(1618, 0, 3000), new WithdrawItem(1624, 0, 1000), new WithdrawItem(1622, 0, 500), new WithdrawItem(1620, 0, 2000), new WithdrawItem(1617, 0, 3000), new WithdrawItem(1623, 0, 1000), new WithdrawItem(1621, 0, 500), new WithdrawItem(1619, 0, 2000), new WithdrawItem(1739, 0, 100), new WithdrawItem(1740, 0, 100), new WithdrawItem(2132, 0, 80), new WithdrawItem(2133, 0, 80), new WithdrawItem(526, 0, 80), new WithdrawItem(527, 0, 80)});
    }

    public PaintTimer timer = new PaintTimer();
    MuleManager manager;
    private int deaths;
    private TradeStrategy tradeStrategy;

    public MuleReceiver() {
        super(DEFAULT);
    }

    public void onLoad() {
        this.getScriptHandler().loginRandom.autoWorldAssignEnabled = false;
    }

    public void onStartWhenLoggedIn() {
        this.addEventHandler(new MessageEventHandler(this));
    }

    public void onStart() {
        WebConfigDynamicString forceTrade = new WebConfigDynamicString("mule_force_trade", 60000L);
        WebConfigDynamicString liteMules = new WebConfigDynamicString("lite_mules", 60000L);
        this.getScriptHandler().loginRandom.autoWorldAssignEnabled = false;
        this.tradeStrategy = new TradeStrategy(this);
        DEFAULT.add(new BankEnsureStrategy());
        DEFAULT.add(this.tradeStrategy);

        if (get().getStartArguments().startsWith("mule_name_varrock")) {

            this.manager = new MuleManager(new OnDemandMuleDynamicString(), 0, 600000, PRICES);
            this.manager.setup(this, DEFAULT);
            this.tradeStrategy.allowedPackages = new int[]{1, 2, 11, 8, 16};
            this.manager.giveToMuleActionId = 4;
            this.manager.determineBankCache = false;
            this.manager.requireMuleHandlerAssigned = true;
            if (liteMules.intValue() == 1) {
                this.manager.requiredItems = REQUIRED_ITEMS_MULE_FLAXING;
                this.manager.itemsToKeep = KEEP_MULE;
                this.manager.supplyFromMuleActionId = 8;
            } else {
                this.manager.requiredItems = REQUIRED_ITEMS_MULE_FLAXING;
                this.manager.itemsToKeep = KEEP_MULE_NON_LITE;
                this.manager.supplyFromMuleActionId = 12;
            }

            this.manager.tradeOnWorldGive = 308;
            this.manager.tradeOnWorldResupply = 308;
        } else if (get().getStartArguments().equals("mule_name_falador")) {

            RECEIVING_TILE = Locations.BANK_FALADOR_EAST;
            this.manager = new MuleManager(new OnDemandMuleDynamicString(), 0, 300000, PRICES);
            this.manager.setup(this, DEFAULT);
            this.tradeStrategy.allowedPackages = new int[]{3};
            this.manager.giveToMuleActionId = 4;
            this.manager.supplyFromMuleActionId = -1;
        } else if (get().getStartArguments().equals("mule_name_rimmington")) {

            RECEIVING_TILE = Locations.BANK_RIMMINGTON;
            this.manager = new MuleManager(new OnDemandMuleDynamicString(), 0, 600000, PRICES);
            this.manager.setup(this, DEFAULT);
            this.tradeStrategy.allowedPackages = new int[]{15};
            this.manager.giveToMuleActionId = 4;
            this.manager.supplyFromMuleActionId = -1;
        } else if (get().getStartArguments().equals("global")) {

            this.manager = new MuleManager(new OnDemandMuleDynamicString(), 0, 3000000, PRICES);
            this.manager.setup(this, DEFAULT);
            this.tradeStrategy.allowedPackages = new int[]{14, 4, 9, 13, 12, 5};
            this.manager.giveToMuleActionId = -1;
            this.manager.supplyFromMuleActionId = 19;
            this.manager.requiredItems = REQUIRED_ITEMS_MULE_GLOBAL;
            this.manager.itemsToKeep = KEEP_MULE_GLOBAL_BIG;
            this.manager.requireMuleHandlerAssigned = true;
            this.tradeStrategy.tradeOnlyWorld = 303;
            if (liteMules.intValue() == 1) {
                this.tradeStrategy.allowedPackages = new int[]{4, 13, 8};
                this.manager.itemsToKeep = KEEP_MULE_GLOBAL;
                this.manager.supplyFromMuleActionId = 10;
            }
        } else if (get().getStartArguments().equals("farming")) {

            if (forceTrade.intValue() == 1) {
                this.manager = new MuleManager(new OnDemandMuleDynamicString(), 0, 100000, PRICES);
                this.manager.setup(this, DEFAULT);
                this.tradeStrategy.allowedPackages = new int[]{14, 8, 5, 1, 2, 11, 16};
                this.manager.tradeOnWorldGive = 303;
                this.manager.tradeOnWorldResupply = 303;
                this.tradeStrategy.tradeOnlyWorld = (new Random()).nextInt(2) == 1 ? 303 : 304;
                this.manager.requiredItems = null;
                this.manager.itemsToKeep = null;
                this.manager.supplyFromMuleActionId = -1;
                this.manager.requireMuleHandlerAssigned = true;
            } else {
                this.manager = new MuleManager(new OnDemandMuleDynamicString(), 0, 2000000, PRICES);
                this.manager.setup(this, DEFAULT);
                this.tradeStrategy.onlyP2p = true;
                this.tradeStrategy.allowedPackages = new int[]{14, 8, 18, 2, 16};
                this.manager.tradeOnWorldGive = 303;
                this.manager.tradeOnWorldResupply = 303;
                this.tradeStrategy.tradeOnlyWorld = (new Random()).nextInt(2) == 1 ? 303 : 304;
                this.manager.supplyFromMuleActionId = 13;
                this.manager.giveToMuleActionId = 9;
                this.manager.requiredItems = REQUIRED_ITEMS_MULE_FLAXING;
                this.manager.itemsToKeep = KEEP_MULE_NON_LITE;
                this.manager.requireMuleHandlerAssigned = true;
            }
        } else if (get().getStartArguments().equals("main")) {

            this.tradeStrategy.allowedPackages = new int[]{9, 14, 12, 8, 10, 17, 19, 18, 5};
        } else if (this.isMuleName(WebConfig.getString("mule_name_saltpetre"))) {

            RECEIVING_TILE = Locations.BANK_ZEAH;
            this.tradeStrategy.allowedPackages = new int[]{6};
        } else {

        }

        if (this.manager != null) {
            this.manager.determineBankCache = false;
            this.manager.muleTransferListener.muleQueueCountStart = new DynamicString("50000");
        }

        this.setCurrentlyExecutitng(DEFAULT);
    }

    private boolean isMuleName(String name) {
        return this.getScriptHandler().getBot().getSession().getAccount().getGameUsername().equalsIgnoreCase(name);
    }

    private String getWealth() {
        if (this.manager != null && this.manager.toWithdraw != null) {
            int wealth = this.manager.toWithdraw.calculateWealth() / 1000;
            return wealth + "k";
        } else {
            return "?";
        }
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 2.995");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "OnlyWorld: " + this.tradeStrategy.tradeOnlyWorld);
        this.drawString(g, "Trades: " + TradeStrategy.tradesReceived + " / " + TradeStrategy.tradesGiven + "(" + (TradeStrategy.tradesGiven + TradeStrategy.tradesReceived) + ")");
        if (this.getCurrentlyExecuting() != null && this.getCurrentlyExecuting().equals(DEFAULT) && TradeStrategy.currentTrade != null) {
            this.drawString(g, "Trading with: " + TradeStrategy.currentTrade + "(" + TradeStrategy.currentTrade.tradeOnWorld + ")");
            this.drawString(g, "Trade ID: " + TradeStrategy.currentTrade.id);
            this.drawString(g, "Timer: " + TradeStrategy.currentTradeTimer.getElapsedString());
        }

        if (this.manager != null && this.manager.toWithdraw != null) {
            this.drawString(g, "");
            this.drawString(g, "");
            this.drawString(g, "Trade action: " + this.manager.tradeAction);
            this.drawString(g, "Mule " + this.manager.getMuleName());
            this.drawString(g, "Wealth: " + this.manager.toWithdraw.calculateWealth());
            this.drawString(g, "");
            WithdrawItem[] var2 = this.manager.toWithdraw.getPriceableItemArray();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Item item = var2[var4];
                this.drawString(g, item.getName() + ": " + item.getAmount());
            }
        }

    }

    public String runtimeInfo() {
        return "<th>" + this.timer.getElapsedString() + "</th>" + (this.getCurrentlyExecuting().toString().contains("Mule") ? "<th><b>Mule Trading(" + this.manager.getMuleName() + ")</b></th>" : "<th><b>Trading: </b>" + TradeStrategy.currentTrade + "</th>") + "<th><b>Wealth:</b> " + this.getWealth() + "</th>" + (this.deaths > 0 ? "<th><b>Deaths:</b> " + this.deaths + "</th>" : "") + "<th><b>Got: </b>" + TradeStrategy.tradesReceived + "</th><th><b>Gave: </b>" + TradeStrategy.tradesGiven + "</th>";
    }

    public void onMessage(String message) {
        if (message.toLowerCase().contains("you are dead")) {
            ++this.deaths;
        }

    }
}
