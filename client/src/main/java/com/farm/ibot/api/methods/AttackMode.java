package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.GameTab;

public enum AttackMode {
    ACCURATE(new int[]{0}),
    AGGRESIVE(new int[]{1, 2}),
    DEFENSIVE(new int[]{3});

    int[] configs;

    private AttackMode(int... config) {
        this.configs = config;
    }

    public static AttackMode get() {
        AttackMode[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            AttackMode mode = var0[var2];
            int[] var4 = mode.configs;
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                int config = var4[var6];
                if (Config.get(43) == config) {
                    return mode;
                }
            }
        }

        return ACCURATE;
    }

    public void enable() {
        Widget toClick = null;
        switch (this) {
            case ACCURATE:
                toClick = Widgets.forId(38862852);
                break;
            case AGGRESIVE:
                toClick = Widgets.forId(38862856);
                break;
            case DEFENSIVE:
                toClick = Widgets.forId(38862864);
                if (toClick == null) {
                    toClick = Widgets.forId(38862860);
                }
        }

        if (!get().equals(this) && GameTab.COMBAT.open() && toClick != null) {
            toClick.interact("");
            Time.sleep(() -> {
                return get().equals(this);
            });
        }

    }
}
