package com.farm.scripts.fisher;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.scripts.fisher.util.RequiredItem;

public class FishSettings {
    public static final Tile LOCATION_LUMBRIDGE = new Tile(3245, 3155, 0);
    public static final Tile LOCATION_PORT_SARIM = new Tile(3087, 3228, 0);
    public static final Tile LOCATION_BARBARIAN_VILLAGE = new Tile(3108, 3434, 0);
    public static final Tile LOCATION_KARAMJA = new Tile(2924, 3178, 0);
    public static final String FISHING_METHOD_NET = "Net";
    public static final String FISHING_METHOD_CAGE = "Cage";
    public static final String FISHING_METHOD_LURE = "Lure";
    public static final String FISHING_METHOD_BAIT = "Bait";
    public static final String FISHING_METHOD_HARPOON = "Harpoon";
    public static final FishingConfig FISHING_SHRIMPS;
    public static final FishingConfig FISHING_SARDINES;
    public static final FishingConfig FISHING_SALMON;
    public static final FishingConfig FISHING_SALMON_POWER;
    public static final FishingConfig FISHING_LOBSTERS;
    public static final FishingConfig FISHING_SWORDFISH;
    public static final FishingConfig[] ALL_CONFIGS;
    public static boolean powerFishing = false;

    static {
        FISHING_SHRIMPS = new FishingConfig("Net", new RequiredItem[]{new RequiredItem("Small fishing net", 1, 1, 1)}, LOCATION_LUMBRIDGE, 15, false);
        FISHING_SARDINES = new FishingConfig("Bait", new RequiredItem[]{new RequiredItem("Fishing rod", 1, 1, 1), new RequiredItem("Fishing bait", 250, 250, 1)}, LOCATION_LUMBRIDGE, 15, false);
        FISHING_SALMON = new FishingConfig("Lure", new RequiredItem[]{new RequiredItem("Fly fishing rod", 1, 1, 1), new RequiredItem("Feather", 800, 800, 1)}, LOCATION_BARBARIAN_VILLAGE, 15, true);
        FISHING_SALMON_POWER = new FishingConfig("Lure", new RequiredItem[]{new RequiredItem("Fly fishing rod", 1, 1, 1), new RequiredItem("Feather", 800, 800, 1)}, LOCATION_BARBARIAN_VILLAGE, 15, false);
        FISHING_LOBSTERS = new FishingConfig("Cage", new RequiredItem[]{new RequiredItem("Lobster pot", 1, 1, 1), new RequiredItem(995, 5000, 0, 1000)}, LOCATION_KARAMJA, 15, true, true);
        FISHING_SWORDFISH = new FishingConfig("Harpoon", new RequiredItem[]{new RequiredItem("Harpoon", 1, 1, 1), new RequiredItem(995, 5000, 0, 1000)}, LOCATION_KARAMJA, 15, true, true);
        ALL_CONFIGS = new FishingConfig[]{FISHING_SHRIMPS, FISHING_SARDINES, FISHING_SALMON, FISHING_SALMON_POWER, FISHING_LOBSTERS, FISHING_SWORDFISH};
    }

    public static FishingConfig getConfig() {
        FishingConfig config = FISHING_SHRIMPS;
        if (powerFishing) {
            if (Skill.FISHING.getRealLevel() >= 5) {
                config = FISHING_SARDINES;
            }

            if (Skill.FISHING.getRealLevel() >= 20) {
                config = FISHING_SALMON_POWER;
            }
        } else {
            if (Skill.FISHING.getRealLevel() >= 5) {
                config = FISHING_SARDINES;
            }

            if (Skill.FISHING.getRealLevel() >= 20) {
                config = FISHING_SALMON_POWER;
            }

            if (Skill.FISHING.getRealLevel() >= 40) {
                Strategies.PRICES.setPrice(314, 3);
                config = FISHING_LOBSTERS;
            }

            if (Skill.FISHING.getRealLevel() >= 55) {
                config = FISHING_SWORDFISH;
            }
        }

        return config;
    }
}
