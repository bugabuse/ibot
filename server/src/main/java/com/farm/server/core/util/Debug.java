/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.util;

import java.util.logging.Logger;

public class Debug {
	private static Logger logger = Logger.getLogger(Debug.class.getName());
    public static void log(Object message) {
        logger.info((String)message);
    }
}

