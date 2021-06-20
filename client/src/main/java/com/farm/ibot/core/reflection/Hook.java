package com.farm.ibot.core.reflection;

import java.math.BigInteger;

public class Hook {
    public static final int FIELD = 0;
    public static final int METHOD = 1;
    public static final int CLASS = 2;
    public final String realName;
    public final String obfuscatedName;
    public final Number setterMultipler;
    private final String classLocation;
    private final String fieldName;
    private final int type;
    public int reflectionIndex;
    public Number getterMultipler;

    public Hook(String realName, String obfuscatedName) {
        this(realName, obfuscatedName, -1);
    }

    public Hook(String realName, String obfuscatedName, Number getterMultipler) {
        this(realName, obfuscatedName, getterMultipler, 0);
    }

    public Hook(String realName, String obfuscatedName, Number getterMultipler, int type) {
        this.reflectionIndex = -1;
        this.realName = realName;
        this.obfuscatedName = obfuscatedName;
        if (getterMultipler.longValue() != -1L) {
            int len = String.valueOf(getterMultipler.longValue()).length();
            if (len <= 3) {
                this.getterMultipler = getterMultipler.byteValue();
            } else if (len <= 14) {
                this.getterMultipler = getterMultipler.intValue();
            } else {
                this.getterMultipler = getterMultipler;
            }
        }

        this.type = type;
        this.setterMultipler = this.inverse(getterMultipler);
        if (obfuscatedName.contains(".")) {
            this.classLocation = obfuscatedName.substring(0, obfuscatedName.lastIndexOf("."));
            this.fieldName = obfuscatedName.substring(obfuscatedName.lastIndexOf(".") + 1, obfuscatedName.length());
        } else {
            this.classLocation = obfuscatedName;
            this.fieldName = "";
        }

    }

    public String getObfuscatedClass() {
        return this.classLocation;
    }

    public String getObfuscatedField() {
        return this.fieldName;
    }

    public boolean isField() {
        return this.type == 0;
    }

    public boolean isMethod() {
        return this.type == 1;
    }

    public int inverse(Number input) {
        if (input != null && this.isField() && input.longValue() != -1L) {
            BigInteger a = BigInteger.valueOf(input.longValue());
            BigInteger modulus = BigInteger.ONE.shiftLeft(32);
            return a.modInverse(modulus).intValue();
        } else {
            return -1;
        }
    }
}
