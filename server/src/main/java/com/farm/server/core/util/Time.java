/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.util;

import com.farm.server.core.util.Condition;

public class Time {
    public static boolean sleep(Condition condition) {
        return Time.sleep(6500, condition);
    }

    public static boolean sleep(int durationMax, Condition condition) {
        long maxTime = System.currentTimeMillis() + (long)durationMax;
        while (System.currentTimeMillis() < maxTime && !condition.active()) {
            Time.sleep(5);
        }
        return condition.active();
    }

    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }
}

