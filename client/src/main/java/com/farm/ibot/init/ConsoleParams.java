package com.farm.ibot.init;

import com.farm.ibot.api.util.StringUtils;

public class ConsoleParams {
    public static String[] args;

    public static boolean contains(String key) {
        if (args == null) {
            return false;
        } else {
            key = key.toLowerCase();
            return StringUtils.containsEqualIgnoreCase("-" + key, args);
        }
    }

    public static String getValue(String key) {
        if (args == null) {
            return "";
        } else {
            key = "" + key.toLowerCase();

            for (int i = 0; i < args.length; ++i) {
                if (args.length > i + 1 && args[i].toLowerCase().startsWith("-" + key)) {
                    return args[i + 1];
                }
            }

            return null;
        }
    }

    public static int getIntValue(String key) {
        if (args == null) {
            return -1;
        } else {
            key = "" + key.toLowerCase();

            for (int i = 0; i < args.length; ++i) {
                if (args.length > i + 1 && args[i].toLowerCase().startsWith("-" + key)) {
                    return Integer.parseInt(args[i + 1]);
                }
            }

            return -1;
        }
    }

    public static String getArgsString() {
        if (args == null) {
            return "";
        } else {
            StringBuilder b = new StringBuilder();
            String[] var1 = args;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                String arg = var1[var3];
                b.append(String.valueOf(arg));
                b.append(" ");
            }

            return b.toString();
        }
    }
}
