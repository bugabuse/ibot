package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IStream;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Stream extends Wrapper {
    public Stream(Object instance) {
        super(instance);
    }

    public static IStream getStreamInterface() {
        return Bot.get().accessorInterface.streamInterface;
    }

    private static byte[] encodeString(CharSequence charsequence1) {
        int var2 = charsequence1.length();
        byte[] var5 = new byte[var2];

        for (int var4 = 0; var4 < var2; ++var4) {
            char var3 = charsequence1.charAt(var4);
            if ((var3 <= 0 || var3 >= 128) && (var3 < 160 || var3 > 255)) {
                if (var3 == 8364) {
                    var5[var4] = -128;
                } else if (var3 == 8218) {
                    var5[var4] = -126;
                } else if (var3 == 402) {
                    var5[var4] = -125;
                } else if (var3 == 8222) {
                    var5[var4] = -124;
                } else if (var3 == 8230) {
                    var5[var4] = -123;
                } else if (var3 == 8224) {
                    var5[var4] = -122;
                } else if (var3 == 8225) {
                    var5[var4] = -121;
                } else if (var3 == 710) {
                    var5[var4] = -120;
                } else if (var3 == 8240) {
                    var5[var4] = -119;
                } else if (var3 == 352) {
                    var5[var4] = -118;
                } else if (var3 == 8249) {
                    var5[var4] = -117;
                } else if (var3 == 338) {
                    var5[var4] = -116;
                } else if (var3 == 381) {
                    var5[var4] = -114;
                } else if (var3 == 8216) {
                    var5[var4] = -111;
                } else if (var3 == 8217) {
                    var5[var4] = -110;
                } else if (var3 == 8220) {
                    var5[var4] = -109;
                } else if (var3 == 8221) {
                    var5[var4] = -108;
                } else if (var3 == 8226) {
                    var5[var4] = -107;
                } else if (var3 == 8211) {
                    var5[var4] = -106;
                } else if (var3 == 8212) {
                    var5[var4] = -105;
                } else if (var3 == 732) {
                    var5[var4] = -104;
                } else if (var3 == 8482) {
                    var5[var4] = -103;
                } else if (var3 == 353) {
                    var5[var4] = -102;
                } else if (var3 == 8250) {
                    var5[var4] = -101;
                } else if (var3 == 339) {
                    var5[var4] = -100;
                } else if (var3 == 382) {
                    var5[var4] = -98;
                } else if (var3 == 376) {
                    var5[var4] = -97;
                } else {
                    var5[var4] = 63;
                }
            } else {
                var5[var4] = (byte) var3;
            }
        }

        return var5;
    }

    public static int writeByteArray(byte[] bytesToWrite, int start, int length, byte[] bytes4, int offset) {
        for (int i = offset; i < offset + bytesToWrite.length; ++i) {
            bytes4[i] = bytesToWrite[i - offset];
        }

        return offset + bytesToWrite.length;
    }

    public static int writeStringToByteArray(CharSequence charsequence1, int start, int length, byte[] bytes4, int offset) {
        int var6 = length - start;

        for (int var8 = 0; var8 < var6; ++var8) {
            char var7 = charsequence1.charAt(start + var8);
            if ((var7 <= 0 || var7 >= 128) && (var7 < 160 || var7 > 255)) {
                if (var7 == 8364) {
                    bytes4[var8 + offset] = -128;
                } else if (var7 == 8218) {
                    bytes4[var8 + offset] = -126;
                } else if (var7 == 402) {
                    bytes4[offset + var8] = -125;
                } else if (var7 == 8222) {
                    bytes4[offset + var8] = -124;
                } else if (var7 == 8230) {
                    bytes4[var8 + offset] = -123;
                } else if (var7 == 8224) {
                    bytes4[var8 + offset] = -122;
                } else if (var7 == 8225) {
                    bytes4[offset + var8] = -121;
                } else if (var7 == 710) {
                    bytes4[var8 + offset] = -120;
                } else if (var7 == 8240) {
                    bytes4[var8 + offset] = -119;
                } else if (var7 == 352) {
                    bytes4[var8 + offset] = -118;
                } else if (var7 == 8249) {
                    bytes4[var8 + offset] = -117;
                } else if (var7 == 338) {
                    bytes4[offset + var8] = -116;
                } else if (var7 == 381) {
                    bytes4[offset + var8] = -114;
                } else if (var7 == 8216) {
                    bytes4[var8 + offset] = -111;
                } else if (var7 == 8217) {
                    bytes4[offset + var8] = -110;
                } else if (var7 == 8220) {
                    bytes4[var8 + offset] = -109;
                } else if (var7 == 8221) {
                    bytes4[offset + var8] = -108;
                } else if (var7 == 8226) {
                    bytes4[offset + var8] = -107;
                } else if (var7 == 8211) {
                    bytes4[offset + var8] = -106;
                } else if (var7 == 8212) {
                    bytes4[var8 + offset] = -105;
                } else if (var7 == 732) {
                    bytes4[var8 + offset] = -104;
                } else if (var7 == 8482) {
                    bytes4[var8 + offset] = -103;
                } else if (var7 == 353) {
                    bytes4[offset + var8] = -102;
                } else if (var7 == 8250) {
                    bytes4[offset + var8] = -101;
                } else if (var7 == 339) {
                    bytes4[var8 + offset] = -100;
                } else if (var7 == 382) {
                    bytes4[offset + var8] = -98;
                } else if (var7 == 376) {
                    bytes4[var8 + offset] = -97;
                } else {
                    bytes4[offset + var8] = 63;
                }
            } else {
                bytes4[offset + var8] = (byte) var7;
            }
        }

        return var6;
    }

    @HookName("Stream.byteArray")
    public byte[] getByteArray() {
        return getStreamInterface().getByteArray(this.instance);
    }

    @HookName("Stream.position")
    public int getPosition() {
        return getStreamInterface().getPosition(this.instance);
    }

    @HookName("Stream.position")
    public void setPosition(int value) {
        getStreamInterface().setPosition(this.instance, value);
    }

    public void writeByte(int i1) {
        int position = this.getPosition();
        this.getByteArray()[position++] = (byte) i1;
        this.setPosition(position);
    }

    public void writeByte8(int i1) {
        int position = this.getPosition();
        this.getByteArray()[position++] = (byte) (i1 >> 8);
        this.getByteArray()[position++] = (byte) i1;
        this.setPosition(position);
    }

    public void writeStringStart(int i1) {
        if (i1 >= 0 && i1 < 128) {
            this.writeByte(i1);
        } else {
            if (i1 < 0 || i1 >= 32768) {
                throw new IllegalArgumentException();
            }

            this.writeByte8(i1 + '耀');
        }

    }

    public int writeEncodedString(String str) {
        int positionStart = this.getPosition();
        int position = this.getPosition();
        byte[] var4 = encodeString(str);
        this.writeStringStart(var4.length);
        position += writeByteArray(var4, 0, var4.length, this.getByteArray(), position);
        this.setPosition(position);
        return position - positionStart;
    }

    public void writeString(String string) {
        if (string.indexOf(0) < 0) {
            int position = this.getPosition();
            position += writeStringToByteArray(string, 0, string.length(), this.getByteArray(), position);
            this.getByteArray()[position++] = 0;
            this.setPosition(position);
        }
    }

    public void writeString2(String string) {
        if (string.indexOf(0) < 0) {
            int position = this.getPosition();
            this.getByteArray()[position++] = 0;
            position += writeStringToByteArray(string, 0, string.length(), this.getByteArray(), position);
            this.getByteArray()[position++] = 0;
            this.setPosition(position);
        }
    }

    public void writeInt8(int i) {
        int position = this.getPosition();
        this.getByteArray()[position++] = (byte) (i >> 8);
        this.getByteArray()[position++] = (byte) i;
        this.setPosition(position);
    }

    public void endPacket8(int i) {
        int position = this.getPosition();
        this.getByteArray()[position - i - 2] = (byte) (i >> 8);
        this.getByteArray()[position - i - 1] = (byte) i;
        this.setPosition(position);
    }
}
