package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;
import java.util.Iterator;

public class NpcDebug extends BackgroundScript implements PaintHandler {
    public void onStart() {
    }

    public int onLoop() {
        return 5000;
    }

    public void onPaint(Graphics g) {
        this.drawString(g, "Npcs: " + Npcs.getAll().size());
        Iterator var2 = Npcs.getAll().iterator();

        while (var2.hasNext()) {
            Npc npc = (Npc) var2.next();
            if (npc.getDefinition() != null) {
                g.drawString(npc.getDefinition().getName() + "(" + npc.getId() + ", " + npc.getComposite().getId() + ")  Index: " + npc.getIndex(), npc.getScreenPoint().x, npc.getScreenPoint().y);
            }
        }

    }
}
