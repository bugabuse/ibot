package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IIsaacWrapper;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class IsaacWrapper extends Wrapper {
    public static String[] staticStringArray4;
    int anInt141;
    int anInt142;
    int anInt144;
    int anInt143;
    int[] anIntArray39 = new int[256];

    public IsaacWrapper(Object instance) {
        super(instance);
    }

    public static IIsaacWrapper getIsaacWrapperInterface() {
        return Bot.get().accessorInterface.isaacWrapperInterface;
    }

    @HookName("IsaacWrapper.key")
    public int[] getKey() {
        return getIsaacWrapperInterface().getKey(this.instance);
    }

    final void method266() {
        int var2 = -1640531527;
        int var4 = -1640531527;
        int var5 = -1640531527;
        int var6 = -1640531527;
        int var7 = -1640531527;
        int var8 = -1640531527;
        int var9 = -1640531527;
        int var10 = -1640531527;

        int var3;
        for (var3 = 0; var3 < 4; ++var3) {
            var10 ^= var9 << 11;
            var7 += var10;
            var9 += var8;
            var9 ^= var8 >>> 2;
            var6 += var9;
            var8 += var7;
            var8 ^= var7 << 8;
            var5 += var8;
            var7 += var6;
            var7 ^= var6 >>> 16;
            var4 += var7;
            var6 += var5;
            var6 ^= var5 << 10;
            var2 += var6;
            var5 += var4;
            var5 ^= var4 >>> 4;
            var10 += var5;
            var4 += var2;
            var4 ^= var2 << 8;
            var9 += var4;
            var2 += var10;
            var2 ^= var10 >>> 9;
            var8 += var2;
            var10 += var9;
        }

        for (var3 = 0; var3 < 256; var3 += 8) {
            var10 += this.getKey()[var3];
            var9 += this.getKey()[var3 + 1];
            var8 += this.getKey()[var3 + 2];
            var7 += this.getKey()[var3 + 3];
            var6 += this.getKey()[var3 + 4];
            var5 += this.getKey()[var3 + 5];
            var4 += this.getKey()[var3 + 6];
            var2 += this.getKey()[var3 + 7];
            var10 ^= var9 << 11;
            var7 += var10;
            var9 += var8;
            var9 ^= var8 >>> 2;
            var6 += var9;
            var8 += var7;
            var8 ^= var7 << 8;
            var5 += var8;
            var7 += var6;
            var7 ^= var6 >>> 16;
            var4 += var7;
            var6 += var5;
            var6 ^= var5 << 10;
            var2 += var6;
            var5 += var4;
            var5 ^= var4 >>> 4;
            var10 += var5;
            var4 += var2;
            var4 ^= var2 << 8;
            var9 += var4;
            var2 += var10;
            var2 ^= var10 >>> 9;
            var8 += var2;
            var10 += var9;
            this.anIntArray39[var3] = var10;
            this.anIntArray39[var3 + 1] = var9;
            this.anIntArray39[var3 + 2] = var8;
            this.anIntArray39[var3 + 3] = var7;
            this.anIntArray39[var3 + 4] = var6;
            this.anIntArray39[var3 + 5] = var5;
            this.anIntArray39[var3 + 6] = var4;
            this.anIntArray39[var3 + 7] = var2;
        }

        for (var3 = 0; var3 < 256; var3 += 8) {
            var10 += this.anIntArray39[var3];
            var9 += this.anIntArray39[var3 + 1];
            var8 += this.anIntArray39[var3 + 2];
            var7 += this.anIntArray39[var3 + 3];
            var6 += this.anIntArray39[var3 + 4];
            var5 += this.anIntArray39[var3 + 5];
            var4 += this.anIntArray39[var3 + 6];
            var2 += this.anIntArray39[var3 + 7];
            var10 ^= var9 << 11;
            var7 += var10;
            var9 += var8;
            var9 ^= var8 >>> 2;
            var6 += var9;
            var8 += var7;
            var8 ^= var7 << 8;
            var5 += var8;
            var7 += var6;
            var7 ^= var6 >>> 16;
            var4 += var7;
            var6 += var5;
            var6 ^= var5 << 10;
            var2 += var6;
            var5 += var4;
            var5 ^= var4 >>> 4;
            var10 += var5;
            var4 += var2;
            var4 ^= var2 << 8;
            var9 += var4;
            var2 += var10;
            var2 ^= var10 >>> 9;
            var8 += var2;
            var10 += var9;
            this.anIntArray39[var3] = var10;
            this.anIntArray39[var3 + 1] = var9;
            this.anIntArray39[var3 + 2] = var8;
            this.anIntArray39[var3 + 3] = var7;
            this.anIntArray39[var3 + 4] = var6;
            this.anIntArray39[var3 + 5] = var5;
            this.anIntArray39[var3 + 6] = var4;
            this.anIntArray39[var3 + 7] = var2;
        }

        this.method267();
        this.anInt141 = 256;
    }

    final void method267() {
        this.anInt144 += (this.anInt142 += 127667623) * -121386473;

        for (int var2 = 0; var2 < 256; ++var2) {
            int var3 = this.anIntArray39[var2];
            if ((var2 & 2) == 0) {
                if ((var2 & 1) == 0) {
                    this.anInt143 ^= this.anInt143 << 13;
                } else {
                    this.anInt143 ^= this.anInt143 >>> 6;
                }
            } else if ((var2 & 1) == 0) {
                this.anInt143 ^= this.anInt143 << 2;
            } else {
                this.anInt143 ^= this.anInt143 >>> 16;
            }

            this.anInt143 += this.anIntArray39[var2 + 128 & 255];
            int var4;
            this.anIntArray39[var2] = var4 = this.anInt143 + this.anIntArray39[(var3 & 1020) >> 2] + this.anInt144;
            this.getKey()[var2] = this.anInt144 = this.anIntArray39[(var4 >> 8 & 1020) >> 2] + var3;
        }

    }

    final int getEncryptedInt() {
        if (--this.anInt141 + 1 == 0) {
            this.method267();
            this.anInt141 = 255;
        }

        return this.getKey()[this.anInt141];
    }
}
