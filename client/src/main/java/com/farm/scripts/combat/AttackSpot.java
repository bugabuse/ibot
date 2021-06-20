package com.farm.scripts.combat;

import com.farm.ibot.api.world.area.Area;

public class AttackSpot {
    public Area area;
    public String[] npcNames;
    public boolean isWalkingToUnreavhableNpcs = false;
    public boolean isMultiCombat = false;

    public AttackSpot area(Area area) {
        this.area = area;
        return this;
    }

    public AttackSpot npcs(String... npcNames) {
        this.npcNames = npcNames;
        return this;
    }

    public AttackSpot walkToUnreachable(boolean walk) {
        this.isWalkingToUnreavhableNpcs = this.isWalkingToUnreavhableNpcs;
        return this;
    }

    public AttackSpot multiCombat(boolean isMultiCombat) {
        this.isMultiCombat = isMultiCombat;
        return this;
    }
}
