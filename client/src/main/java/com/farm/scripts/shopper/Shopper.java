package com.farm.scripts.shopper;

import com.farm.ibot.api.data.definition.ItemDefinition;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.interfaces.ScriptRuntimeInfo;
import com.farm.ibot.api.listener.InventoryEventHandler;
import com.farm.ibot.api.listener.InventoryListener;
import com.farm.ibot.api.methods.shop.Shop;
import com.farm.ibot.api.methods.shop.ShopItem;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.scripts.shopper.strategy.main.ShopStrategy;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Shopper extends MultipleStrategyScript implements PaintHandler, ScriptRuntimeInfo, InventoryListener {
    public static final int RUNE_DEATH = 560;
    public static final int RUNE_AIR = 556;
    public static final int RUNE_FIRE = 554;
    public static final int RUNE_WATER = 555;
    public static final int RUNE_EARTH = 557;
    public static final int JUG_EMPTY = 557;
    public static final int JUG_EMPTY_NOTED = 558;
    public static Tile SHOP_TILE_RUNES = new Tile(3253, 3402, 0);
    public static Tile SHOP_TILE_VARROCK_GENERAL = new Tile(3217, 3416, 0);
    public static Tile SHOP_TILE;
    public static String NPC_NAME;
    public static Shopper instance;

    static {
        SHOP_TILE = SHOP_TILE_RUNES;
        NPC_NAME = "Aubury";
    }

    public PaintTimer timer = new PaintTimer();
    public HashMap<Integer, Integer> gains = new HashMap();

    public Shopper() {
        super(Strategies.DEFAULT);
    }

    public void onStart() {
        instance = this;
        Strategies.init(this);
        this.addEventHandler(new InventoryEventHandler(this));
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Version 0.4");
        this.drawString(g, "Runtime: " + this.timer.getElapsedString());
        this.drawString(g, "Current Strategies: " + this.getCurrentlyExecuting());
        Iterator var2 = this.gains.entrySet().iterator();

        int id;
        while (var2.hasNext()) {
            Entry<Integer, Integer> entry = (Entry) var2.next();
            id = (Integer) entry.getKey();
            int amount = (Integer) entry.getValue();
            this.drawString(g, ItemDefinition.forId(id).name + ": " + amount + "(" + this.timer.getHourRatio(amount) + ")");
        }

        g.setColor(Color.green);
        ShopItem[] var7 = ShopStrategy.itemsToBuy;
        int var8 = var7.length;

        for (id = 0; id < var8; ++id) {
            ShopItem shopItem = var7[id];
            Item item = Shop.getContainer().get(shopItem.name);
            if (item != null) {
                g.drawString("" + shopItem.getNextUpdateTime(), item.getBounds().x + 8, item.getBounds().y + 45);
            }
        }

    }

    public int loopInterval() {
        return 3;
    }

    public String runtimeInfo() {
        return this.timer.getElapsedString() + "</th><th>Runes: " + this.gains.getOrDefault(560, 0) + "(" + this.timer.getHourRatio((Integer) this.gains.getOrDefault(560, 0)) + ")</th>";
    }

    public void onItemAdded(Item item) {
        this.gains.put(item.getId(), (Integer) this.gains.getOrDefault(item.getId(), 0) + item.getAmount());
    }
}
