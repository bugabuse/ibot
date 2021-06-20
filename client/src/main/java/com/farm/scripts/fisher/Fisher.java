package com.farm.scripts.fisher;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.Combat;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.SkillTracker;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.scriptutils.mule.MuleUtils;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Fisher extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, InventoryListener {
    public PaintTimer timer = new PaintTimer();
    private int fishCaught;
    private SkillTracker tracker;
    private FishingConfig lastConfig = null;

    public Fisher() {
        super(Strategies.DEFAULT);
    }

    public Fisher(boolean powerFishing) {
        super(Strategies.DEFAULT);
        FishSettings.powerFishing = true;
    }

    public void onStartWhenLoggedIn() {
        this.tracker = new SkillTracker(Skill.FISHING);
        this.timer.reset();
    }

    public void onStart() {
        WebWalking.enableRunningCondition = () -> {
            return Combat.isUnderAttack() || Client.getRunEnergy() >= 30;
        };
        this.addEventHandler(new InventoryEventHandler(this));
        if (this.startArguments.contains("powerfishing")) {
            FishSettings.powerFishing = true;
        }

        Strategies.init(this);

    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.55");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current: " + this.getCurrentlyExecuting());
        this.drawString(g, "Fishes: " + this.fishCaught + "(" + this.timer.getHourRatio(this.fishCaught) + ")");
        this.drawString(g, "Exp: " + this.tracker.getExpGained() + " (" + this.tracker.getHourRatio() / 1000 + "k)");
        Iterator var2 = Bank.getCache().getAll((i) -> {
            return i.getName().startsWith("Raw ");
        }).iterator();

        while (var2.hasNext()) {
            Item item = (Item) var2.next();
            this.drawString(g, item.getName() + " " + item.getAmount());
        }

    }

    public String runtimeInfo() {
        String otherBotsStr = "";
        if (Player.getLocal().getAnimation() != -1) {
            List<Player> list = (List) Players.getAll().stream().filter((p) -> {
                return p.getPosition().distance() < 12;
            }).collect(Collectors.toList());
            int myBots = (int) list.stream().filter((p) -> {
                return MuleUtils.isOnline(p.getName());
            }).count();
            otherBotsStr = "MyBots: " + myBots + "(" + (list.size() - myBots) + ")";
        }

        return "<th>" + this.timer.getElapsedString() + "</th><th>" + otherBotsStr + "</th><th><b>FL: </b>" + Skill.FISHING.getRealLevel() + "</th><th><b>" + (Skill.FISHING.getRealLevel() >= 40 ? "Fishes" + (this.fishCaught > 10 ? "" : "_") : "F") + ":" + this.fishCaught + "(" + this.timer.getHourRatio(this.fishCaught) + ")</b></th><th><b>" + this.getCurrentlyExecuting() + "</b></th>";
    }

    public void onItemAdded(Item item) {
        if (this.lastConfig != FishSettings.getConfig()) {
            this.lastConfig = FishSettings.getConfig();
            this.timer.reset();
            this.fishCaught = 0;
        }

        if (item.getName().toLowerCase().contains("raw") && item.getAmount() == 1) {
            if (this.fishCaught == 0) {
                this.timer.reset();
            }

            ++this.fishCaught;
            if (FishSettings.getConfig().isUsingDepositBox()) {
                Item fish = Bank.getCache().get(item.getId());
                if (fish != null) {
                    fish.setAmount(fish.getAmount() + 1);
                } else {
                    ArrayList<Item> newItems = new ArrayList((Collection) Arrays.stream(Bank.getCache().getItems()).collect(Collectors.toList()));
                    newItems.add(item);
                    Bank.setCache(new ItemContainer(newItems));
                }
            }
        }

    }

    public int loopInterval() {
        return 1100;
    }
}
