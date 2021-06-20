package com.farm.ibot.api.util.string;

public class DynamicString {
    private String str = "";

    public DynamicString() {
    }

    public DynamicString(String str) {
        this.str = str;
    }

    public int intValue() {
        try {
            return Integer.parseInt(this.toString());
        } catch (Exception var2) {
            var2.printStackTrace();
            return -1;
        }
    }

    public String toString() {
        return this.str;
    }
}
