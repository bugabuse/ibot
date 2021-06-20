package com.farm.scripts.fletcher;

import com.farm.ibot.api.data.Skill;
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
    public static final int KNIFE = 946;
    public static final int WILLOW_LOGS = 1519;
    public static final int OAK_LOGS = 1521;
    public static final int MAPLE_LOGS = 1517;
    public static final int YEW_LOGS = 1515;
    public static final int MAPLE_LONGBOW = 851;
    public static final RequiredItemContainer ITEMS_FOR_ARROW_SCHAFTS = new RequiredItemContainer("Arrow schafts", new RequiredItem[]{new RequiredItem(1511, 27, 120, 1), new RequiredItem(946, 1, 1, 1)});
    public static final RequiredItemContainer ITEMS_FOR_LONGBOWS_UNF = new RequiredItemContainer("Longbows(u)", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1511, 27, 500, 1)});
    public static final RequiredItemContainer ITEMS_FOR_OAK_LONGBOWS = new RequiredItemContainer("Oak longbows", new RequiredItem[]{new RequiredItem(56, 14, 500, 1), new RequiredItem(1777, 14, 500, 1)});
    public static final RequiredItemContainer ITEMS_FOR_OAK_LONGBOWS_UNF = new RequiredItemContainer("Oak Longbows(u)", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1521, 27, 500, 1)});
    public static final RequiredItemContainer ITEMS_FOR_WILLOW_LONGBOWS = new RequiredItemContainer("Willow Longbows", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1519, 27, 500, 1)});
    public static final RequiredItemContainer ITEMS_FOR_MAPLE_LONGBOWS = new RequiredItemContainer("Maple Longbows", new RequiredItem[]{new RequiredItem(62, 14, 5000, 1), new RequiredItem(1777, 14, 5000, 1)});
    public static final RequiredItemContainer ITEMS_FOR_MAPLE_LONGBOWS_UNF = new RequiredItemContainer("Maple Longbows(u)", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1517, 27, 500, 1)});
    public static final RequiredItemContainer ITEMS_FOR_YEW_LONGBOWS_UNF = new RequiredItemContainer("Yew Longbows(u)", new RequiredItem[]{new RequiredItem(946, 1, 1, 1), new RequiredItem(1515, 27, 500, 1)});
    public static final RequiredItemContainer ITEMS_FOR_YEW_LONGBOWS = new RequiredItemContainer("Yew Longbows", new RequiredItem[]{new RequiredItem(66, 14, 5000, 1), new RequiredItem(1777, 14, 5000, 1)});

    public static RequiredItemContainer getRequiredItems() {
        if (Skill.FLETCHING.getRealLevel() >= 70) {
            return ITEMS_FOR_YEW_LONGBOWS;
        } else if (Skill.FLETCHING.getRealLevel() >= 55) {
            return ITEMS_FOR_MAPLE_LONGBOWS;
        } else if (Skill.FLETCHING.getRealLevel() >= 40) {
            return ITEMS_FOR_WILLOW_LONGBOWS;
        } else if (Skill.FLETCHING.getRealLevel() >= 25) {
            return ITEMS_FOR_OAK_LONGBOWS_UNF;
        } else {
            return Skill.FLETCHING.getRealLevel() >= 10 ? ITEMS_FOR_LONGBOWS_UNF : ITEMS_FOR_ARROW_SCHAFTS;
        }
    }
}
