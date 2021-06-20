package com.farm.ibot.api.methods;

import com.farm.ibot.api.accessors.Config;

public enum Varbit {
    MEMBERSHIP_DAYS(1780),
    WITHDRAW_X_AMOUNT(304),
    HAS_HOUSE(2187, 738, 0, 4),
    HOUSE_BUILD_MODE(2179, 780, 0, 0),
    PATCH_STATE(-1, 529, 24, 31);

    private static final int[] BIT_MASKS = new int[32];

    static {
        int var = 2;

        for (int i = 0; i < 32; ++i) {
            BIT_MASKS[i] = var - 1;
            var += var;
        }

    }

    int varbitId = -1;
    int configId = -1;
    int min = -1;
    int max = -1;

    private Varbit(int configId) {
        this.configId = configId;
    }

    private Varbit(int varbitId, int configId, int min, int max) {
        this.varbitId = varbitId;
        this.configId = configId;
        this.min = min;
        this.max = max;
    }

    public static int getMask(int index) {
        return BIT_MASKS[index];
    }

    public int intValue() {
        return this.min == -1 ? Config.get(this.configId) : Config.get(this.configId) >> this.min & getMask(this.max - this.min);
    }

    public boolean booleanValue() {
        return this.intValue() >= 1;
    }
}
