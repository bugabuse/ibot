/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.util;

public class CurrencyUtils {
    public static String centsToString(int cents) {
        return "$" + (double)cents / 100.0;
    }
}

