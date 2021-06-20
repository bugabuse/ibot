package com.farm.ibot.api.interact.action.data;

public enum PlayerMethod {
    FOLLOW("Follow", 2046),
    TRADE("Trade", 2047);

    public int value;
    public String stringValue;

    private PlayerMethod(int value) {
        this("", value);
    }

    private PlayerMethod(String stringValue, int value) {
        this.value = value;
        this.stringValue = stringValue;
    }

    public static PlayerMethod forName(String name) {
        PlayerMethod[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            PlayerMethod method = var1[var3];
            if (method.stringValue.equalsIgnoreCase(name)) {
                return method;
            }
        }

        return null;
    }
}
