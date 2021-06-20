/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.util;

import java.util.UUID;

public class Uuid {
    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

