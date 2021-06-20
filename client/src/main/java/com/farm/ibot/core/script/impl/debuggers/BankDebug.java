package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.Iterator;

public class BankDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Bank open: " + Bank.isOpen());
        Iterator var2 = Bank.getAll().iterator();

        while (var2.hasNext()) {
            Item item = (Item) var2.next();
            g.drawString(item.getSlot() + " " + item.getId(), item.getBounds().x, item.getBounds().y);
            g.drawRect(item.getBounds().x, item.getBounds().y, item.getBounds().width, item.getBounds().height);
        }

    }
}
