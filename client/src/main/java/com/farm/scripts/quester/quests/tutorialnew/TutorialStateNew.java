package com.farm.scripts.quester.quests.tutorialnew;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.interfaces.Condition;
import com.farm.ibot.api.util.Time;

public enum TutorialStateNew {
    NONE(-1, new int[]{-1}),
    CHOOSE_NAME(2686, new int[]{1}),
    TALK_GUIDE(2686, new int[]{2}),
    CLICK_SETTINGS(2686, new int[]{3}),
    TALK_GUIDE_2(2686, new int[]{7}),
    LEAVE_STARTING_ROOM(2686, new int[]{10}),
    TALK_SURVIVAL_EXPERT(2686, new int[]{20}),
    OPEN_INVENTORY(2686, new int[]{30}),
    FISH_SHRIPMS(2686, new int[]{40}),
    OPEN_SKILLS_TAB(2686, new int[]{50}),
    TALK_SURVIVAL_EXPERT_2(2686, new int[]{60}),
    CUT_DOWN_TREE(2686, new int[]{70}),
    MAKE_FIRE(2686, new int[]{80}),
    COOK_SHRIPMS(2686, new int[]{90, 100}),
    BURNT_SHRIMP(2686, new int[]{110}),
    WALK_TO_GATE(2686, new int[]{120}),
    OPEN_COOK_DOORS(2686, new int[]{130}),
    TALK_TO_COOK(2686, new int[]{140}),
    CLICK_MINIMAP(2686, new int[]{145}),
    COMBINE_FLOUR_WITH_WATER(2686, new int[]{150}),
    USE_BREAD_WITH_RANGE(2686, new int[]{160}),
    OPEN_MUSIC_TAB(2686, new int[]{170}),
    LEAVE_COOKS_BUILDING(2686, new int[]{170}),
    OPEN_EMOTE_TAB(2686, new int[]{183}),
    PERFORM_EMOTE(2686, new int[]{187}),
    OPEN_SETTINGS(2686, new int[]{190}),
    ENABLE_RUN(2686, new int[]{200}),
    OPEN_QUEST_DOOR(2686, new int[]{210}),
    TALK_TO_QUEST_GUIDE(2686, new int[]{220}),
    OPEN_QUEST_TAB(2686, new int[]{230}),
    TALK_TO_QUEST_GUIDE_2(2686, new int[]{240}),
    LEAVE_QUEST_ROOM(2686, new int[]{250}),
    TALK_TO_MINING_INSTRUCTOR(2686, new int[]{260}),
    PROSPECT_TIN_ORE(2686, new int[]{270}),
    PROSPECT_COPPER_ORE(2686, new int[]{280}),
    TALK_TO_MINING_INSTRUCTOR_2(2686, new int[]{290}),
    MINE_TIN_ORE(2686, new int[]{300}),
    MINE_COPPER_ORE(2686, new int[]{310}),
    MAKE_BRONZE_BAR(2686, new int[]{320}),
    TALK_TO_MINING_INSTRUCTOR_3(2686, new int[]{330}),
    USE_BAR_ON_ANVIL(2686, new int[]{340}),
    MAKE_BRONZE_DAGGER(2686, new int[]{349}),
    OPEN_COMBAT_INSTRUCTOR_GATE(2686, new int[]{350}),
    TALK_TO_COMBAT_INSTRUCTOR(2686, new int[]{360}),
    OPEN_EQUIP_TAB(2686, new int[]{390}),
    VIEW_WORN_EQUIPMENT(2686, new int[]{400}),
    WIELD_DAGGER(2686, new int[]{405}),
    CLOSE_INTERFACE(2686, new int[]{410}),
    UNEQUIP_DAGGER_WIELD_SWORD(2686, new int[]{420}),
    OPEN_COMBAT_OPTIONS_TAB(2686, new int[]{430}),
    OPEN_GATE_RATS(2686, new int[]{440}),
    ATTACK_RAT_MELEE(2686, new int[]{450}),
    ATTACK_RAT_MELEE_IN_PROGRESS(2686, new int[]{459}),
    TALK_TO_COMBAT_INSTRUCTOR_2(2686, new int[]{460}),
    TAKE_LOOT(2686, new int[]{465}),
    TALK_TO_COMBAT_INSTRUCTOR_3(2686, new int[]{470}),
    ATTACK_RAT_RANGED(2686, new int[]{480}),
    ATTACK_RAT_RANGED_IN_PROGRESS(2686, new int[]{490}),
    LEAVE_UNDERGROUND(2686, new int[]{500}),
    TALK_TO_BROTHER_BRACE(2686, new int[]{510}),
    OPEN_PRAYER_TAB(2686, new int[]{520}),
    TALK_TO_BROTHER_BRACE_1_5(2686, new int[]{525}),
    USE_ALTAR(2686, new int[]{530}),
    TALK_TO_BROTHER_BRACE_2(2686, new int[]{535}),
    BURY_BONES(2686, new int[]{540}),
    TALK_TO_BROTHER_BRACE_3(2686, new int[]{545}),
    OPEN_FRIENDS_TAB(2686, new int[]{550}),
    TALK_TO_BROTHER_BRACE_4(2686, new int[]{555}),
    LEAVE_CHURCH(2686, new int[]{560}),
    INTERACT_BANK(2686, new int[]{565}),
    INTERACT_POOL_BOOTH(2686, new int[]{570}),
    LEAVE_BANK_ROOM(2686, new int[]{580}),
    TALK_TO_MAGIC_INTRUCTOR(2686, new int[]{620}),
    OPEN_MAGIC_TAB(2686, new int[]{630}),
    TALK_TO_MAGIC_INTRUCTOR_2(2686, new int[]{640}),
    GET_SOME_KFC_WINGS(2686, new int[]{650}),
    TALK_TO_MAGIC_INTRUCTOR_3(2686, new int[]{670}),
    USE_HOME_TELEPORT(2686, new int[]{680}),
    TUTORIAL_DONE_2(2686, new int[]{1000}),
    TUTORIAL_DONE(21, new int[]{67108864});

    public int configId;
    public int[] value;
    public Condition condition = null;

    private TutorialStateNew(int configId, int... value) {
        this.configId = configId;
        this.value = value;
    }

    private TutorialStateNew(Condition condition) {
        this.condition = condition;
    }

    public static boolean isStatePassed(TutorialStateNew state) {
        return getStateIndex(getState()) >= getStateIndex(state);
    }

    public static boolean isInState(TutorialStateNew... states) {
        TutorialStateNew[] var1 = states;
        int var2 = states.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            TutorialStateNew state = var1[var3];
            if (getState() == state) {
                return true;
            }
        }

        return false;
    }

    private static int getStateIndex(TutorialStateNew states) {
        int i = 0;
        TutorialStateNew[] var2 = values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            TutorialStateNew state = var2[var4];
            ++i;
            if (getState() == state) {
                break;
            }
        }

        return i;
    }

    public static TutorialStateNew getState() {
        TutorialStateNew current = NONE;
        TutorialStateNew[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            TutorialStateNew state = var1[var3];
            if (state.condition == null) {
                int[] var5 = state.value;
                int var6 = var5.length;

                for (int var7 = 0; var7 < var6; ++var7) {
                    int value = var5[var7];
                    if (Config.get(state.configId) >= value) {
                        current = state;
                    }
                }
            }
        }

        return current;
    }

    public static void waitStateChange() {
        TutorialStateNew currentState = getState();
        Time.sleep(() -> {
            return !currentState.equals(getState());
        });
    }
}
