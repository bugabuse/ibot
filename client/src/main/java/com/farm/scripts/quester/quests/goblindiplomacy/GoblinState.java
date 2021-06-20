package com.farm.scripts.quester.quests.goblindiplomacy;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.util.Time;

public enum GoblinState {
    NONE(-1, new int[]{-1}),
    SHOW_ORANGE_ARMOUR(62, new int[]{3}),
    SHOW_BLUE_ARMOUR(62, new int[]{4}),
    SHOW_BROWN_ARMOUR(62, new int[]{5}),
    QUEST_DONE(62, new int[]{6});

    public int configId;
    public int[] value;
    public Condition condition = null;

    private GoblinState(int configId, int... value) {
        this.configId = configId;
        this.value = value;
    }

    private GoblinState(Condition condition) {
        this.condition = condition;
    }

    public static boolean isStatePassed(GoblinState state) {
        return getStateIndex(getState()) >= getStateIndex(state);
    }

    public static boolean isInState(GoblinState... states) {
        GoblinState[] var1 = states;
        int var2 = states.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            GoblinState state = var1[var3];
            if (getState() == state) {
                return true;
            }
        }

        return false;
    }

    private static int getStateIndex(GoblinState states) {
        int i = 0;
        GoblinState[] var2 = values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            GoblinState state = var2[var4];
            ++i;
            if (getState() == state) {
                break;
            }
        }

        return i;
    }

    public static GoblinState getState() {
        GoblinState current = NONE;
        GoblinState[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            GoblinState state = var1[var3];
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
        GoblinState currentState = getState();
        Time.sleep(() -> {
            return !currentState.equals(getState());
        });
    }
}
