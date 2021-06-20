package com.farm.scripts.hunter;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.core.script.StrategyScript;
import com.farm.ibot.core.script.manifest.ScriptManifest;
import com.farm.scripts.hunter.strategy.falconry.CatchStrategy;
import com.farm.scripts.hunter.strategy.falconry.FalconGetStrategy;
import com.farm.scripts.hunter.strategy.trap.DropStrategy;
import com.farm.scripts.hunter.strategy.trap.TrapStrategy;
import com.farm.scripts.hunter.trap.TrapTile;

import java.awt.*;

@ScriptManifest(
        isP2p = true
)
public class Hunter extends StrategyScript implements PaintHandler, InventoryListener {
    private PaintTimer timer = new PaintTimer();
    private int shitCatched;
    private TrapStrategy trapStrategy = new TrapStrategy();
    private SkillTracker tracker;

    public Hunter() {
        super(new Strategy[0]);
        this.tracker = new SkillTracker(Skill.HUNTER);
    }

    public void onStart() {
        this.addEventHandler(new InventoryEventHandler(this));

    }

    public void onStartWhenLoggedIn() {

        this.tracker = new SkillTracker(Skill.HUNTER);
        if ((new Tile(2379, 3594, 0)).distance() <= 80) {
            this.addStrategy(new Strategy[]{new CatchStrategy(this), new FalconGetStrategy(), new DropStrategy()});
        } else {
            this.addStrategy(new Strategy[]{this.trapStrategy, new DropStrategy()});
            if (Inventory.container().get((i) -> {
                return i.getName().contains("Box trap");
            }) != null) {
                HunterConfig.currentSpot = HunterConfig.CHINCHOMPA_SPOT_1;
            } else if (Skill.HUNTER.getRealLevel() >= 19) {
                HunterConfig.currentSpot = HunterConfig.WAGTAILS_SPOT_1;
            } else {
                HunterConfig.currentSpot = HunterConfig.CRISMON_SWIFT_SPOT_1;
            }
        }

        this.timer.reset();
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.25");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Shit catched: " + this.shitCatched + "(" + this.timer.getHourRatio(this.shitCatched) + ")");
        this.drawString(g, "Experience gained: " + this.tracker.getExpGained() + "(" + this.tracker.getHourRatio() + ")");
        if (HunterConfig.currentSpot != null) {
            int i = 0;
            TrapTile[] var3 = HunterConfig.currentSpot.getTiles();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                TrapTile tile = var3[var5];
                if (!this.trapStrategy.canLayTrap(i)) {
                    break;
                }

                Rectangle rect = tile.getBounds().getBounds();
                g.drawString("" + tile.getState() + " [" + tile.getPriority() + "]", (int) rect.getCenterX(), (int) rect.getCenterY());
                ++i;
            }
        }

    }

    public void onItemAdded(Item item) {
        if (item.getName().toLowerCase().contains("bones") || item.getName().toLowerCase().contains("chin")) {
            ++this.shitCatched;
        }

    }
}
