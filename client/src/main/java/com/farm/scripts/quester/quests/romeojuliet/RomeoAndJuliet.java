package com.farm.scripts.quester.quests.romeojuliet;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.scripts.quester.quest.Quest;
import com.farm.scripts.quester.quests.romeojuliet.strategies.TalkApothecary;
import com.farm.scripts.quester.quests.romeojuliet.strategies.TalkFatherLawrence;
import com.farm.scripts.quester.quests.romeojuliet.strategies.TalkJuliet;
import com.farm.scripts.quester.quests.romeojuliet.strategies.TalkRomeo;

import java.awt.*;

public class RomeoAndJuliet extends Quest implements PaintHandler {
    public static Tile JULIET_TILE = new Tile(3158, 3425, 1);
    public static Tile ROMEO_TILE = new Tile(3213, 3425, 0);
    public static Tile FATHER_LAWRENCE_TILE = new Tile(3254, 3485, 0);
    public static Tile APOTHECARY_TILE = new Tile(3195, 3404, 0);
    public static Tile CADAVA_TILE = new Tile(3269, 3370, 0);

    public RomeoAndJuliet() {
        super(new TalkRomeo(), new TalkJuliet(), new TalkApothecary(), new TalkFatherLawrence());
    }

    public void onPaint(Graphics g) {
        g.drawString("Romeo & juliet 1.0", 5, 25);
        g.drawString("State: " + RomeoState.getState() + " (" + Config.get(144) + ")", 5, 45);
    }

    public boolean isCompleted() {
        return RomeoState.isInState(RomeoState.QUEST_COMPLETE);
    }

    public String getStateString() {
        return RomeoState.getState().toString();
    }
}
