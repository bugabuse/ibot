package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.Arrays;

public class InventoryDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 1000;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, Arrays.toString(Player.getLocal().getComposite().getAppearance()));
        Item[] var2 = Inventory.getAll();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Item item = var2[var4];
            g.drawString("" + item.getId(), item.getBounds().x, item.getBounds().y);
            g.drawString("" + item.getAmount(), Mouse.getLocation().x, Mouse.getLocation().y);
        }

    }
}
