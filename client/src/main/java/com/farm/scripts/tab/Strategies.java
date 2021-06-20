package com.farm.scripts.tab;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.MultipleStrategyScript;
import com.farm.ibot.core.script.StrategyContainer;
import com.farm.scripts.tab.strategies.EnsureItems;
import com.farm.scripts.tab.strategies.HouseOwnersSearcher;
import com.farm.scripts.tab.strategies.MakeTabStrategy;
import com.farm.scripts.tab.strategies.UnnoteStrategy;

public class Strategies {
    public static final int ID_NOTED_CLAY = 1762;
    public static final int ID_UNNOTED_CLAY = 1761;
    public static final int ID_RUNE_EARTH = 557;
    public static final int ID_RUNE_LAW = 563;
    public static final int ID_RUNE_FIRE = 554;
    public static final int WIDGET_VARROCK = 11;
    public static final int WIDGET_LUMBRIDGE = 12;
    public static final int WIDGET_FALADOR = 13;
    public static final int WIDGET_CAMELOT = 14;
    public static final int WIDGET_ARDOUGNE = 15;
    public static final int WIDGET_WATCHOWER = 16;
    public static final int WIDGET_HOUSE = 17;
    public static final Tile TILE_PHIALS = new Tile(2950, 3213, 0);
    public static StrategyContainer DEFAULT = new StrategyContainer("Default");

    public static void init(MultipleStrategyScript script) {
        DEFAULT.add(new EnsureItems());
        DEFAULT.add(new MakeTabStrategy());
        DEFAULT.add(new UnnoteStrategy());
        DEFAULT.add(new HouseOwnersSearcher(script));
        script.setCurrentlyExecutitng(DEFAULT);
        script.getScriptHandler().webNotFoundRandom.isDisabledCondition = UnnoteStrategy::isAtHome;
        script.getScriptHandler().loginRandom.autoWorldAssignEnabled = false;
        WebWalking.enableRunningCondition = WebWalking.RUNNING_ALWAYS_ON;
    }

    public static int getTabToMake() {
        return Skill.MAGIC.getRealLevel() >= 40 && Inventory.contains(557) ? 17 : 11;
    }
}
