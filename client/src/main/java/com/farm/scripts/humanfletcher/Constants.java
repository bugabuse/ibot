package com.farm.scripts.humanfletcher;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.scripts.fisher.util.RequiredItem;
import com.farm.scripts.fletcher.api.RequiredItemContainer;

public class Constants {
    public static final int BOW_STRING = 1777;
    public static final int MAPLE_LONGBOW_UNFINISHED = 62;
    public static final int OAK_LONGBOW_UNFINISHED = 56;
    public static final int WILLOW_LONGBOW_UNFINISHED = 58;
    public static final int YEW_LONGBOW_UNFINISHED = 66;
    public static final int YEW_LONGBOW = 855;
    public static final int LONGBOW_UNFINISHED = 48;
    public static final int LOGS = 1511;
    public static final int LOGS_OAK = 1521;
    public static final int LOGS_WILLOW = 1519;
    public static final int LOGS_MAPLE = 1517;
    public static final int LOGS_YEW = 1515;
    public static final int KNIFE = 946;
    public static final int WILLOW_LOGS = 1519;
    public static final int OAK_LOGS = 1521;
    public static final int MAPLE_LOGS = 1517;
    public static final int YEW_LOGS = 1515;
    public static final int MAPLE_LONGBOW = 851;
    public static final int WILLOW_LONGBOW = 847;
    public static final int OAK_LONGBOW = 845;
    public static final int LONGBOW = 839;
    public static final int ARROW_SHAFTS = 52;
    public static final RequiredItemContainer ITEMS_FOR_ARROW_SCHAFTS = new RequiredItemContainer("Arrow schafts", new RequiredItem[]{new RequiredItem(1511, 27, 500, 1), new RequiredItem(946, 1, 1, 1)});
    public static final RequiredItemContainer ITEMS_FOR_LONGBOWS = new RequiredItemContainer("Longbows", new RequiredItem[]{new RequiredItem(48, 14, 650, 1), new RequiredItem(1777, 14, 5000, 1), new RequiredItem(56, 1, 500, 1)});
    public static final RequiredItemContainer ITEMS_FOR_LONGBOWS_UNF = new RequiredItemContainer("Longbows(u)", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1511, 27, 650, 1), new RequiredItem(1521, 27, 650, 1)});
    public static final RequiredItemContainer ITEMS_FOR_OAK_LONGBOWS = new RequiredItemContainer("Oak longbows", new RequiredItem[]{new RequiredItem(56, 14, 1500, 1), new RequiredItem(1777, 14, 5000, 1)});
    public static final RequiredItemContainer ITEMS_FOR_OAK_LONGBOWS_UNF = new RequiredItemContainer("Oak Longbows(u)", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1521, 27, 3300, 1)});
    public static final RequiredItemContainer ITEMS_FOR_WILLOW_LONGBOWS = new RequiredItemContainer("Willow Longbows", new RequiredItem[]{new RequiredItem(58, 14, 2500, 1), new RequiredItem(1777, 14, 5000, 1)});
    public static final RequiredItemContainer ITEMS_FOR_WILLOW_LONGBOWS_UNF = new RequiredItemContainer("Willow Longbows(u)", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1519, 27, 4300, 1)});
    public static final RequiredItemContainer ITEMS_FOR_MAPLE_LONGBOWS = new RequiredItemContainer("Maple Longbows", new RequiredItem[]{new RequiredItem(62, 14, 3000, 1), new RequiredItem(1777, 14, 5000, 1)});
    public static final RequiredItemContainer ITEMS_FOR_MAPLE_LONGBOWS_UNF = new RequiredItemContainer("Maple Longbows(u)", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1517, 27, 10000, 1)});
    public static final RequiredItemContainer ITEMS_FOR_YEW_LONGBOWS = new RequiredItemContainer("Yew Longbows", new RequiredItem[]{(new RequiredItem(66, 14, 500, 1)).withMaxToSpend(0.65F), (new RequiredItem(1777, 14, 500, 1)).withMaxToSpend(1.0F)});
    public static final RequiredItemContainer ITEMS_FOR_YEW_LONGBOWS_UNF = new RequiredItemContainer("Yew Longbows(u)", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1515, 27, 1000, 1)});
    public static int LOGS_ID = -1;
    public static int UNSTRUG_BOW_ID = -1;
    public static int BOW_ID = -1;
    public static Tile[] STAND_TILES = new Tile[]{new Tile(3162, 3488, 0), new Tile(3162, 3489, 0), new Tile(3167, 3488, 0), new Tile(3167, 3489, 0), new Tile(3167, 3490, 0), new Tile(3167, 3491, 0)};

    public static void loadItems() {
        if (Skill.FLETCHING.getRealLevel() >= 70) {
            UNSTRUG_BOW_ID = 66;
            BOW_ID = 855;
        } else if (Skill.FLETCHING.getRealLevel() >= 55) {
            LOGS_ID = 1517;
        } else if (Skill.FLETCHING.getRealLevel() >= 40) {
            LOGS_ID = 1519;
        } else if (Skill.FLETCHING.getRealLevel() >= 25) {
            LOGS_ID = 1521;
        } else if (Skill.FLETCHING.getRealLevel() >= 10) {
            LOGS_ID = 1511;
        }
    }

    public static RequiredItemContainer getRequiredItems() {
        if (Skill.FLETCHING.getRealLevel() >= 70) {
            return ITEMS_FOR_YEW_LONGBOWS;
        } else if (Skill.FLETCHING.getRealLevel() >= 55) {
            return ITEMS_FOR_MAPLE_LONGBOWS_UNF;
        } else if (Skill.FLETCHING.getRealLevel() >= 40) {
            return ITEMS_FOR_WILLOW_LONGBOWS_UNF;
        } else if (Skill.FLETCHING.getRealLevel() >= 25) {
            return ITEMS_FOR_OAK_LONGBOWS_UNF;
        } else {
            return Skill.FLETCHING.getRealLevel() >= 10 ? ITEMS_FOR_LONGBOWS_UNF : ITEMS_FOR_ARROW_SCHAFTS;
        }
    }
}
