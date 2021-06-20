package com.farm.scripts.woodcutter;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.util.MathUtils;
import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.api.world.area.Area;
import com.farm.ibot.api.world.area.MultipleArea;
import com.farm.ibot.api.world.area.RadiusArea;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BotScript;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.woodcutter.util.Tree;
import com.farm.scripts.woodcutter.util.TreeArea;

public class ChopSettings {
    public static WebConfigDynamicString onlyOaks = new WebConfigDynamicString("only_oaks", 3600000L);
    public static PaintTimer treeToChopCacheTimer = new PaintTimer(0L);
    public static PaintTimer treeSpotTimer = new PaintTimer(0L);
    public static Tree treeToChop = null;
    public static TreeArea treeSpot = null;
    public static Tree TREE_YEW = (new Tree("Yew", new int[]{9714})).ids(10823);
    public static Tree TREE_OAK = new Tree("Oak", new int[]{1356});
    public static Tree TREE_WILLOW = new Tree("Willow", new int[]{-1});
    public static Tree TREE_NORMAL = new Tree("Tree", new int[]{1342});
    public static TreeArea WILLOW_LUIMBRIDGE_1 = (new TreeArea(new RadiusArea(new Tile(3178, 3273, 0), 3))).setNote("Willow lumbridge 1");
    public static TreeArea WILLOW_LUIMBRIDGE_2 = (new TreeArea(new RadiusArea(new Tile(3165, 3272, 0), 3))).setNote("Willow lumbridge 2");
    public static TreeArea WILLOW_LUIMBRIDGE_3 = (new TreeArea(new RadiusArea(new Tile(3164, 3267, 0), 3))).setNote("Willow lumbridge 3");
    public static TreeArea NORMAL_VARROCK_EAST = (new TreeArea(new RadiusArea(new Tile(3279, 3427, 0), 15))).setNote("Trees varrock east");
    public static TreeArea NORMAL_VARROCK_WEST = (new TreeArea(new RadiusArea(new Tile(3164, 3415, 0), 15))).setNote("Trees varrock west");
    public static TreeArea NORMAL_LUMBRIDGE_NORTH = (new TreeArea(new RadiusArea(new Tile(3197, 3244, 0), 10))).setNote("Trees Lumbridge North");
    public static TreeArea NORMAL_LUMBRIDGE_EAST = (new TreeArea(new RadiusArea(new Tile(3193, 3246, 0), 10))).setNote("Trees Lumbridge East");
    public static TreeArea NORMAL_GE = (new TreeArea(new RadiusArea(new Tile(3129, 3436, 0), 25))).setNote("Trees GE");
    public static TreeArea OAK_VARROCK_EAST = (new TreeArea(new RadiusArea(new Tile(3279, 3427, 0), 15))).setNote("Oak varrock east");
    public static TreeArea OAK_VARROCK_NORTH = (new TreeArea(new RadiusArea(new Tile(3193, 3461, 0), 7))).setNote("Oak varrock north");
    public static TreeArea OAK_VARROCK_WEST = (new TreeArea(new RadiusArea(new Tile(3164, 3415, 0), 15))).setNote("Oak varrock west");
    public static TreeArea OAK_FALADOR = (new TreeArea(new RadiusArea(new Tile(3007, 3316, 0), 15))).setNote("Oak Falador");
    public static TreeArea OAK_FALADOR_NORTH = (new TreeArea(new RadiusArea(new Tile(2951, 3401, 0), 15))).setNote("Oak Falador North");
    public static TreeArea OAK_LUMBRIDGE_NORTH = (new TreeArea(new RadiusArea(new Tile(3204, 3244, 0), 15))).treesCount(2).setNote("Oak Lumbridge North");
    public static TreeArea OAK_GE = (new TreeArea(new RadiusArea(new Tile(3129, 3436, 0), 15))).treesCount(2).setNote("Oak GE");
    public static TreeArea YEW_GE = (new TreeArea(new RadiusArea(new Tile(3214, 3502, 0), 25))).setNote("Yew GE").treesCount(3);
    public static TreeArea YEW_EDGE = (new TreeArea(new RadiusArea(new Tile(3087, 3477, 0), 25))).setNote("Yew Edge").treesCount(2);
    public static TreeArea YEW_FALADOR = (new TreeArea(new RadiusArea(new Tile(3007, 3316, 0), 25))).setNote("Yew Falador").treesCount(2);
    public static TreeArea YEW_LUMBRIDGE_EAST = (new TreeArea(new RadiusArea(new Tile(3149, 3228, 0), 30))).setNote("Yew Lumbridge East").treesCount(2);
    public static TreeArea YEW_RIMMINGTON = (new TreeArea(new RadiusArea(new Tile(2938, 3231, 0), 20))).setNote("Yew Rimmington").treesCount(4);
    public static TreeArea YEW_VARROCK_EAST = (new TreeArea((new MultipleArea()).add(new Area[]{new RadiusArea(new Tile(3270, 3482, 0), 25)}).exclude(new Area[]{new RadiusArea(new Tile(3247, 3473, 0), 5)}))).setNote("Yew Varrock East").treesCount(2);
    public static boolean powerChopping;
    private static TreeArea[] LOCATION_NORMAL;
    private static TreeArea[] LOCATION_NORMAL_POWER;
    private static TreeArea[] LOCATION_OAK;
    private static TreeArea[] LOCATION_OAK_POWER;
    private static TreeArea[] LOCATION_YEW;
    private static TreeArea[] LOCATION_WILLOW;

    static {
        LOCATION_NORMAL = new TreeArea[]{NORMAL_VARROCK_EAST, NORMAL_VARROCK_WEST, NORMAL_LUMBRIDGE_NORTH, NORMAL_LUMBRIDGE_EAST, NORMAL_GE};
        LOCATION_NORMAL_POWER = new TreeArea[]{NORMAL_GE};
        LOCATION_OAK = new TreeArea[]{OAK_VARROCK_EAST, OAK_VARROCK_NORTH, OAK_VARROCK_WEST, OAK_GE};
        LOCATION_OAK_POWER = new TreeArea[]{OAK_GE};
        LOCATION_YEW = new TreeArea[]{YEW_VARROCK_EAST, YEW_GE, YEW_LUMBRIDGE_EAST, YEW_EDGE, YEW_FALADOR};
        LOCATION_WILLOW = new TreeArea[]{WILLOW_LUIMBRIDGE_1, WILLOW_LUIMBRIDGE_2, WILLOW_LUIMBRIDGE_3};
    }

    public static TreeArea[] getAvailableSpots() {
        if (Player.getLocal() == null) {
            return LOCATION_OAK;
        } else if (powerChopping) {
            if (Skill.WOODCUTTING.getRealLevel() >= 30) {
                return LOCATION_WILLOW;
            } else {
                return Skill.WOODCUTTING.getRealLevel() >= 15 ? LOCATION_OAK_POWER : LOCATION_NORMAL_POWER;
            }
        } else if (!isCuttingOnlyOaks() && Skill.WOODCUTTING.getRealLevel() >= 60) {
            return LOCATION_YEW;
        } else {
            return Skill.WOODCUTTING.getRealLevel() >= 15 ? LOCATION_OAK : LOCATION_NORMAL;
        }
    }

    public static TreeArea getSpot() {
        if (treeSpot == null || treeSpotTimer.getElapsedSeconds() > 6L) {
            treeSpot = getAvailableSpots()[MathUtils.clamp(AccountData.current().getUniqueScriptId() % getAvailableSpots().length, 0, getAvailableSpots().length)];
        }

        return treeSpot;
    }

    public static Tree getTreeToChop() {
        if (powerChopping) {
            treeToChop = TREE_NORMAL;
            if (Skill.WOODCUTTING.getRealLevel() >= 15) {
                treeToChop = TREE_OAK;
            }

            if (Skill.WOODCUTTING.getRealLevel() >= 30) {
                treeToChop = TREE_WILLOW;
            }
        } else if (treeToChop == null || treeToChopCacheTimer.getElapsedSeconds() > 6L) {
            if (!isCuttingOnlyOaks() && Skill.WOODCUTTING.getRealLevel() >= 60) {
                treeToChop = TREE_YEW;
            } else if (Skill.WOODCUTTING.getRealLevel() >= 15) {
                treeToChop = TREE_OAK;
            } else {
                treeToChop = TREE_NORMAL;
            }

            treeToChopCacheTimer.reset();
        }

        return treeToChop;
    }

    private static boolean isCuttingOnlyOaks() {
        return BotScript.get().getStartArguments().contains("Oak") || onlyOaks.intValue() == 1;
    }

    public static boolean isDroppingLogs() {
        return powerChopping;
    }
}
