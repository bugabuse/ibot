package com.farm.ibot.scriptutils.mule;

import com.farm.ibot.api.data.Locations;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.string.AccountPlaytimeDynamicString;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.container.PriceHandler;
import com.farm.ibot.api.wrapper.item.container.WithdrawContainer;
import com.farm.ibot.api.wrapper.item.container.WithdrawItem;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.ibot.scriptutils.mule.strategies.MuleTransfer;
import com.farm.ibot.scriptutils.mule.strategies.MuleTransferListener;
import com.farm.ibot.scriptutils.mule.strategies.MuleTransferNotifier;
import com.farm.ibot.scriptutils.mule.strategies.WalkToSpotStrategy;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;

public class MuleManager {
    public static final int STATE_NONE = 0;
    public static final int STATE_SUPPLY_WAIT = 1;
    public static final int STATE_TRANSFER = 2;
    public final PriceHandler priceHandler;
    private final OnDemandMuleDynamicString muleName;
    private final OnDemandMuleDynamicString resupplyMuleName;
    public AccountPlaytimeDynamicString playTime;
    public int giveToMuleActionId;
    public int supplyFromMuleActionId;
    public int wealthToKeep;
    public int wealthToGive;
    public Tile receivingMuleTile;
    public Tile resupplyMuleTile;
    public WithdrawContainer toWithdraw;
    public int tradeAction;
    public MultipleStrategyScript script;
    public StrategyContainer muleStrategies;
    public boolean determineBankCache;
    public WithdrawItem[] requiredItems;
    public WithdrawItem[] itemsToKeep;
    public boolean requireMuleHandlerAssigned;
    public MuleTransferNotifier notifierStrategy;
    public int tradeOnWorldResupply;
    public int tradeOnWorldGive;
    public WalkToSpotStrategy walkToSpotStrategy;
    public MuleTransferListener muleTransferListener;
    private int currentState;
    private StrategyContainer geStrategies;
    private Condition enabledCondition;

    public MuleManager(OnDemandMuleDynamicString muleName, int wealthToKeep, int wealthToGive, PriceHandler priceHandler) {
        this(muleName, muleName, wealthToKeep, wealthToGive, priceHandler);
    }

    public MuleManager(OnDemandMuleDynamicString muleName, OnDemandMuleDynamicString resupplyMuleName, int wealthToKeep, int wealthToGive, PriceHandler priceHandler) {
        this.playTime = new AccountPlaytimeDynamicString();
        this.giveToMuleActionId = 1;
        this.supplyFromMuleActionId = 2;
        this.receivingMuleTile = Locations.RESUPPLY_REGULAR_TILE;
        this.resupplyMuleTile = Locations.RESUPPLY_REGULAR_TILE;
        this.toWithdraw = null;
        this.tradeAction = 0;
        this.currentState = 0;
        this.determineBankCache = true;
        this.requireMuleHandlerAssigned = false;
        this.tradeOnWorldResupply = -1;
        this.tradeOnWorldGive = -1;
        this.enabledCondition = () -> {
            return true;
        };
        this.walkToSpotStrategy = (x$0, x$1) -> {
            return WebWalking.walkTo(x$0, x$1);
        };
        this.wealthToKeep = wealthToKeep;
        this.wealthToGive = wealthToGive;
        this.priceHandler = priceHandler;
        this.muleName = muleName;
        this.resupplyMuleName = resupplyMuleName;
    }

    public StrategyContainer setup(MultipleStrategyScript script, StrategyContainer defaultStrategies) {
        this.script = script;
        this.notifierStrategy = new MuleTransferNotifier(this);
        this.muleTransferListener = new MuleTransferListener(this);
        defaultStrategies.add(this.muleTransferListener);
        defaultStrategies.add(this.notifierStrategy);
        if (this.muleStrategies == null) {
            this.muleStrategies = new StrategyContainer("Mule Trading");
            this.muleStrategies.add(new MuleTransfer(this, script));
            this.muleStrategies.add(this.notifierStrategy);
        }

        return this.muleStrategies;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public MuleManager withGeStrategies(StrategyContainer geStrategies) {
        this.geStrategies = geStrategies;
        return this;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public void setState(int state) {
        this.currentState = state;
        if (state == 0) {
            this.script.setCurrentlyExecutitng(this.script.getDefaultStrategies());
        } else {
            this.script.setCurrentlyExecutitng(this.muleStrategies);
        }

    }

    public String getMuleName() {
        return this.getMuleDynamicString().toString();
    }

    public OnDemandMuleDynamicString getMuleDynamicString() {
        return this.tradeAction == this.giveToMuleActionId ? this.muleName : this.resupplyMuleName;
    }

    public StrategyContainer getGeStrategies() {
        return this.geStrategies;
    }

    public void activateResupplyState() {
        this.tradeAction = this.supplyFromMuleActionId;
        this.setState(1);
    }

    public Condition getEnabledCondition() {
        return this.enabledCondition;
    }

    public void setEnabledCondition(Condition enabledCondition) {
        this.enabledCondition = enabledCondition;
    }
}
