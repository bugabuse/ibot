package com.farm.scripts.firemaker;

import com.farm.ibot.api.data.Skill;

public class FiremakerSettings {
    public static int getLogToBurn() {
        if (Skill.FIREMAKING.getRealLevel() >= 45) {
            return 1517;
        } else {
            return Skill.FIREMAKING.getRealLevel() >= 30 ? 1519 : 1511;
        }
    }
}
