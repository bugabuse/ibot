package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Menu;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.wrapper.MenuNode;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;

public class MenuDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Menu open: " + Menu.isVisible());
        MenuNode current = Menu.getNodes()[Menu.getCount() - 1];
        MenuNode[] var3;
        int var4;
        int var5;
        MenuNode node;
        if (Menu.isVisible()) {
            var3 = Menu.getNodes();
            var4 = var3.length;

            for (var5 = 0; var5 < var4; ++var5) {
                node = var3[var5];
                if (node.getBounds().contains(Mouse.getLocation())) {
                    current = node;
                    break;
                }
            }
        }

        this.drawString(g, "Opcode: " + current.getOpcodes());
        this.drawString(g, "Var: " + current.getVariable());
        this.drawString(g, "Option: " + current.getOption());
        this.drawString(g, "Action: " + current.getAction());
        this.drawString(g, "ActionNames: " + current.getActionName());
        this.drawString(g, "X: " + current.getXInteraction());
        this.drawString(g, "Y: " + current.getYInteraction());
        var3 = Menu.getNodes();
        var4 = var3.length;

        for (var5 = 0; var5 < var4; ++var5) {
            node = var3[var5];
            g.setColor(Color.white);
            this.drawString(g, "" + node.getId() + " | " + node.getActionName());
            g.drawRect(node.getBounds().x, node.getBounds().y, node.getBounds().width, node.getBounds().height);
        }

    }
}
