package com.longmai.security.plugin.util;

import com.longmai.security.plugin.driver.conn.Connection;
import java.io.ByteArrayOutputStream;

public class StringUtils {
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String fromUTF8ByteArray(byte[] bytes) {
        int i = 0;
        int length = 0;
        while (i < bytes.length) {
            length++;
            if ((bytes[i] & Connection.UART_OP_KEYEXCHG) == Connection.UART_OP_KEYEXCHG) {
                length++;
                i += 4;
            } else if ((bytes[i] & 224) == 224) {
                i += 3;
            } else if ((bytes[i] & 192) == 192) {
                i += 2;
            } else {
                i++;
            }
        }
        char[] cs = new char[length];
        i = 0;
        length = 0;
        while (i < bytes.length) {
            int length2;
            char ch;
            if ((bytes[i] & Connection.UART_OP_KEYEXCHG) == Connection.UART_OP_KEYEXCHG) {
                int U = (((((bytes[i] & 3) << 18) | ((bytes[i + 1] & 63) << 12)) | ((bytes[i + 2] & 63) << 6)) | (bytes[i + 3] & 63)) - 65536;
                char W2 = (char) (56320 | (U & 1023));
                length2 = length + 1;
                cs[length] = (char) (55296 | (U >> 10));
                ch = W2;
                i += 4;
                length = length2;
            } else if ((bytes[i] & 224) == 224) {
                ch = (char) ((((bytes[i] & 15) << 12) | ((bytes[i + 1] & 63) << 6)) | (bytes[i + 2] & 63));
                i += 3;
            } else if ((bytes[i] & 208) == 208) {
                ch = (char) (((bytes[i] & 31) << 6) | (bytes[i + 1] & 63));
                i += 2;
            } else if ((bytes[i] & 192) == 192) {
                ch = (char) (((bytes[i] & 31) << 6) | (bytes[i + 1] & 63));
                i += 2;
            } else {
                ch = (char) (bytes[i] & 255);
                i++;
            }
            length2 = length + 1;
            cs[length] = ch;
            length = length2;
        }
        return new String(cs);
    }

    public static byte[] toUTF8ByteArray(String string) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        char[] c = string.toCharArray();
        int i = 0;
        while (i < c.length) {
            char ch = c[i];
            if (ch < '') {
                bOut.write(ch);
            } else if (ch < 'ࠀ') {
                bOut.write((ch >> 6) | 192);
                bOut.write((ch & 63) | 128);
            } else if (ch < '?' || ch > '?') {
                bOut.write((ch >> 12) | 224);
                bOut.write(((ch >> 6) & 63) | 128);
                bOut.write((ch & 63) | 128);
            } else if (i + 1 >= c.length) {
                throw new IllegalStateException("invalid UTF-16 codepoint");
            } else {
                char W1 = ch;
                i++;
                char W2 = c[i];
                if (W1 > '?') {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                int codePoint = (((W1 & 1023) << 10) | (W2 & 1023)) + 65536;
                bOut.write((codePoint >> 18) | Connection.UART_OP_KEYEXCHG);
                bOut.write(((codePoint >> 12) & 63) | 128);
                bOut.write(((codePoint >> 6) & 63) | 128);
                bOut.write((codePoint & 63) | 128);
            }
            i++;
        }
        return bOut.toByteArray();
    }

    public static String toUpperCase(String string) {
        boolean changed = false;
        char[] chars = string.toCharArray();
        for (int i = 0; i != chars.length; i++) {
            char ch = chars[i];
            if ('a' <= ch && 'z' >= ch) {
                changed = true;
                chars[i] = (char) ((ch - 97) + 65);
            }
        }
        if (changed) {
            return new String(chars);
        }
        return string;
    }

    public static String toLowerCase(String string) {
        boolean changed = false;
        char[] chars = string.toCharArray();
        for (int i = 0; i != chars.length; i++) {
            char ch = chars[i];
            if ('A' <= ch && 'Z' >= ch) {
                changed = true;
                chars[i] = (char) ((ch - 65) + 97);
            }
        }
        if (changed) {
            return new String(chars);
        }
        return string;
    }

    public static byte[] toByteArray(String string) {
        byte[] bytes = new byte[string.length()];
        for (int i = 0; i != bytes.length; i++) {
            bytes[i] = (byte) string.charAt(i);
        }
        return bytes;
    }
}
