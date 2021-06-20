package com.farm.scripts.farmer.api;

import com.farm.ibot.api.accessors.Config;
import com.farm.ibot.api.methods.Varbit;
import com.farm.scripts.farmer.strategies.FarmingStateListener;

public enum PatchState {
    GROWING,
    GROWN,
    SEEDABLE,
    NEED_TO_RAKE,
    DISEASED,
    DEAD;

    public static PatchState fromConfig(String name) {
        long patchTime = (Long) FarmingStateListener.config.getOrDefault(name, 0L) + 4800000L;
        return System.currentTimeMillis() > patchTime ? GROWN : GROWING;
    }

    public static PatchState get() {
        int value = Config.get(529) >> 24 & Varbit.getMask(7);
        if (value >= 0 && value <= 2) {
            return NEED_TO_RAKE;
        } else if (value == 3) {
            return SEEDABLE;
        } else if (value >= 4 && value <= 7) {
            return GROWING;
        } else if (value >= 8 && value <= 10) {
            return GROWN;
        } else if (value >= 11 && value <= 14) {
            return GROWING;
        } else if (value >= 15 && value <= 17) {
            return GROWN;
        } else if (value >= 18 && value <= 21) {
            return GROWING;
        } else if (value >= 22 && value <= 24) {
            return GROWN;
        } else if (value >= 25 && value <= 28) {
            return GROWING;
        } else if (value >= 29 && value <= 31) {
            return GROWN;
        } else if (value >= 32 && value <= 35) {
            return GROWING;
        } else if (value >= 36 && value <= 38) {
            return GROWN;
        } else if (value >= 39 && value <= 42) {
            return GROWING;
        } else if (value >= 43 && value <= 45) {
            return GROWN;
        } else if (value >= 46 && value <= 49) {
            return GROWING;
        } else if (value >= 50 && value <= 52) {
            return GROWN;
        } else if (value >= 53 && value <= 56) {
            return GROWING;
        } else if (value >= 57 && value <= 59) {
            return GROWN;
        } else if (value >= 60 && value <= 67) {
            return GROWING;
        } else if (value >= 68 && value <= 71) {
            return GROWING;
        } else if (value >= 72 && value <= 74) {
            return GROWN;
        } else if (value >= 75 && value <= 78) {
            return GROWING;
        } else if (value >= 79 && value <= 81) {
            return GROWN;
        } else if (value >= 82 && value <= 85) {
            return GROWING;
        } else if (value >= 86 && value <= 88) {
            return GROWN;
        } else if (value >= 89 && value <= 92) {
            return GROWING;
        } else if (value >= 93 && value <= 95) {
            return GROWN;
        } else if (value >= 96 && value <= 99) {
            return GROWING;
        } else if (value >= 100 && value <= 102) {
            return GROWN;
        } else if (value >= 103 && value <= 106) {
            return GROWING;
        } else if (value >= 107 && value <= 109) {
            return GROWN;
        } else if (value >= 128 && value <= 130) {
            return DISEASED;
        } else if (value >= 131 && value <= 133) {
            return DISEASED;
        } else if (value >= 134 && value <= 136) {
            return DISEASED;
        } else if (value >= 137 && value <= 139) {
            return DISEASED;
        } else if (value >= 140 && value <= 142) {
            return DISEASED;
        } else if (value >= 143 && value <= 145) {
            return DISEASED;
        } else if (value >= 146 && value <= 148) {
            return DISEASED;
        } else if (value >= 149 && value <= 151) {
            return DISEASED;
        } else if (value >= 152 && value <= 154) {
            return DISEASED;
        } else if (value >= 155 && value <= 157) {
            return DISEASED;
        } else if (value >= 158 && value <= 160) {
            return DISEASED;
        } else if (value >= 161 && value <= 163) {
            return DISEASED;
        } else if (value >= 164 && value <= 166) {
            return DISEASED;
        } else if (value >= 167 && value <= 169) {
            return DISEASED;
        } else if (value >= 170 && value <= 172) {
            return DEAD;
        } else if (value >= 173 && value <= 191) {
            return GROWING;
        } else if (value >= 192 && value <= 195) {
            return GROWING;
        } else if (value >= 196 && value <= 197) {
            return GROWN;
        } else if (value >= 198 && value <= 200) {
            return DISEASED;
        } else if (value >= 201 && value <= 203) {
            return DEAD;
        } else if (value >= 204 && value <= 219) {
            return GROWING;
        } else {
            return value >= 221 && value <= 255 ? GROWING : null;
        }
    }
}
