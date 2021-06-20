package com.farm.scripts.zulrahkiller.api;

import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.scripts.zulrahkiller.strategy.ListenerStrategy;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Zulrah {
    private static ArrayList<ZulrahRotation> colorHistory = new ArrayList();

    public static boolean isSwitching() {
        return isDying() || isArriving();
    }

    public static boolean isDying() {
        return get() != null && Ints.contains(new int[]{5072}, get().getAnimation());
    }

    public static boolean isArriving() {
        return get() != null && Ints.contains(new int[]{5073}, get().getAnimation());
    }

    public static Zulrah.Color getColor() {
        Npc zul = get();
        int id = zul != null ? zul.getId() : -1;
        return (Zulrah.Color) Arrays.stream(Zulrah.Color.values()).filter((c) -> {
            return c.npcId == id;
        }).findAny().orElse(Zulrah.Color.NONE);
    }

    public static Tile getPosition() {
        return (Tile) Optional.ofNullable(get()).map((z) -> {
            return z.getPosition();
        }).orElse(new Tile(0, 0));
    }

    public static Npc get() {
        return Npcs.get("Zulrah");
    }

    public static Pattern getPattern() {
        Pattern currentPattern = Pattern.NONE;
        int matchesCount = 1;
        Pattern[] var2 = Pattern.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Pattern pattern = var2[var4];
            int currentMatches = 0;

            for (int i = 0; i < colorHistory.size() && i < pattern.strategies.length; ++i) {
                if (((ZulrahRotation) colorHistory.get(i)).equals(pattern.strategies[i])) {
                    ++currentMatches;
                }
            }

            if (currentMatches > matchesCount) {
                matchesCount = currentMatches;
                currentPattern = pattern;
            }
        }

        return currentPattern;
    }

    public static ZulrahRotation getCurrentRotationStrategy() {
        Pattern pattern = getPattern();
        int zulrahIndex = getZulrahIndex();
        if (pattern != null && zulrahIndex > 0 && pattern.strategies.length > zulrahIndex) {
            if (zulrahIndex >= 2 && isDying()) {
                ListenerStrategy.currentInstanceTimer.reset();
                return getNextRotationStrategy();
            } else {
                return pattern.strategies[zulrahIndex];
            }
        } else {
            return new ZulrahRotation(Zulrah.Color.NONE, Positions.RIGHT_CORNER);
        }
    }

    public static ZulrahRotation getNextRotationStrategy() {
        Pattern pattern = getPattern();
        if (pattern != null && pattern.strategies.length > 0) {
            return pattern.strategies.length > getZulrahIndex() + 1 ? pattern.strategies[getZulrahIndex() + 1] : pattern.strategies[0];
        } else {
            return new ZulrahRotation(Zulrah.Color.NONE, Positions.CENTER_BOTTOM);
        }
    }

    public static ArrayList<ZulrahRotation> getColorHistory() {
        return colorHistory;
    }

    public static int getZulrahIndex() {
        return colorHistory.size() - 1;
    }

    public static boolean hasWaveEnd() {
        Pattern pattern = getPattern();
        if (pattern != null && pattern.strategies.length > 0) {
            return getZulrahIndex() + 1 >= pattern.strategies.length;
        } else {
            return false;
        }
    }

    public static enum Color {
        NONE(-1),
        RED(2043),
        GREEN(2042),
        BLUE(2044);

        int npcId;

        private Color(int npcId) {
            this.npcId = npcId;
        }
    }
}
