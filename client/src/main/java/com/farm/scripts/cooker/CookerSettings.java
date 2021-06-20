package com.farm.scripts.cooker;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.data.definition.ItemDefinition;

public class CookerSettings {
    public static int getFishToCook() {
        if (Skill.COOKING.getRealLevel() >= 30) {
            return ItemDefinition.forName("Raw tuna").getUnnotedId();
        } else if (Skill.COOKING.getRealLevel() >= 25) {
            return ItemDefinition.forName("Raw salmon").getUnnotedId();
        } else {
            return Skill.COOKING.getRealLevel() >= 15 ? ItemDefinition.forName("Raw trout").getUnnotedId() : ItemDefinition.forName("Raw sardine").getUnnotedId();
        }
    }

    public static int getLogToBurn() {
        return 1519;
    }

    public static int getFishToCookAmount() {
        return Skill.COOKING.getRealLevel() >= 30 ? 250 : 150;
    }
}
