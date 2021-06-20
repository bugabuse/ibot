package com.farm.scripts.quester.quests.tutorial;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.methods.Inventory;
import com.farm.ibot.api.util.Time;

public enum TutorialState {
    NONE(-1, new int[]{-1}),
    CHOOSE_NAME(281, new int[]{1}),
    TALK_GUIDE(281, new int[]{2}),
    CLICK_SETTINGS(281, new int[]{3}),
    TALK_GUIDE_2(281, new int[]{7}),
    LEAVE_STARTING_ROOM(281, new int[]{10}),
    TALK_SURVIVAL_EXPERT(281, new int[]{20}),
    OPEN_INVENTORY(281, new int[]{30}),
    CUT_DOWN_TREE(281, new int[]{70}),
    MAKE_FIRE(281, new int[]{80}),
    TALK_SURVIVAL_EXPERT_LOST_TINDERBOX(() -> {
        return Config.get(281) >= 50 && Config.get(281) <= 60 && !Inventory.container().contains("Tinderbox");
    }),
    OPEN_SKILLS_TAB(281, new int[]{50}),
    TALK_SURVIVAL_EXPERT_2(281, new int[]{60}),
    FISH_SHRIPMS(281, new int[]{40}),
    COOK_SHRIPMS(281, new int[]{90, 100}),
    BURNT_SHRIMP(281, new int[]{110}),
    WALK_TO_GATE(281, new int[]{120}),
    OPEN_COOK_DOORS(281, new int[]{130}),
    TALK_TO_COOK(281, new int[]{140}),
    COMBINE_FLOUR_WITH_WATER(281, new int[]{150}),
    USE_BREAD_WITH_RANGE(281, new int[]{160}),
    OPEN_MUSIC_TAB(281, new int[]{170}),
    LEAVE_COOKS_BUILDING(281, new int[]{170}),
    OPEN_EMOTE_TAB(281, new int[]{183}),
    PERFORM_EMOTE(281, new int[]{187}),
    OPEN_SETTINGS(281, new int[]{190}),
    ENABLE_RUN(281, new int[]{200}),
    OPEN_QUEST_DOOR(281, new int[]{210}),
    TALK_TO_QUEST_GUIDE(281, new int[]{220}),
    OPEN_QUEST_TAB(281, new int[]{230}),
    TALK_TO_QUEST_GUIDE_2(281, new int[]{240}),
    LEAVE_QUEST_ROOM(281, new int[]{250}),
    TALK_TO_MINING_INSTRUCTOR(281, new int[]{260}),
    PROSPECT_TIN_ORE(281, new int[]{270}),
    PROSPECT_COPPER_ORE(281, new int[]{280}),
    TALK_TO_MINING_INSTRUCTOR_2(281, new int[]{290}),
    MINE_TIN_ORE(281, new int[]{300}),
    MINE_COPPER_ORE(281, new int[]{310}),
    MAKE_BRONZE_BAR(281, new int[]{320}),
    TALK_TO_MINING_INSTRUCTOR_3(281, new int[]{330}),
    USE_BAR_ON_ANVIL(281, new int[]{340}),
    MAKE_BRONZE_DAGGER(281, new int[]{350}),
    OPEN_COMBAT_INSTRUCTOR_GATE(281, new int[]{360}),
    TALK_TO_COMBAT_INSTRUCTOR(281, new int[]{370}),
    OPEN_EQUIP_TAB(281, new int[]{390}),
    VIEW_WORN_EQUIPMENT(281, new int[]{400}),
    WIELD_DAGGER(281, new int[]{405}),
    CLOSE_INTERFACE(281, new int[]{410}),
    UNEQUIP_DAGGER_WIELD_SWORD(281, new int[]{420}),
    OPEN_COMBAT_OPTIONS_TAB(281, new int[]{430}),
    OPEN_GATE_RATS(281, new int[]{440}),
    ATTACK_RAT_MELEE(281, new int[]{450}),
    ATTACK_RAT_MELEE_IN_PROGRESS(281, new int[]{460}),
    TALK_TO_COMBAT_INSTRUCTOR_2(281, new int[]{470}),
    ATTACK_RAT_RANGED(281, new int[]{480}),
    ATTACK_RAT_RANGED_IN_PROGRESS(281, new int[]{490}),
    LEAVE_UNDERGROUND(281, new int[]{500}),
    INTERACT_BANK(281, new int[]{510}),
    INTERACT_POOL_BOOTH(281, new int[]{520}),
    LEAVE_BANK_ROOM(281, new int[]{525}),
    TALK_FINANCIAL_ADVISOR(281, new int[]{530}),
    OPEN_ACCOUNT_MANAGEMENT(281, new int[]{531}),
    TALK_FINANCIAL_ADVISOR_2(281, new int[]{532}),
    LEAVE_FINANCIAL_ROOM(281, new int[]{540}),
    TALK_TO_BROTHER_BRACE(281, new int[]{550}),
    OPEN_PRAYER_TAB(281, new int[]{560}),
    TALK_TO_BROTHER_BRACE_2(281, new int[]{570}),
    OPEN_FRIENDS_TAB(281, new int[]{580}),
    OPEN_IGNORE_TAB(281, new int[]{590}),
    TALK_TO_BROTHER_BRACE_3(281, new int[]{600}),
    LEAVE_CHURCH(281, new int[]{610}),
    TALK_SKIPPY(() -> {
        return false;
    }),
    TALK_TO_MAGIC_INTRUCTOR(281, new int[]{620}),
    OPEN_MAGIC_TAB(281, new int[]{630}),
    TALK_TO_MAGIC_INTRUCTOR_2(281, new int[]{640}),
    GET_SOME_KFC_WINGS(281, new int[]{650}),
    TALK_TO_MAGIC_INTRUCTOR_3(281, new int[]{670}),
    TUTORIAL_DONE_2(281, new int[]{1000}),
    TUTORIAL_DONE(21, new int[]{67108864});

    public int configId;
    public int[] value;
    public Condition condition = null;

    private TutorialState(int configId, int... value) {
        this.configId = configId;
        this.value = value;
    }

    private TutorialState(Condition condition) {
        this.condition = condition;
    }

    public static boolean isStatePassed(TutorialState state) {
        return getStateIndex(getState()) >= getStateIndex(state);
    }

    public static boolean isInState(TutorialState... states) {
        TutorialState[] var1 = states;
        int var2 = states.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            TutorialState state = var1[var3];
            if (getState() == state) {
                return true;
            }
        }

        return false;
    }

    private static int getStateIndex(TutorialState states) {
        int i = 0;
        TutorialState[] var2 = values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            TutorialState state = var2[var4];
            ++i;
            if (getState() == state) {
                break;
            }
        }

        return i;
    }

    public static TutorialState getState() {
        TutorialState current = NONE;
        TutorialState[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            TutorialState state = var1[var3];
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
        TutorialState currentState = getState();
        Time.sleep(() -> {
            return !currentState.equals(getState());
        });
    }
}
