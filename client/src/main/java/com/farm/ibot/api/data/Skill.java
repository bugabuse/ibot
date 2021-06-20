package com.farm.ibot.api.data;

import com.farm.ibot.api.accessors.Client;

public enum Skill {
    ATTACK(0),
    DEFENSE(1),
    STRENGTH(2),
    HITPOINTS(3),
    RANGE(4),
    PRAYER(5),
    MAGIC(6),
    COOKING(7),
    WOODCUTTING(8),
    FLETCHING(9),
    FISHING(10),
    FIREMAKING(11),
    CRAFTING(12),
    SMITHING(13),
    MINING(14),
    HERBLORE(15),
    AGILITY(16),
    THIEVING(17),
    SLAYER(18),
    FARMING(19),
    RUNECRAFTING(20),
    HUNTER(21),
    CONSTRUCTION(22);

    private int index;

    private Skill(int index) {
        this.index = index;
    }

    public static int getExperienceForLevel(int level) {
        int a = 0;

        for (int x = 1; x < level; ++x) {
            a = (int) ((double) a + Math.floor((double) x + 300.0D * Math.pow(2.0D, (double) x / 7.0D)));
        }

        return (int) Math.floor((double) a / 4.0D);
    }

    public int getRealLevel() {
        int[] arr = Client.getRealSkillLevelArray();
        return arr != null && arr.length > this.index ? arr[this.index] : 0;
    }

    public int getCurrentLevel() {
        int[] arr = Client.getCurrentLevelArray();
        return arr != null && arr.length > this.index ? arr[this.index] : 0;
    }

    public int getExperience() {
        int[] arr = Client.getExperienceArray();
        return arr != null && arr.length > this.index ? arr[this.index] : 0;
    }

    public int getExperienceToLevel(int level) {
        return getExperienceForLevel(level) - getExperienceForLevel(this.getRealLevel());
    }

    public int getCurrentPercent() {
        return (int) ((double) this.getCurrentLevel() / (double) this.getRealLevel() * 100.0D);
    }
}
