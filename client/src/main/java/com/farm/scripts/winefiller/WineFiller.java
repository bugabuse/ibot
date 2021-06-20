package com.farm.scripts.winefiller;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.PlayerSpotInfo;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.string.AccountPlaytimeDynamicString;
import com.farm.ibot.api.util.string.DynamicString;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.scriptutils.mule.util.OnDemandMuleDynamicString;
import com.farm.scripts.winefiller.strategies.JugFillStrategy;

import java.awt.*;

public class WineFiller extends MultipleStrategyScript implements PaintHandler, PlayerSpotInfo, InventoryListener, ScriptRuntimeInfo {
    public static DynamicString MULE_NAME = new OnDemandMuleDynamicString();
    public static int NORMAL = 0;
    public static int GRAND_EXCHANGE = 1;
    public static int currentState;
    public static String[] states;

    static {
        currentState = NORMAL;
        states = new String[]{"Fill jugs", "Grand Exchange"};
    }

    private AccountPlaytimeDynamicString playTime = new AccountPlaytimeDynamicString();
    private PaintTimer timer = new PaintTimer();
    private int jugs;

    public WineFiller() {
        super(Strategies.DEFAULT);
    }

    private long getTotalPlayTime() {
        return (long) this.playTime.intValue() / 1000L / 60L / 60L;
    }

    public boolean breaksEnabled() {
        return false;
    }

    public void onLoad() {
        this.getScriptHandler().loginRandom.autoWorldAssignEnabled = true;
    }

    public void onStartWhenLoggedIn() {
        Strategies.init(this);
    }

    public void onStart() {

        this.addEventHandler(new InventoryEventHandler(this));
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 1.71");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Jugs: " + this.jugs + "(" + this.timer.getHourRatio(this.jugs) + ")");
        this.drawString(g, "State: " + states[currentState]);
        this.drawString(g, "Filling: " + JugFillStrategy.isFilling());
    }

    public void onItemAdded(Item item) {
        if (this.timer.getElapsedSeconds() > 60L && this.timer.getHourRatio(this.jugs) > 4000) {
            this.jugs = 0;
        }

        if (this.jugs == 0) {
            this.timer.reset();
        }

        if (item.getId() == Strategies.JUG_OF_WATER) {
            this.jugs += MathUtils.clamp(item.getAmount(), 1, 6);
        }

    }

    public String runtimeInfo() {
        int hourRatio = this.timer.getHourRatio(this.jugs);
        int jugs = this.jugs;
        if (hourRatio > 2600) {
            hourRatio = 0;
            jugs = -1;
        }

        return this.timer.getElapsedString() + "</th><th>Jugs: " + jugs + "(" + hourRatio + ")</th><th>" + Strategies.getSpot().tile.getNote() + "</th><th>PlaytimeHours: " + this.getTotalPlayTime() + "</th><th>" + states[currentState] + "(" + this.getCurrentlyExecuting() + ")</th>";
    }

    public int maxSpotCount() {
        return 1;
    }

    public int loopInterval() {
        return 600;
    }
}
