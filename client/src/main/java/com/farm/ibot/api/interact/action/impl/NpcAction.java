package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.util.Random;

import java.awt.*;

public class NpcAction extends Action {
    private static final int xClick = 0;
    private static final int yClick = 0;

    public NpcAction(int index, int playerId) {
        super(0, 0, index, playerId, "", "", 0, 0);
    }

    public static Action create(String action, Npc npc) {
        if (npc != null) {
            NpcAction npcAction = new NpcAction(9 + ObjectAction.getIndex(npc.getDefinition().getActions(), action), npc.getIndex());
            if (action.toLowerCase().startsWith("cast")) {
                npcAction = new NpcAction(8, npc.getIndex());
            } else if (action.toLowerCase().startsWith("use")) {
                npcAction = new NpcAction(7, npc.getIndex());
            }

            Point p = Random.human(npc.getPosition().getBounds().getBounds());
            p.translate(Random.next(-30, 30), Random.next(-30, 30));
            npcAction.clickPointX = p.x;
            npcAction.clickPointY = p.y;
            return npcAction;
        } else {
            return new Action("NpcAction");
        }
    }
}
