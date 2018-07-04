package com.longmai.security.plugin.util;

public class Int2Bytes {
    public static byte[] int2byte(int Value, int len, boolean IsBigEnd) {
        byte[] abyte = new byte[len];
        int i;
        if (IsBigEnd) {
            for (i = 0; i < len; i++) {
                abyte[(len - i) - 1] = (byte) ((Value >> (i * 8)) & 255);
            }
        } else {
            for (i = 0; i < len; i++) {
                abyte[i] = (byte) ((Value >> (i * 8)) & 255);
            }
        }
        return abyte;
    }

    public static int bytes2int(byte[] bytes, boolean IsBigEnd) {
        int addr = bytes[0] & 255;
        int i;
        if (IsBigEnd) {
            for (i = 1; i < bytes.length; i++) {
                addr = (addr << 8) | (bytes[i] & 255);
            }
        } else {
            for (i = 1; i < bytes.length; i++) {
                addr |= (bytes[i] & 255) << (i * 8);
            }
        }
        return addr;
    }

    public static int bytes2int(byte[] bytes, int len, boolean IsBigEnd) {
        int addr = bytes[0] & 255;
        int i;
        if (IsBigEnd) {
            for (i = 1; i < len; i++) {
                addr = (addr << 8) | (bytes[i] & 255);
            }
        } else {
            for (i = 1; i < len; i++) {
                addr |= (bytes[i] & 255) << (i * 8);
            }
        }
        return addr;
    }

    public static int bytes2int(byte[] bytes, int off, int len, boolean IsBigEnd) {
        int addr = bytes[off] & 255;
        int i;
        if (IsBigEnd) {
            for (i = 1; i < len; i++) {
                addr = (addr << 8) | (bytes[off + i] & 255);
            }
        } else {
            for (i = 1; i < len; i++) {
                addr |= (bytes[off + i] & 255) << (i * 8);
            }
        }
        return addr;
    }
}
