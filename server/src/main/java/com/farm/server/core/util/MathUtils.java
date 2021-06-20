/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.util;

public class MathUtils {
    public static boolean isInRange(long value, long min, long max) {
        return value >= min || value <= max;
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static int clamp(int value, int max) {
        return MathUtils.clamp(value, 0, max);
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}

