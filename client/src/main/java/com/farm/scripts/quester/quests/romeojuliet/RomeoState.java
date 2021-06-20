package com.farm.scripts.quester.quests.romeojuliet;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.util.Time;

public enum RomeoState {
    NONE(-1, new int[]{-1}),
    TALK_TO_ROMEO(144, new int[]{0}),
    TALK_TO_JULIET(144, new int[]{10}),
    DELIVER_MESSAGE(144, new int[]{20}),
    TALK_TO_FATHER_LAWRENCE(144, new int[]{30}),
    TALK_TO_APOTHECARY(144, new int[]{40}),
    MAKE_CADAVA_POTION_AND_DELIVER_TO_JULIET(144, new int[]{50}),
    TALK_TO_ROMEO_2(144, new int[]{60}),
    QUEST_COMPLETE(144, new int[]{100});

    public int configId;
    public int[] value;
    public Condition condition = null;

    private RomeoState(int configId, int... value) {
        this.configId = configId;
        this.value = value;
    }

    private RomeoState(Condition condition) {
        this.condition = condition;
    }

    public static boolean isStatePassed(RomeoState state) {
        return getStateIndex(getState()) >= getStateIndex(state);
    }

    public static boolean isInState(RomeoState... states) {
        RomeoState[] var1 = states;
        int var2 = states.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            RomeoState state = var1[var3];
            if (getState() == state) {
                return true;
            }
        }

        return false;
    }

    private static int getStateIndex(RomeoState states) {
        int i = 0;
        RomeoState[] var2 = values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            RomeoState state = var2[var4];
            ++i;
            if (getState() == state) {
                break;
            }
        }

        return i;
    }

    public static RomeoState getState() {
        RomeoState current = NONE;
        RomeoState[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            RomeoState state = var1[var3];
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
        RomeoState currentState = getState();
        Time.sleep(() -> {
            return !currentState.equals(getState());
        });
    }
}
