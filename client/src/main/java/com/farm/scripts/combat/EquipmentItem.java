package com.farm.scripts.combat;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.methods.Equipment.Slot;

public class EquipmentItem {
    public String itemName;
    public Slot slot;
    public boolean isRequired = true;
    public int levelRequired;
    public Skill skillRequired;

    public EquipmentItem(String itemName, Slot slot) {
        this.itemName = itemName;
        this.slot = slot;
    }

    public EquipmentItem required(boolean isRequired) {
        this.isRequired = isRequired;
        return this;
    }

    public EquipmentItem level(Skill skill, int levelRequired) {
        this.levelRequired = levelRequired;
        this.skillRequired = skill;
        return this;
    }

    public boolean canWear() {
        return this.skillRequired == null || this.skillRequired.getRealLevel() >= this.levelRequired;
    }

    public String toString() {
        return this.itemName;
    }
}
