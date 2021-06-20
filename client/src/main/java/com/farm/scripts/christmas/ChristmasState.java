package com.farm.scripts.christmas;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.util.Time;

public enum ChristmasState {
    NONE(-1, new int[]{-1}),
    START(2633, new int[]{0}),
    TALK_TO_SANTA(2633, new int[]{256}),
    TALK_TO_BAKER(2633, new int[]{257}),
    TALK_TO_SANTA_2(2633, new int[]{258}),
    SKIP_DIALOGUES(2633, new int[]{259}),
    SKIP_DIALOGUES_2(2633, new int[]{260}),
    SKIP_DIALOGUES_3(2633, new int[]{261}),
    TALK_TO_DOOR(2633, new int[]{262}),
    CREATE_ITEMS(2633, new int[]{33554694}),
    PULL_LEVER(2633, new int[]{34341126}),
    TALK_TO_PAUL(2633, new int[]{33554695}),
    FINISHED(2633, new int[]{33554696});

    public int configId;
    public int[] value;
    public Condition condition = null;

    private ChristmasState(int configId, int... value) {
        this.configId = configId;
        this.value = value;
    }

    private ChristmasState(Condition condition) {
        this.condition = condition;
    }

    public static boolean isStatePassed(ChristmasState state) {
        return getStateIndex(getState()) >= getStateIndex(state);
    }

    public static boolean isInState(ChristmasState... states) {
        ChristmasState[] var1 = states;
        int var2 = states.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ChristmasState state = var1[var3];
            if (getState() == state) {
                return true;
            }
        }

        return false;
    }

    private static int getStateIndex(ChristmasState states) {
        int i = 0;
        ChristmasState[] var2 = values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            ChristmasState state = var2[var4];
            ++i;
            if (getState() == state) {
                break;
            }
        }

        return i;
    }

    public static ChristmasState getState() {
        ChristmasState current = NONE;
        ChristmasState[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ChristmasState state = var1[var3];
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
        ChristmasState currentState = getState();
        Time.sleep(() -> {
            return !currentState.equals(getState());
        });
    }
}
