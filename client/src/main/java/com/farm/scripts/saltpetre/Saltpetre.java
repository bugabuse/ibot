package com.farm.scripts.saltpetre;

import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.listener.MessageEventHandler;
import com.farm.ibot.api.listener.MessageListener;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.init.AccountData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Saltpetre extends MultipleStrategyScript implements PaintHandler, InventoryListener, ScriptRuntimeInfo, MessageListener {
    public static final int SPADE_ID = 952;
    public PaintTimer timer = new PaintTimer();
    public int itemsGained = 0;

    public Saltpetre() {
        super(Strategies.DEFAULT);
    }

    public void onStart() {
        Strategies.init(this);
        this.addEventHandler(new InventoryEventHandler(this));
        this.addEventHandler(new MessageEventHandler(this));
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.3");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Salpetres digged: " + this.itemsGained + "(" + this.timer.getHourRatio(this.itemsGained) + ")");
        this.drawString(g, "Strategy: " + this.getCurrentlyExecuting());
        ArrayList<GameObject> saltpetres = GameObjects.getAll("Saltpetre");
        this.drawString(g, "Available saltpetres: " + saltpetres.size());
        Iterator var3 = saltpetres.iterator();

        while (var3.hasNext()) {
            GameObject saltpetre = (GameObject) var3.next();
            g.drawString("Saltpetre", saltpetre.getScreenPoint().x, saltpetre.getScreenPoint().y);
            this.drawString(g, "" + saltpetre.getPosition());
        }

    }

    public void onItemAdded(Item item) {
        if (item.getId() == 13421) {
            this.itemsGained += item.getAmount();
        }

    }

    public String runtimeInfo() {
        return "Runtime: " + this.timer.getElapsedString() + " Salpetres digged: " + this.itemsGained + "(" + this.timer.getHourRatio(this.itemsGained) + ")";
    }

    public void onMessage(String message) {
        if (message.contains("days of membership")) {
            AccountData.current().isMembers = true;

            while (!Login.logout()) {
                Time.sleep(1000);
            }
        }

    }
}
