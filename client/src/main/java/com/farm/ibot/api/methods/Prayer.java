package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;

import java.util.ArrayList;
import java.util.List;

public enum Prayer {
    THICK_SKIN(5, 0),
    BURST_OF_STRENGTH(6, 1),
    CLARITY_OF_THOUGHT(7, 2),
    ROCK_SKIN(8, 3),
    SUPERHUMAN_STRENGTH(9, 4),
    SHARP_EYE(23, 18),
    MYSTIC_WILL(24, 19),
    IMPROVED_REFLEXES(10, 5),
    RAPID_RESTORE(11, 6),
    RAPID_HEAL(12, 7),
    PROTECT_ITEM(13, 8),
    HAWK_EYE(25, 20),
    MYSTIC_LORE(26, 21),
    STEEL_SKIN(14, 9),
    ULTIMATE_STRENGTH(15, 10),
    INCREDIBLE_REFLEXES(16, 11),
    PROTECT_MELEE(19, 14),
    PROTECT_MAGIC(17, 12),
    PROTECT_RANGED(18, 13),
    EAGLE_EYE(27, 22),
    MYSTIC_MIGHT(28, 23),
    RETRIBUTION(20, 15),
    REDEMPTION(21, 16),
    SMITE(22, 17);

    public int widgetId;
    public int configIndex;

    private Prayer(int widgetId, int configIndex) {
        this.widgetId = widgetId;
        this.configIndex = configIndex;
    }

    public static List<Prayer> getEnabled() {
        ArrayList<Prayer> prayers = new ArrayList();
        Prayer[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Prayer p = var1[var3];
            if (p.isEnabled()) {
                prayers.add(p);
            }
        }

        return prayers;
    }

    public String toString() {
        return this.name().toLowerCase().replaceAll("_", " ");
    }

    public boolean setEnabled(boolean enabled) {
        if (enabled == this.isEnabled()) {
            return true;
        } else if (!GameTab.PRAYER.open()) {
            return false;
        } else {
            Widget widget = Widgets.get(541, this.widgetId);
            return widget != null && widget.interact("") && Time.sleep(1600, () -> {
                return enabled == this.isEnabled();
            });
        }
    }

    public boolean isEnabled() {
        return (Config.get(83) >> this.configIndex & 1) == 1;
    }
}
