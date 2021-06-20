package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Menu;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class WidgetsDebug extends BackgroundScript implements PaintHandler {
    HashMap<Integer, Color> colors = new HashMap();

    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Cutscene: " + Dialogue.isInTheCutScene());
        this.drawString(g, "cfg: " + Config.get(2686));
        this.drawString(g, "cfg old: " + Config.get(281));

        Widget top = Widgets.getTopAt(Mouse.getLocation(), (w) -> {
            return w.getActions() != null && w.getActions().length > 0;
        });
        if (top != null) {
            this.drawString(g, "Top widget: " + top.getId() + " " + Arrays.toString(top.getIndexes()));
            g.setColor(Color.white);
            g.drawRect(top.getBounds().x, top.getBounds().y, top.getWidth(), top.getHeight());
            this.drawString(g, "Real top: " + Menu.getYInteractions()[1] + "  " + Arrays.toString(top.getIndexes()));
            this.drawString(g, "Selected: " + top.getSelectedAction());
            this.drawString(g, String.format("Main [%s] x:%s y: %s", top.getIndex(), top.getX(), top.getY()));

            for (Widget parent = top.getParent(); parent != null; parent = parent.getParent()) {
                this.drawString(g, String.format("[%s] x:%s y: %s", parent.getIndex(), parent.getX(), parent.getY()));
                g.drawString(Arrays.toString(parent.getIndexes()), parent.getBounds().x, parent.getBounds().y + 5);
                g.setColor(Color.green);
                g.drawRect(parent.getBounds().x, parent.getBounds().y, parent.getWidth(), parent.getHeight());
            }
        }

    }
}
