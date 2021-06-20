package com.farm.scripts.birthday;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.util.Time;

public enum BirthdayState {
    NONE(-1, new int[]{-1}),
    START(2665, new int[]{0}),
    TALK_TO_PHILLIPA(2665, new int[]{10}),
    TAKE_COMB_AND_TALK_TO_IFFIE(2665, new int[]{20}),
    TALK_IFFIE_1(2665, new int[]{30}),
    TALK_IFFIE_2(2665, new int[]{35}),
    TALK_TO_JULIET(2665, new int[]{40}),
    PERFORM_EMOTES(() -> {
        return getBit(Config.get(2665), 1) != 1 && Config.get(2665) > 10000;
    }),
    FINISHED(() -> {
        return Config.get(2665) > 10000 && getBit(Config.get(2665), 1) == 1;
    });

    public int configId;
    public int[] value;
    public Condition condition = null;

    private BirthdayState(int configId, int... value) {
        this.configId = configId;
        this.value = value;
    }

    private BirthdayState(Condition condition) {
        this.condition = condition;
    }

    private static int getBit(int i, int index) {
        return i >> index & 1;
    }

    public static boolean isStatePassed(BirthdayState state) {
        return getStateIndex(getState()) >= getStateIndex(state);
    }

    public static boolean isInState(BirthdayState... states) {
        BirthdayState[] var1 = states;
        int var2 = states.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            BirthdayState state = var1[var3];
            if (getState() == state) {
                return true;
            }
        }

        return false;
    }

    private static int getStateIndex(BirthdayState states) {
        int i = 0;
        BirthdayState[] var2 = values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            BirthdayState state = var2[var4];
            ++i;
            if (getState() == state) {
                break;
            }
        }

        return i;
    }

    public static BirthdayState getState() {
        BirthdayState current = NONE;
        BirthdayState[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            BirthdayState state = var1[var3];
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

    private static String binary(int i) {
        return String.format("%32s", Integer.toBinaryString(i)).replace(' ', '0');
    }

    public static void waitStateChange() {
        BirthdayState currentState = getState();
        Time.sleep(() -> {
            return !currentState.equals(getState());
        });
    }
}
