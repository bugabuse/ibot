package com.farm.scripts.quester.quests.cooksassistant;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.util.Time;

public enum CookState {
    NONE(-1, new int[]{-1}),
    NOT_STARTED(29, new int[]{0}),
    WHEAT_READY(695, new int[]{1}),
    PROVIDE_INGREDIENTS(29, new int[]{1}),
    QUEST_DONE(29, new int[]{2});

    public int configId;
    public int[] value;
    public Condition condition = null;

    private CookState(int configId, int... value) {
        this.configId = configId;
        this.value = value;
    }

    private CookState(Condition condition) {
        this.condition = condition;
    }

    public static boolean isStatePassed(CookState state) {
        return getStateIndex(getState()) >= getStateIndex(state);
    }

    public static boolean isInState(CookState... states) {
        CookState[] var1 = states;
        int var2 = states.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            CookState state = var1[var3];
            if (getState() == state) {
                return true;
            }
        }

        return false;
    }

    private static int getStateIndex(CookState states) {
        int i = 0;
        CookState[] var2 = values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            CookState state = var2[var4];
            ++i;
            if (getState() == state) {
                break;
            }
        }

        return i;
    }

    public static CookState getState() {
        CookState current = NONE;
        CookState[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            CookState state = var1[var3];
            if (state.condition != null && state.condition.active()) {
                current = state;
            } else if (state.condition == null) {
                int[] var5 = state.value;
                int var6 = var5.length;

                for (int var7 = 0; var7 < var6; ++var7) {
                    int value = var5[var7];
                    if (Config.get(state.configId) == value) {
                        current = state;
                        break;
                    }
                }
            }
        }

        return current;
    }

    public static void waitStateChange() {
        CookState currentState = getState();
        Time.sleep(() -> {
            return !currentState.equals(getState());
        });
    }
}
