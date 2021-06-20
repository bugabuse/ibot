package com.farm.scripts.miner;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.PlayerSpotInfo;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.Combat;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.PaintUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.scripts.miner.strategies.AntiCombatStrategy;

import java.awt.*;
import java.util.Optional;

public class Miner extends MultipleStrategyScript implements PaintHandler, PlayerSpotInfo, ScriptRuntimeInfo, InventoryListener, MessageListener {
    public PaintTimer timer = new PaintTimer();
    private int oresMined;
    private int deathCount = 0;

    public Miner() {
        super(Strategies.DEFAULT);
    }

    public void onLoad() {
        (new Thread(() -> {


            while (true) {
                if (this.scriptHandler.getCurrentlyExecuting() instanceof com.farm.scripts.combat.Combat) {
                    Debug.log("Required combat: " + MiningSettings.getSpot().requiredCombat);
                    com.farm.scripts.combat.Combat.maxCombat = MiningSettings.getSpot().requiredCombat;
                }

                if (this.scriptHandler.getCurrentlyExecuting() == null) {

                    return;
                }

                Time.sleep(1000);
            }
        })).start();
    }

    public void onStart() {
        Strategies.init(this);
        WebWalking.enableRunningCondition = () -> {
            return Combat.isUnderAttack() || Client.getRunEnergy() >= 30;
        };
        this.addEventHandler(new InventoryEventHandler(this));
        this.addEventHandler(new MessageEventHandler(this));
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Miner 0.68");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Ores mined: " + this.oresMined + "(" + this.timer.getHourRatio(this.oresMined) + ")");
        this.drawString(g, "Strategy: " + ((MultipleStrategyScript) this.scriptHandler.getCurrentlyExecuting()).getCurrentlyExecuting());
        this.drawString(g, "Current spot: " + MiningSettings.getSpot().getNote());
        if (Strategies.muleManager != null) {
            this.drawString(g, "Trading: " + Strategies.muleManager.getMuleName());
        }

        PaintUtils.drawTile(g, new Tile(3179, 3371, 0));
    }

    public String runtimeInfo() {
        String pickaxe = ((String) Optional.ofNullable(Inventory.container().get((i) -> {
            return i.getName().contains("pick");
        })).map(Item::getName).orElse("None")).replace(" pickaxe", "");
        return this.timer.getElapsedString() + "</th><th>Mined: " + this.oresMined + "(" + this.timer.getHourRatio(this.oresMined) + ")</th><th>ML: " + Skill.MINING.getRealLevel() + " (" + pickaxe + ")</th><th>Attacked: " + AntiCombatStrategy.inCombatAmount + "(" + this.deathCount + ")</th><th>" + MiningSettings.getSpot().getNote() + "</th>";
    }

    public void onItemAdded(Item item) {
        if (item.getName().contains("Clay") || item.getName().contains(" ore")) {
            ++this.oresMined;
        }

    }

    public int maxSpotCount() {
        return MiningSettings.miningTiles.length;
    }

    public void onMessage(String message) {
        if (message.toLowerCase().contains("dead")) {
            ++this.deathCount;
        }

    }
}
